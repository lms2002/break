package com.example.breakApp.member.service

import com.example.breakApp.common.authority.JwtTokenProvider
import com.example.breakApp.common.authority.TokenInfo
import com.example.breakApp.common.exception.InvalidInputException
import com.example.breakApp.common.status.Gender
import org.springframework.security.core.Authentication
import com.example.breakApp.member.dto.LoginDto
import com.example.breakApp.member.dto.MemberDtoRequest
import com.example.breakApp.member.dto.MemberDtoResponse
import com.example.breakApp.member.dto.UpdateDtoRequest
import com.example.breakApp.member.entity.Member
import com.example.breakApp.member.entity.VerificationToken
import com.example.breakApp.member.entity.VerifiedEmail
import com.example.breakApp.member.repository.*
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val emailSender: JavaMailSender,
    private val passwordEncoder: PasswordEncoder,
    private val verificationTokenRepository: VerificationTokenRepository,
    private val verifiedEmailRepository: VerifiedEmailRepository,
) {
    /**
     * 회원가입
     * 로그인 ID와 이메일 인증 상태를 확인하고, 비밀번호를 해시화한 후 새로운 회원을 등록합니다.
     */
    fun signUp(memberDtoRequest: MemberDtoRequest): String {
        // 1. 로그인 ID 중복 검사
        if (isLoginIdDuplicate(memberDtoRequest.loginId)) {
            throw InvalidInputException("loginId", "이미 사용 중인 로그인 ID입니다.")
        }

        // 2. 이메일 인증 확인 (VerifiedEmail에 해당 이메일이 있는지 확인)
        val verifiedEmail = verifiedEmailRepository.findByEmail(memberDtoRequest.email)
            ?: throw InvalidInputException("email", "이메일 인증이 필요합니다.")

        // 3. 비밀번호 해시화 및 Member 객체 생성
        val hashedPassword = passwordEncoder.encode(memberDtoRequest.password)
        val member = Member(
            loginId = memberDtoRequest.loginId,
            password = hashedPassword,
            userName = memberDtoRequest.userName,
            email = memberDtoRequest.email,
            gender = memberDtoRequest.gender
        )

        memberRepository.save(member)

        // 4. 회원가입 후 VerifiedEmail 기록 삭제
        verifiedEmailRepository.delete(verifiedEmail)

        return "회원가입이 완료되었습니다."
    }


    /**
     * 로그인 아이디 중복 검증
     * 주어진 로그인 ID가 이미 존재하는지 확인
     */
    fun isLoginIdDuplicate(loginId: String): Boolean {
        return memberRepository.findByLoginId(loginId) != null
    }
    /**
     * 이메일 인증 코드 전송 요청
     * 이메일 인증을 위한 인증 코드를 생성하고, 해당 이메일로 전송
     */
    fun requestEmailVerification(email: String): String {
        // 이메일이 이미 등록된 경우 예외 처리
        if (memberRepository.findByEmail(email) != null) {
            throw InvalidInputException("email", "이미 가입된 이메일입니다.")
        }

        // 인증 코드 생성
        val verificationCode = generateVerificationCode()

        // 인증 토큰 생성 및 저장
        val verificationToken = VerificationToken(
            token = verificationCode,
            member = null,  // 아직 회원가입 전이므로 회원 정보는 없을 수 있습니다.
            expiryDate = LocalDateTime.now().plusMinutes(5) // 인증번호 유효기간 5분
        )
        verificationTokenRepository.save(verificationToken)

        // 이메일로 인증 코드 전송
        sendVerificationCodeEmail(email, verificationCode)

        return "인증 코드가 이메일로 전송되었습니다."
    }
    /**
     * 이메일 인증 검증
     * 주어진 이메일과 인증 코드가 유효한지 확인한 후, VerifiedEmail 테이블에 저장
     */
    @Transactional
    fun verifyEmail(email: String, token: String): String {
        // 토큰 조회
        val verificationToken = verificationTokenRepository.findByToken(token)
            ?: throw InvalidInputException("token", "유효하지 않은 인증 코드입니다.")

        // 토큰 만료 여부 확인
        if (verificationToken.isExpired()) {
            throw InvalidInputException("token", "인증 코드가 만료되었습니다.")
        }

        // 이메일 인증 완료 처리: VerifiedEmail 테이블에 저장
        verifiedEmailRepository.save(VerifiedEmail(email = email))

        // 인증 후 사용된 토큰 삭제
        verificationTokenRepository.delete(verificationToken)

        return "이메일 인증이 완료되었습니다!"
    }

    /**
     * 이메일로 6자리 인증 코드를 전송하는 메서드
     * @param email 사용자 이메일 주소
     * @param code 6자리 인증 코드
     */
    private fun sendVerificationCodeEmail(email: String, code: String) {
        val message = SimpleMailMessage()
        message.setTo(email)
        message.subject = "이메일 인증 코드"
        message.text = """
        안녕하세요,
        
        회원가입을 위한 인증 코드는 다음과 같습니다:
        [$code]
        
        이 인증 코드는 발송 후 5분 동안 유효합니다.
    """.trimIndent()
        emailSender.send(message)
    }


    /**
     * 이메일 랜덤 인증 번호
     * 6자리의 랜덤 인증 코드를 생성하여 반환
     */
    fun generateVerificationCode(): String {
        return (100000..999999).random().toString()
    }

    /**
     * 로그인 -> 토큰 발행
     * 로그인 ID와 비밀번호로 사용자를 인증하고, Access 및 Refresh 토큰을 발행
     */
    fun login(loginDto: LoginDto): TokenInfo {
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.loginId, loginDto.password)
        // 데이터베이스의 멤버 정보와 비교
        val authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        // Access Token과 Refresh Token 생성
        val accessToken = jwtTokenProvider.createToken(authentication)
        val refreshToken = jwtTokenProvider.createRefreshToken()

        // 사용자 정보 가져오기
        val member = memberRepository.findByLoginId(loginDto.loginId)
            ?: throw InvalidInputException("loginId", "존재하지 않는 사용자입니다.")

        // Refresh Token을 사용자 엔티티에 저장하고 DB에 업데이트
        member.refreshToken = refreshToken
        memberRepository.save(member)

        // Access Token과 Refresh Token을 함께 반환
        return TokenInfo("Bearer", accessToken.accessToken, refreshToken)
    }
    /**
     * Access Token 갱신
     */
    fun refreshAccessToken(refreshToken: String): String {
        // Refresh Token 검증
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw RuntimeException("유효하지 않은 리프레시 토큰입니다.")
        }

        // Refresh Token에서 사용자 ID 추출
        val userId = jwtTokenProvider.getUserIdFromToken(refreshToken)
        val user = memberRepository.findByIdOrNull(userId)
            ?: throw RuntimeException("존재하지 않는 사용자입니다.")

        // 저장된 Refresh Token과 일치하는지 확인
        if (user.refreshToken != refreshToken) {
            throw RuntimeException("리프레시 토큰이 일치하지 않습니다.")
        }

        // userId 기반으로 Authentication 객체 생성
        val authentication = createAuthentication(userId)

        // 새로운 Access Token 생성 및 반환
        return jwtTokenProvider.createToken(authentication).accessToken
    }
    // userId를 기반으로 Authentication 객체 생성
    fun createAuthentication(userId: Long): Authentication {
        return UsernamePasswordAuthenticationToken(userId, null, emptyList()) // 권한 리스트는 빈 리스트 사용
    }

    /**
     * 내 정보 조회
     */
    fun searchMyInfo(id: Long): MemberDtoResponse {
        val member: Member = memberRepository.findByIdOrNull(id) ?: throw InvalidInputException("id", "회원번호(${id})가 존재하지 않는 유저입니다.")
        return member.toDto()
    }

    /**
     * 내 정보 수정
     */
    fun saveMyInfo(updateDtoRequest: UpdateDtoRequest): String {
        val memberId = updateDtoRequest.id ?: throw InvalidInputException("id", "ID가 입력되지 않았습니다.")
        val existingMember = memberRepository.findByIdOrNull(memberId)
            ?: throw InvalidInputException("id", "회원번호(${memberId})가 존재하지 않는 유저입니다.")

        // 비밀번호가 있는 경우 해시화하여 업데이트
        updateDtoRequest.password?.let {
            existingMember.password = passwordEncoder.encode(it)  // 해시화된 비밀번호로 저장
        }
        updateDtoRequest.userName?.let { existingMember.userName = it }
        updateDtoRequest.email?.let { existingMember.email = it }
        updateDtoRequest.gender?.let { existingMember.gender = Gender.valueOf(it) }

        // 변경된 정보 저장
        memberRepository.save(existingMember)
        return "수정이 완료되었습니다."
    }

    // 대소문자 구분하며 이메일 찾는 함수
    fun findIdByEmail(email: String) {
        val member = memberRepository.findByEmail(email)
            ?: throw NoSuchElementException("이메일을 찾을 수 없습니다.")

        println("찾은 멤버의 아이디: ${member.loginId}")  // 로그 추가
        sendEmailWithLoginId(email, member.loginId)
    }

    private fun sendEmailWithLoginId(email: String, loginId: String) {
        val message = SimpleMailMessage()
        message.setTo(email)  // 배열 대신 단일 이메일을 그대로 사용
        message.subject = "아이디 찾기 결과"
        message.text = "귀하의 아이디는 다음과 같습니다: $loginId"

        emailSender.send(message)
    }

    /**
     * 이메일을 기반으로 사용자를 찾고, 임시 비밀번호를 생성하여 이메일로 전송
     */
    fun resetPassword(email: String) {
        // 1. 이메일로 사용자 찾기
        val member = memberRepository.findByEmail(email)
            ?: throw NoSuchElementException("해당 이메일을 가진 사용자가 없습니다.")

        // 2. 임시 비밀번호 생성
        val temporaryPassword = generateTemporaryPassword()

        // 3. 임시 비밀번호를 해시하여 사용자 정보에 저장
        val hashedPassword = passwordEncoder.encode(temporaryPassword)  // 해시화된 비밀번호 생성
        member.password = hashedPassword  // 해시화된 비밀번호로 저장
        memberRepository.save(member)

        // 4. 임시 비밀번호 이메일 전송
        sendEmailWithTemporaryPassword(email, temporaryPassword)
    }


    /**
     * 임시 비밀번호를 생성하는 메서드. 10자리의 랜덤 문자열을 반환합니다.
     */
    private fun generateTemporaryPassword(): String {
        val chars = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..10)
            .map { chars.random() }
            .joinToString("")
    }

    /**
     * 임시 비밀번호를 포함한 이메일을 전송하는 메서드.
     */
    private fun sendEmailWithTemporaryPassword(email: String, temporaryPassword: String) {
        val message = SimpleMailMessage()
        message.setTo(email)
        message.subject = "임시 비밀번호 발급"
        message.text = "귀하의 임시 비밀번호는 다음과 같습니다: $temporaryPassword\n" +
                "로그인 후 즉시 비밀번호를 변경해 주시기 바랍니다."

        emailSender.send(message)
    }
}
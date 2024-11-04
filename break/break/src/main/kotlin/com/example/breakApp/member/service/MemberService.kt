package com.example.breakApp.member.service

import com.example.breakApp.common.authority.JwtTokenProvider
import com.example.breakApp.common.authority.TokenInfo
import com.example.breakApp.common.dto.CustomUser
import com.example.breakApp.common.exception.InvalidInputException
import com.example.breakApp.common.status.Gender
import com.example.breakApp.common.status.ROLE
import com.example.breakApp.member.dto.LoginDto
import com.example.breakApp.member.dto.MemberDtoRequest
import com.example.breakApp.member.dto.MemberDtoResponse
import com.example.breakApp.member.dto.UpdateDtoRequest
import com.example.breakApp.member.entity.Member
import com.example.breakApp.member.entity.MemberRole
import com.example.breakApp.member.repository.MemberRepository
import com.example.breakApp.member.repository.MemberRoleRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val emailSender: JavaMailSender,
    private val passwordEncoder: PasswordEncoder,
) {
    /**
     * 회원가입
     */
    fun signUp(memberDtoRequest: MemberDtoRequest): String {
        // ID 중복 검사
        var member: Member? = memberRepository.findByLoginId(memberDtoRequest.loginId)
        if (member != null) {
            throw InvalidInputException("loginId", "이미 등록된 ID 입니다.")
        }

        // 비밀번호 해시화
        val hashedPassword = passwordEncoder.encode(memberDtoRequest.password)

        // Member 엔티티 생성 시 해시화된 비밀번호 사용
        member = memberDtoRequest.toEntity().apply {
            password = hashedPassword  // 해시화된 비밀번호 설정
        }
        memberRepository.save(member)

        // 기본 멤버 권한 설정
        val memberRole: MemberRole = MemberRole(null, ROLE.MEMBER, member)
        memberRoleRepository.save(memberRole)

        return "회원가입이 완료되었습니다."
    }

    /**
     * 로그인 -> 토큰 발행
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

        // 새로운 Access Token 생성 및 반환
        return jwtTokenProvider.createToken(user.toAuthentication()).accessToken
    }

    fun getMemberById(userId: Long): Member {
        return memberRepository.findById(userId)
            .orElseThrow { RuntimeException("User not found") }
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

        // null이 아닌 경우에만 필드 업데이트
        updateDtoRequest.password?.let { existingMember.password = it }
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
     * 이메일을 기반으로 사용자를 찾고, 임시 비밀번호를 생성하여 이메일로 전송합니다.
     */
    fun resetPassword(email: String) {
        // 1. 이메일로 사용자 찾기
        val member = memberRepository.findByEmail(email)
            ?: throw NoSuchElementException("해당 이메일을 가진 사용자가 없습니다.")

        // 2. 임시 비밀번호 생성
        val temporaryPassword = generateTemporaryPassword()

        // 3. 임시 비밀번호를 해시하여 사용자 정보에 저장
        member.password = temporaryPassword
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
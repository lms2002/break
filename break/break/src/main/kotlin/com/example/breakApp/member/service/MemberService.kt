package com.example.breakApp.member.service

import com.example.breakApp.common.authority.JwtTokenProvider
import com.example.breakApp.common.authority.TokenInfo
import com.example.breakApp.common.exception.InvalidInputException
import com.example.breakApp.common.status.ROLE
import com.example.breakApp.member.dto.LoginDto
import com.example.breakApp.member.dto.MemberDtoRequest
import com.example.breakApp.member.dto.MemberDtoResponse
import com.example.breakApp.member.entity.Member
import com.example.breakApp.member.entity.MemberRole
import com.example.breakApp.member.repository.MemberRepository
import com.example.breakApp.member.repository.MemberRoleRepository
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberRoleRepository: MemberRoleRepository,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val jwtTokenProvider: JwtTokenProvider,
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

        member = memberDtoRequest.toEntity()
        memberRepository.save(member)

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

    /**
     * 내 정보 조회
     */
//    fun searchMyInfo(id: Long): MemberDtoResponse {
//        val member: Member = memberRepository.findByIdOrNull(id) ?: throw InvalidInputException("id", "회원번호(${id})가 존재하지 않는 유저입니다.")
//        return member.toDto()
//    }

    /**
     * 내 정보 수정
     */
//    fun saveMyInfo(memberDtoRequest: MemberDtoRequest): String {
//        val member: Member = memberDtoRequest.toEntity()
//        memberRepository.save(member)
//        return "수정이 완료되었습니다."
//    }
}
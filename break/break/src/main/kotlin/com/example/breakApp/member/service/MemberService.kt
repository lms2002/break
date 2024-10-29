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
        
        // 문제가 없다면 토큰 제공
        return jwtTokenProvider.createToken(authentication)
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
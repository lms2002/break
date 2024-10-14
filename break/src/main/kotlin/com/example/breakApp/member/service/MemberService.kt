package com.example.breakApp.member.service

import com.example.breakApp.member.dto.MemberDtoRequest
import com.example.breakApp.member.entity.Member
import com.example.breakApp.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.LocalDate

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository
) {

    /**
     * 회원가입
     */
    fun signUp(memberDtoRequest: MemberDtoRequest): String {
        // ID 중복 검사
        val existingMember: Member? = memberRepository.findByLoginId(memberDtoRequest.loginId)
        if (existingMember != null) {
            return "이미 등록된 ID 입니다."
        }

        // 새로운 회원 정보 저장
        val member = Member(
            loginId = memberDtoRequest.loginId,
            password = memberDtoRequest.password,
            userName = memberDtoRequest.userName,
            createdAt = LocalDate.now(),
            email = memberDtoRequest.email,
            gender = memberDtoRequest.gender
        )

        // DB에 저장
        memberRepository.save(member)
        return "회원가입이 완료되었습니다."
    }
}

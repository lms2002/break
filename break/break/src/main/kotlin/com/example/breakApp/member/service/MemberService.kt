package com.example.breakApp.member.service

import com.example.breakApp.common.exception.InvalidInputException
import com.example.breakApp.member.dto.MemberDtoRequest
import com.example.breakApp.member.entity.Member
import com.example.breakApp.member.repository.MemberRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Transactional
@Service
class MemberService(
    private val memberRepository: MemberRepository // 의존성 주입
) {
    /**
     * 회원가입
     */
    fun signUp(memberDtoRequest: MemberDtoRequest): String {
        // ID 중복 검사
        val existingMember: Member? = memberRepository.findByLoginId(memberDtoRequest.loginId)
        if (existingMember != null) {
            throw InvalidInputException("loginId", "이미 등록된 ID 입니다.")
        }

        // toEntity 함수를 이용 해 새로운 회원 정보 저장
        val member = memberDtoRequest.toEntity() // DTO 데이터를 엔터티로 변환(멤버로 저장 할 수 있는 형태로)
        // DB에 저장
        memberRepository.save(member)
        return "회원가입이 완료되었습니다."
    }
}

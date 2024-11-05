package com.example.breakApp.inbody.service

import com.example.breakApp.inbody.entity.InBody
import com.example.breakApp.inbody.repository.InBodyRepository
import com.example.breakApp.member.entity.Member
import com.example.breakApp.member.repository.MemberRepository // MemberRepository 추가
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InBodyService(
    private val inBodyRepository: InBodyRepository,
    private val memberRepository: MemberRepository // MemberRepository 주입
) {

    @Transactional
    fun createInBody(inBody: InBody): InBody {
        return inBodyRepository.save(inBody) // 인바디 데이터 저장
    }

    fun getInBodyByMember(member: Member): List<InBody> {
        return inBodyRepository.findByMember(member) // 사용자의 인바디 데이터 조회
    }

    fun getMemberById(userId: Long): Member? {
        return memberRepository.findById(userId).orElse(null) // ID로 Member 조회
    }

    @Transactional
    fun updateInBody(inBody: InBody): InBody {
        return inBodyRepository.save(inBody) // 인바디 데이터 수정
    }

    @Transactional
    fun deleteInBody(inBodyId: Long) {
        inBodyRepository.deleteById(inBodyId) // 인바디 데이터 삭제
    }
}

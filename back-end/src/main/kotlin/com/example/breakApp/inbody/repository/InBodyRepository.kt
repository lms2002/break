package com.example.breakApp.inbody.repository

import com.example.breakApp.inbody.entity.InBody
import com.example.breakApp.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InBodyRepository : JpaRepository<InBody, Long> {
    fun findByMember(member: Member): List<InBody> // 특정 회원의 모든 인바디 데이터를 조회
}
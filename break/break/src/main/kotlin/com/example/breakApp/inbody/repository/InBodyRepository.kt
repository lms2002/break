package com.example.breakApp.inbody.repository

import com.example.breakApp.inbody.entity.InBody
import com.example.breakApp.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface InBodyRepository : JpaRepository<InBody, Long> {
    fun findByMember(member: Member): List<InBody> // Member를 통해 InBody 리스트 조회
}

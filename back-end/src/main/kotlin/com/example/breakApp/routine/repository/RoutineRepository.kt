package com.example.breakApp.routine.repository

import com.example.breakApp.member.entity.Member
import com.example.breakApp.routine.entity.Routine
import org.springframework.data.jpa.repository.JpaRepository

interface RoutineRepository : JpaRepository<Routine, Long> {
    fun findByMember(member: Member): List<Routine>
}
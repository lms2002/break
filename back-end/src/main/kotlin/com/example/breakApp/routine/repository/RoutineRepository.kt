package com.example.breakApp.routine.repository

import com.example.breakApp.member.entity.Member
import com.example.breakApp.routine.entity.Routine
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface RoutineRepository : JpaRepository<Routine, Long> {
    fun findByMember(member: Member): List<Routine>
    @Query("SELECT r FROM Routine r WHERE r.createdAt < :cutoff AND r.routineId NOT IN (SELECT wl.routine.routineId FROM WorkoutLog wl WHERE wl.startTime >= :cutoff)")
    fun findUnstartedRoutines(@Param("cutoff") cutoff: LocalDateTime): List<Routine>
}
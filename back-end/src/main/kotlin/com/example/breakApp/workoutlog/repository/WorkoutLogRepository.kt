package com.example.breakApp.workoutlog.repository

import com.example.breakApp.workoutlog.entity.WorkoutLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface WorkoutLogRepository : JpaRepository<WorkoutLog, Long> {
    fun findByMemberUserId(userId: Long): List<WorkoutLog>
}
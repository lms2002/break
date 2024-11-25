package com.example.breakApp.workoutlog.repository

import com.example.breakApp.workoutlog.entity.WorkoutLog
import org.springframework.data.jpa.repository.JpaRepository

interface WorkoutLogRepository : JpaRepository<WorkoutLog, Long> {
    fun findAllByRoutineRoutineId(routineId: Long): List<WorkoutLog>
}
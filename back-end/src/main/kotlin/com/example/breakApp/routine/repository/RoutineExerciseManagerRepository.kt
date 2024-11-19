package com.example.breakApp.routine.repository

import com.example.breakApp.routine.entity.RoutineExerciseManager
import org.springframework.data.jpa.repository.JpaRepository

interface RoutineExerciseManagerRepository : JpaRepository<RoutineExerciseManager, Long> {
    fun findByRoutineRoutineId(routineId: Long): List<RoutineExerciseManager>
}
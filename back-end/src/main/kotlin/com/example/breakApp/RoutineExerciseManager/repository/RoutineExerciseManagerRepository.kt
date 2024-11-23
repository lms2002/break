package com.example.breakApp.RoutineExerciseManager.repository

import com.example.breakApp.RoutineExerciseManager.entity.RoutineExerciseManager
import com.example.breakApp.RoutineExerciseManager.entity.RoutineExerciseManagerId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoutineExerciseManagerRepository : JpaRepository<RoutineExerciseManager, RoutineExerciseManagerId> {
    fun findByRoutineId(routineId: Long): List<RoutineExerciseManager>
    fun findByExerciseId(exerciseId: Long): List<RoutineExerciseManager>
}
package com.example.breakApp.routine.repository

import com.example.breakApp.routine.entity.Routine
import com.example.breakApp.routine.entity.RoutineExerciseManager
import com.example.breakApp.routine.entity.RoutineExerciseManagerId
import org.springframework.data.jpa.repository.JpaRepository

interface RoutineExerciseManagerRepository : JpaRepository<RoutineExerciseManager, RoutineExerciseManagerId> {
    // 특정 루틴의 모든 운동 조회
    fun findByRoutineRoutineId(routineId: Long): List<RoutineExerciseManager>
    fun deleteByRoutine(routine: Routine)
}
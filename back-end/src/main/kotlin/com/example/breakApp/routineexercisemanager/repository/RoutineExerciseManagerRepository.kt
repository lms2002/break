package com.example.breakApp.routineexercisemanager.repository

import com.example.breakApp.routineexercisemanager.entity.RoutineExerciseManager
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoutineExerciseManagerRepository : JpaRepository<RoutineExerciseManager, RoutineExerciseManager.RoutineExerciseManagerId> {

    // 특정 루틴에 포함된 모든 운동을 조회하는 메서드
    fun findByRoutineRoutineId(routineId: Long): List<RoutineExerciseManager>

    // 특정 루틴에 특정 운동이 포함되어 있는지 조회하는 메서드
    fun findByRoutineRoutineIdAndExerciseExerciseId(routineId: Long, exerciseId: Long): RoutineExerciseManager?
}

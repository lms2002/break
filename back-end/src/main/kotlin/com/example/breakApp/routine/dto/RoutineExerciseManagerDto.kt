package com.example.breakApp.routine.dto

import com.example.breakApp.exercise.entity.Exercise
import com.example.breakApp.routine.entity.Routine
import com.example.breakApp.routine.entity.RoutineExerciseManager

/**
 * RoutineExerciseManager 엔티티의 데이터 전송 객체
 */
data class RoutineExerciseManagerDto(
    val id: Long? = null, // RoutineExerciseManager ID
    val routineId: Long, // 연관된 루틴 ID
    val exerciseId: Long, // 연관된 운동 ID
    val sets: Int, // 세트 수
    val repetitions: Int, // 반복 횟수
    val weight: Float? = null, // 중량
    val restTimeSeconds: Int? = null // 휴식 시간
) {
    /**
     * DTO -> Entity 변환 메서드
     */
    fun toEntity(routine: Routine, exercise: Exercise): RoutineExerciseManager = RoutineExerciseManager(
        routine = routine,
        exercise = exercise,
        sets = sets,
        repetitions = repetitions,
        weight = weight ?: 10f, // 기본값 10
        restTimeSeconds = restTimeSeconds ?: 30 // 기본값 30초
    )
}

/**
 * Entity -> DTO 변환 확장 함수
 */
fun RoutineExerciseManager.toDto(): RoutineExerciseManagerDto = RoutineExerciseManagerDto(
    id = this.id,
    routineId = this.routine.routineId!!,
    exerciseId = this.exercise.exerciseId!!,
    sets = this.sets,
    repetitions = this.repetitions,
    weight = this.weight,
    restTimeSeconds = this.restTimeSeconds
)

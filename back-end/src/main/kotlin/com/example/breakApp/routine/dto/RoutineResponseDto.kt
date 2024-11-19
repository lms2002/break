package com.example.breakApp.routine.dto

data class RoutineResponseDto(
    val routineId: Long, // 루틴 ID
    val userId: Long, // 사용자 ID
    val name: String, // 루틴 이름
    val exercises: List<RoutineExerciseResponseDto> // 연관된 운동 리스트
)

data class RoutineExerciseResponseDto(
    val exerciseId: Long, // 운동 ID
    val sets: Int, // 세트 수
    val repetitions: Int, // 반복 횟수
    val weight: Float, // 중량
    val restTimeSeconds: Int // 휴식 시간
)
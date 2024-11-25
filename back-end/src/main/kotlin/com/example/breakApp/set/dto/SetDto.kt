package com.example.breakApp.set.dto

data class ExerciseSetDto(
    val setId: Long? = null,
    val routineId: Long,
    val exerciseId: Long,  // 여전히 Long으로 유지
    val setNumber: Int,
    val repetitions: Int,
    val weight: Float,
    val isCompleted: Boolean
)
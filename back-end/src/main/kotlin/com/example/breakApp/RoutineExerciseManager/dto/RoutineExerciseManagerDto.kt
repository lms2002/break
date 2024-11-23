package com.example.breakApp.RoutineExerciseManager.dto

import java.time.LocalDateTime

data class RoutineExerciseManagerDto(
    val routineId: Long,
    val exerciseId: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
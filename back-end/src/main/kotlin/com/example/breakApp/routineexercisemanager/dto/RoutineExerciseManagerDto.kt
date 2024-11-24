package com.example.breakApp.routineexercisemanager.dto

import java.time.LocalDateTime

data class RoutineExerciseManagerDto(
    val routineId: Long,
    val exerciseId: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
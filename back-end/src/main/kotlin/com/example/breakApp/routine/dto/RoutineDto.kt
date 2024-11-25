package com.example.breakApp.routine.dto

import java.time.LocalDateTime

data class RoutineDto(
    val routineId: Long? = null,
    val userId: Long,
    val name: String,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)
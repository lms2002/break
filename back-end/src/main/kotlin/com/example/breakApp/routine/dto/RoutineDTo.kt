package com.example.breakApp.routine.dto

data class CreateRoutineDto(
    val name: String,
    val description: String
)

data class RoutineResponseDto(
    val routineId: Long,
    val userId: Long,
    val name: String,
    val description: String
)
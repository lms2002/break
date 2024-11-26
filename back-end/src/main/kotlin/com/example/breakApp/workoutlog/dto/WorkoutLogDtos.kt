package com.example.breakApp.workoutlog.dto

import java.time.LocalDateTime

data class WorkoutLogDto(
    val logId: Long,
    val exerciseId: Long,
    val routineId: Long,
    val setId: Long,
    val repetitions: Int,
    val weight: Float,
    val isCompleted: Boolean,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?,
    val duration: Int?
)
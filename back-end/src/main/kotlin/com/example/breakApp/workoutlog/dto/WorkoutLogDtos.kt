package com.example.breakApp.workoutlog.dto

import com.example.breakApp.routine.dto.RoutineDto
import java.time.LocalDateTime

data class WorkoutLogDto(
    val logId: Long,
    val routine: RoutineDto,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?,
    val duration: Long?
)
data class CompletedWorkoutDto(
    val routineId: Long,
    val completedExercises: List<CompletedExerciseDto>
)

data class CompletedExerciseDto(
    val exerciseId: Long,
    val sets: List<CompletedSetDto>
)

data class CompletedSetDto(
    val setId: Long,
    val setNumber: Int,
    val repetitions: Int,
    val weight: Float
)


data class StartWorkoutRequest(
    val routineId: Long
)
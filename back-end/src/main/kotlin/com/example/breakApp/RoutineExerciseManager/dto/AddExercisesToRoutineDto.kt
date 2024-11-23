package com.example.breakApp.RoutineExerciseManager.dto

data class AddExercisesToRoutineDto(
    val routineId: Long,
    val exercises: List<ExerciseDto>
)

data class ExerciseDto(
    val exerciseId: Long
)
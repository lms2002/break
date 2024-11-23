package com.example.breakApp.RoutineExerciseManager.entity

import java.io.Serializable

data class RoutineExerciseManagerId(
    val routineId: Long = 0,
    val exerciseId: Long = 0
) : Serializable
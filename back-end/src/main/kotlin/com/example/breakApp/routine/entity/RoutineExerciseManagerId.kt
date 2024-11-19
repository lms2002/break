package com.example.breakApp.routine.entity

import java.io.Serializable

// 복합 키를 표현하기 위한 클래스
data class RoutineExerciseManagerId(
    val routine: Long = 0, // Routine ID
    val exercise: Long = 0 // Exercise ID
) : Serializable
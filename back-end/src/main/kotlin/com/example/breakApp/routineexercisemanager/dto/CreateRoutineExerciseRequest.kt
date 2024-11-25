package com.example.breakApp.routineexercisemanager.dto

import com.fasterxml.jackson.annotation.JsonProperty

// 사실상 add
data class CreateRoutineExerciseRequest(
    @JsonProperty("routine_id")
    val routineId: Long,
    val exercises: List<ExerciseRequest>
)

data class ExerciseRequest(
    @JsonProperty("exercise_id")
    val exerciseId: Long
)
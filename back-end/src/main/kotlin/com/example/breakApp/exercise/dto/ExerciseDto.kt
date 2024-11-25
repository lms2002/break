package com.example.breakApp.exercise.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ExerciseDto(
    @JsonProperty("name")
    val name: String? = null,

    @JsonProperty("bodyPart")
    val bodyPart: String = "other",

    @JsonProperty("target")
    val target: String = "other",

    @JsonProperty("equipment")
    val equipment: String? = null,

    @JsonProperty("gifUrl")
    val gifUrl: String? = null,

    @JsonProperty("instructions")
    val instructions: List<String>? = null,
)

// 운동 디테일 정보 표시 DTO
data class ExerciseDetailDto(
    val exerciseId: Long,
    val name: String?,
    val instructions: String?,
    val bodyPart: String,
    val target: String,
    val gifUrl: String?,
    val equipment: String?
)

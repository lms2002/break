package com.example.breakApp.exercise.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ExerciseDto(
    @JsonProperty("name")
    val name: String,

    @JsonProperty("instructions")
    val instructions: List<String>?,  // 설명을 여러 줄로 받을 수 있는 리스트로 설정

    @JsonProperty("bodyPart")
    val category: String,

    @JsonProperty("target")
    val targetArea: String
)
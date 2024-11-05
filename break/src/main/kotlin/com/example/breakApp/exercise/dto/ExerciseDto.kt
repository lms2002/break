package com.example.breakApp.exercise.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ExerciseDto(
    @JsonProperty("name")
    val name: String,

    @JsonProperty("instructions")
    val instructions: List<String>? = null,

    @JsonProperty("bodyPart")  // 실제 API의 필드 이름이 맞는지 확인
    val category: String = "General",

    @JsonProperty("target")  // 실제 API의 필드 이름이 맞는지 확인
    val targetArea: String = "Full Body"
)
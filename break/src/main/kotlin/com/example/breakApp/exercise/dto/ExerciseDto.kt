package com.example.breakApp.exercise.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ExerciseDto(
    @JsonProperty("name")
    val name: String,  // 운동 이름

    @JsonProperty("description")
    val description: String?,  // 운동 설명 (optional)

    @JsonProperty("category")
    val category: String?,  // 운동 카테고리, nullable로 설정

    @JsonProperty("target")
    val targetArea: String  // 타겟 부위
)
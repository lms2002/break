package com.example.breakApp.exercise.dto

import com.fasterxml.jackson.annotation.JsonProperty

// API에서 받아온 운동 데이터를 담는 DTO 클래스
data class ExerciseDto(

    @JsonProperty("name")
    val name: String,  // 운동 이름

    @JsonProperty("description")
    val description: String?,  // 운동 설명 (optional)

    @JsonProperty("category")
    val category: String,  // 운동 카테고리

    @JsonProperty("target")
    val targetArea: String  // 타겟 부위
)
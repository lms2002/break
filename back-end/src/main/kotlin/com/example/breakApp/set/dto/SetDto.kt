package com.example.breakApp.set.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull

data class ExerciseSetDto(
    val setId: Long? = null, // 세트 ID (응답 시 포함, 생성 시에는 null)

    @field:NotNull(message = "Routine ID는 필수입니다.")
    val routineId: Long,  // 루틴 ID

    @field:NotNull(message = "Exercise ID는 필수입니다.")
    val exerciseId: Long,  // 운동 ID

    @field:Min(value = 1, message = "세트 번호는 1 이상이어야 합니다.")
    val setNumber: Int,  // 세트 번호

    @field:Min(value = 1, message = "반복 횟수는 1 이상이어야 합니다.")
    val repetitions: Int = 10,  // 반복 횟수

    @field:Min(value = 0, message = "중량은 0 이상이어야 합니다.")
    val weight: Float = 10.0f,  // 중량

    val isCompleted: Boolean = false  // 세트 완료 여부
)
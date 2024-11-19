package com.example.breakApp.routine.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateRoutineDto(
    @field:NotBlank(message = "루틴 이름은 빈 칸일 수 없습니다.")
    @field:Size(max = 50, message = "루틴 이름의 길이는 50자 미만입니다.")
    val name: String, // 루틴 이름
    val exercises: List<CreateRoutineExerciseDto> // 연관된 운동 리스트
)

data class CreateRoutineExerciseDto(
    val exerciseId: Long, // 운동 ID
    @field:Min(1, message = "Sets must be at least 1")
    val sets: Int,

    @field:Min(1, message = "Repetitions must be at least 1")
    val repetitions: Int,

    @field:Min(0, message = "Weight cannot be negative")
    val weight: Float,

    @field:Min(0, message = "Rest time cannot be negative")
    val restTimeSeconds: Int
)
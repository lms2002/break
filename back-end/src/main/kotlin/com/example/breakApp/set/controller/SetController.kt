package com.example.breakApp.set.controller

import com.example.breakApp.set.dto.ExerciseSetDto
import com.example.breakApp.set.service.ExerciseSetService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/exercise-sets")
class ExerciseSetController(
    private val exerciseSetService: ExerciseSetService
) {
    // 새로운 세트 생성
    @PostMapping
    fun createExerciseSet(@RequestBody @Valid exerciseSetDto: ExerciseSetDto): ResponseEntity<ExerciseSetDto> {
        val createdSet = exerciseSetService.createExerciseSet(exerciseSetDto)
        return ResponseEntity.ok(createdSet)
    }

    // 특정 루틴 및 운동에 대한 모든 세트 조회
    @GetMapping("/routine/{routineId}/exercise/{exerciseId}")
    fun getSetsByRoutineAndExercise(
        @PathVariable routineId: Long,
        @PathVariable exerciseId: Long
    ): ResponseEntity<List<ExerciseSetDto>> {
        val sets = exerciseSetService.getSetsByRoutineAndExercise(routineId, exerciseId)
        return ResponseEntity.ok(sets)
    }

    // 특정 루틴에 대한 모든 세트 조회
    @GetMapping("/routine/{routineId}")
    fun getSetsByRoutine(@PathVariable routineId: Long): ResponseEntity<List<ExerciseSetDto>> {
        val sets = exerciseSetService.getSetsByRoutine(routineId)
        return ResponseEntity.ok(sets)
    }

    // 세트 업데이트
    @PutMapping("/{setId}")
    fun updateExerciseSet(
        @PathVariable setId: Long,
        @RequestBody @Valid updatedSetDto: ExerciseSetDto
    ): ResponseEntity<ExerciseSetDto> {
        val updatedExerciseSet = exerciseSetService.updateExerciseSet(setId, updatedSetDto)
        return ResponseEntity.ok(updatedExerciseSet)
    }

    // 세트 삭제
    @DeleteMapping("/{setId}")
    fun deleteExerciseSet(@PathVariable setId: Long): ResponseEntity<Unit> {
        exerciseSetService.deleteExerciseSet(setId)
        return ResponseEntity.noContent().build()
    }
}
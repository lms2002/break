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
    @PostMapping
    fun createExerciseSet(
        @RequestBody @Valid exerciseSetDto: ExerciseSetDto,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<ExerciseSetDto> {
        val createdSet = exerciseSetService.createExerciseSet(exerciseSetDto, extractBearerToken(token))
        return ResponseEntity.ok(createdSet)
    }

    @GetMapping("/routine/{routineId}/exercise/{exerciseId}")
    fun getSetsByRoutineAndExercise(
        @PathVariable routineId: Long,
        @PathVariable exerciseId: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<List<ExerciseSetDto>> {
        val sets = exerciseSetService.getSetsByRoutineAndExercise(routineId, exerciseId, extractBearerToken(token))
        return ResponseEntity.ok(sets)
    }

    @GetMapping("/routine/{routineId}")
    fun getSetsByRoutine(
        @PathVariable routineId: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<List<ExerciseSetDto>> {
        val sets = exerciseSetService.getSetsByRoutine(routineId, extractBearerToken(token))
        return ResponseEntity.ok(sets)
    }

    @PutMapping("/{setId}")
    fun updateExerciseSet(
        @PathVariable setId: Long,
        @RequestBody @Valid updatedSetDto: ExerciseSetDto,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<ExerciseSetDto> {
        val updatedExerciseSet = exerciseSetService.updateExerciseSet(setId, updatedSetDto, extractBearerToken(token))
        return ResponseEntity.ok(updatedExerciseSet)
    }

    @DeleteMapping("/{setId}")
    fun deleteExerciseSet(
        @PathVariable setId: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Unit> {
        exerciseSetService.deleteExerciseSet(setId, extractBearerToken(token))
        return ResponseEntity.noContent().build()
    }

    private fun extractBearerToken(header: String): String {
        if (header.startsWith("Bearer ")) {
            return header.removePrefix("Bearer ").trim()
        }
        throw IllegalArgumentException("Invalid Authorization header format")
    }
}
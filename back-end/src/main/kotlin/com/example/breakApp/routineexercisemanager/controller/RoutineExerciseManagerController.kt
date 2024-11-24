package com.example.breakApp.routineexercisemanager.controller

import com.example.breakApp.exercise.dto.ExerciseDetailDto
import com.example.breakApp.routineexercisemanager.dto.CreateRoutineExerciseRequest
import com.example.breakApp.routineexercisemanager.service.RoutineExerciseManagerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/routine-exercise")
class RoutineExerciseManagerController(
    private val routineExerciseManagerService: RoutineExerciseManagerService
) {

    // 루틴에 여러 운동 추가
    @PostMapping
    fun addExercisesToRoutine(
        @RequestBody addExercisesToRoutineDto: CreateRoutineExerciseRequest,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Unit> {
        val jwtToken = extractBearerToken(token)
        println("Extracted Bearer Token: $jwtToken")
        println("Routine ID from request: ${addExercisesToRoutineDto.routineId}") // 추가 로그

        routineExerciseManagerService.addExercisesToRoutine(addExercisesToRoutineDto, jwtToken)
        return ResponseEntity.ok().build()
    }

    // 루틴의 모든 운동 조회
    @GetMapping("/{routineId}")
    fun getExercisesByRoutine(
        @PathVariable routineId: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<List<ExerciseDetailDto>> {
        val jwtToken = extractBearerToken(token)
        val exercises = routineExerciseManagerService.getExercisesByRoutine(routineId, jwtToken)
        return ResponseEntity.ok(exercises)
    }


    // 루틴에서 특정 운동 삭제
    @DeleteMapping("/{routineId}/exercise/{exerciseId}")
    fun removeExerciseFromRoutine(
        @PathVariable routineId: Long,
        @PathVariable exerciseId: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Unit> {
        val jwtToken = extractBearerToken(token)
        routineExerciseManagerService.removeExerciseFromRoutine(routineId, exerciseId, jwtToken)
        return ResponseEntity.noContent().build()
    }

    // Authorization 헤더에서 Bearer 접두사 제거
    private fun extractBearerToken(header: String): String {
        if (header.startsWith("Bearer ")) {
            return header.removePrefix("Bearer ").trim()
        }
        throw IllegalArgumentException("Invalid Authorization header format")
    }
}

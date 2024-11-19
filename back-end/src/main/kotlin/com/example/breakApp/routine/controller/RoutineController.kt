package com.example.breakApp.routine.controller

import com.example.breakApp.routine.dto.CreateRoutineDto
import com.example.breakApp.routine.dto.RoutineResponseDto
import com.example.breakApp.routine.service.RoutineService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/routines")
class RoutineController(
    private val routineService: RoutineService
) {

    // 새로운 루틴 생성
    @PostMapping
    fun createRoutine(
        @RequestBody @Valid createRoutineDto: CreateRoutineDto,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<RoutineResponseDto> {
        val jwtToken = extractBearerToken(token)
        val createdRoutine = routineService.createRoutine(createRoutineDto, jwtToken)
        return ResponseEntity.ok(createdRoutine)
    }

    // 사용자의 모든 루틴 조회
    @GetMapping
    fun getRoutineList(@RequestHeader("Authorization") token: String): ResponseEntity<List<RoutineResponseDto>> {
        val jwtToken = extractBearerToken(token)
        val routines = routineService.getRoutineList(jwtToken)
        return ResponseEntity.ok(routines)
    }

    // 특정 루틴 조회
    @GetMapping("/{routineId}")
    fun getRoutineById(
        @PathVariable routineId: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<RoutineResponseDto> {
        val jwtToken = extractBearerToken(token)
        val routine = routineService.getRoutineById(routineId, jwtToken)
        return ResponseEntity.ok(routine)
    }

    // 루틴 이름 및 운동 목록 업데이트 (운동 갱신 기능 포함)
    @PutMapping("/{routineId}")
    fun updateRoutine(
        @PathVariable routineId: Long,
        @RequestBody updateDto: CreateRoutineDto,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<RoutineResponseDto> {
        val jwtToken = extractBearerToken(token)
        val updatedRoutine = routineService.updateRoutineExercises(routineId, updateDto, jwtToken)
        return ResponseEntity.ok(updatedRoutine)
    }

    // 루틴 삭제
    @DeleteMapping("/{routineId}")
    fun deleteRoutine(
        @PathVariable routineId: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<String> {
        val jwtToken = extractBearerToken(token)

        // 삭제 전에 루틴 이름 조회
        val routineName = routineService.getRoutineById(routineId, jwtToken).name

        // 루틴 삭제
        routineService.deleteRoutine(routineId, jwtToken)

        return ResponseEntity.ok("\"$routineName\" 루틴이 삭제되었습니다.")
    }

    // Authorization 헤더에서 Bearer 접두사 제거
    private fun extractBearerToken(header: String): String {
        if (header.startsWith("Bearer ")) {
            return header.removePrefix("Bearer ").trim()
        }
        throw IllegalArgumentException("Invalid Authorization header format")
    }
}
package com.example.breakApp.routine.controller

import com.example.breakApp.routine.dto.RoutineDto
import com.example.breakApp.routine.service.RoutineService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/routines")
class RoutineController(
    private val routineService: RoutineService
) {

    // 새로운 루틴 생성
    @PostMapping
    fun createRoutine(
        @RequestBody @Valid routineDto: RoutineDto,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<RoutineDto> {
        val createdRoutine = routineService.createRoutine(routineDto, extractBearerToken(token))
        return ResponseEntity.ok(createdRoutine)
    }

    // 사용자의 모든 루틴 조회
    @GetMapping
    fun getRoutineList(@RequestHeader("Authorization") token: String): ResponseEntity<List<RoutineDto>> {
        val routines = routineService.getRoutineList(extractBearerToken(token))
        return ResponseEntity.ok(routines)
    }

    // 특정 루틴 조회
    @GetMapping("/{routineId}")
    fun getRoutineById(
        @PathVariable routineId: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<RoutineDto> {
        val routine = routineService.getRoutineById(routineId, extractBearerToken(token))
        return ResponseEntity.ok(routine)
    }

    // 루틴 업데이트
    @PutMapping("/{routineId}")
    fun updateRoutine(
        @PathVariable routineId: Long,
        @RequestBody @Valid updatedRoutineDto: RoutineDto,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<RoutineDto> {
        val updatedRoutine = routineService.updateRoutine(routineId, updatedRoutineDto, extractBearerToken(token))
        return ResponseEntity.ok(updatedRoutine)
    }

    // 루틴 삭제
    @DeleteMapping("/{routineId}")
    fun deleteRoutine(
        @PathVariable routineId: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Unit> {
        routineService.deleteRoutine(routineId, extractBearerToken(token))
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
package com.example.breakApp.routine.controller

import com.example.breakApp.routine.dto.CreateRoutineDto
import com.example.breakApp.routine.dto.RoutineResponseDto
import com.example.breakApp.routine.service.RoutineService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/routines")
class RoutineController(
    private val routineService: RoutineService
) {

    /**
     * 사용자의 모든 루틴 조회
     * @param token 사용자 인증 토큰 (Authorization 헤더)
     * @return 루틴 리스트
     */
    @GetMapping
    fun getRoutineList(@RequestHeader("Authorization") token: String): ResponseEntity<List<RoutineResponseDto>> {
        val jwtToken = extractBearerToken(token)
        val routines = routineService.getRoutineList(jwtToken)
        return ResponseEntity.ok(routines)
    }

    /**
     * 특정 루틴 조회
     * @param routineId 조회할 루틴 ID
     * @param token 사용자 인증 토큰 (Authorization 헤더)
     * @return 조회된 루틴 데이터
     */
    @GetMapping("/{routineId}")
    fun getRoutineById(
        @PathVariable routineId: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<RoutineResponseDto> {
        val jwtToken = extractBearerToken(token) // Bearer 접두사 제거
        val routine = routineService.getRoutineById(routineId, jwtToken)
        return ResponseEntity.ok(routine)
    }


    /**
     * 새로운 루틴 생성
     * @param createRoutineDto 루틴 생성 요청 DTO
     * @param token 사용자 인증 토큰 (Authorization 헤더)
     * @return 생성된 루틴
     */
    @PostMapping
    fun createRoutine(
        @RequestBody createRoutineDto: CreateRoutineDto,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<RoutineResponseDto> {
        val jwtToken = extractBearerToken(token)
        val createdRoutine = routineService.createRoutine(createRoutineDto, jwtToken)
        return ResponseEntity.ok(createdRoutine)
    }

    /**
     * 루틴 업데이트
     * @param routineId 업데이트할 루틴 ID
     * @param updateDto 루틴 업데이트 요청 DTO
     * @param token 사용자 인증 토큰 (Authorization 헤더)
     * @return 업데이트된 루틴
     */
    @PutMapping("/{routineId}")
    fun updateRoutine(
        @PathVariable routineId: Long,
        @RequestBody updateDto: CreateRoutineDto,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<RoutineResponseDto> {
        val jwtToken = extractBearerToken(token)
        val updatedRoutine = routineService.updateRoutine(routineId, updateDto, jwtToken)
        return ResponseEntity.ok(updatedRoutine)
    }

    /**
     * 루틴 삭제
     * @param routineId 삭제할 루틴 ID
     * @param token 사용자 인증 토큰 (Authorization 헤더)
     */
    @DeleteMapping("/{routineId}")
    fun deleteRoutine(
        @PathVariable routineId: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<Void> {
        val jwtToken = extractBearerToken(token)
        routineService.deleteRoutine(routineId, jwtToken)
        return ResponseEntity.noContent().build()
    }

    /**
     * Authorization 헤더에서 Bearer 접두사를 제거하고 JWT 토큰만 반환
     * @param header Authorization 헤더 값
     * @return JWT 토큰
     * @throws IllegalArgumentException 잘못된 형식의 Authorization 헤더인 경우 예외 발생
     */
    private fun extractBearerToken(header: String): String {
        if (header.startsWith("Bearer ")) {
            return header.removePrefix("Bearer ").trim()
        }
        throw IllegalArgumentException("Invalid Authorization header format")
    }
}
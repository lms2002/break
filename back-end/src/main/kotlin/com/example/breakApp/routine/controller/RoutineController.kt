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
        val routines = routineService.getRoutineList(token)
        return ResponseEntity.ok(routines)
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
        val createdRoutine = routineService.createRoutine(createRoutineDto, token)
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
        val updatedRoutine = routineService.updateRoutine(routineId, updateDto, token)
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
        routineService.deleteRoutine(routineId, token)
        return ResponseEntity.noContent().build()
    }
}
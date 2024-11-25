package com.example.breakApp.workoutlog.controller

import com.example.breakApp.workoutlog.dto.WorkoutLogDto
import com.example.breakApp.workoutlog.service.WorkoutLogService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/workout-logs")
class WorkoutLogController(
    private val workoutLogService: WorkoutLogService
) {

    // 특정 루틴의 모든 운동 세트 로그 조회
    @GetMapping("/routine/{routineId}")
    fun getWorkoutLogsForRoutine(
        @PathVariable routineId: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<List<WorkoutLogDto>> {
        val jwtToken = extractBearerToken(token)
        val workoutLogs = workoutLogService.getWorkoutLogsForRoutine(routineId, jwtToken)
        return ResponseEntity.ok(workoutLogs)
    }

    // Authorization 헤더에서 Bearer 접두사 제거
    private fun extractBearerToken(header: String): String {
        if (header.startsWith("Bearer ")) {
            return header.removePrefix("Bearer ").trim()
        }
        throw IllegalArgumentException("Invalid Authorization header format")
    }
}
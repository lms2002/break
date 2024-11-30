package com.example.breakApp.workoutlog.controller

import com.example.breakApp.workoutlog.dto.CompletedWorkoutDto
import com.example.breakApp.workoutlog.dto.StartWorkoutRequest
import com.example.breakApp.workoutlog.dto.WorkoutLogDto
import com.example.breakApp.workoutlog.service.WorkoutLogService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/workout-logs")
class WorkoutLogController(
    private val workoutLogService: WorkoutLogService
) {

    // 운동 시작
    @PostMapping("/start")
    fun startWorkout(
        @RequestBody request: StartWorkoutRequest,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<WorkoutLogDto> {
        val extractedToken = extractBearerToken(token)
        val workoutLog = workoutLogService.startWorkout(request.routineId, extractedToken)
        return ResponseEntity.ok(workoutLog)
    }

    // 운동 종료
    @PostMapping("/{logId}/end")
    fun endWorkout(
        @PathVariable logId: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<CompletedWorkoutDto> {
        val extractedToken = extractBearerToken(token)
        val completedWorkout = workoutLogService.endWorkout(logId, extractedToken)
        return ResponseEntity.ok(completedWorkout)
    }

    // 전체 WorkoutLogDto 조회
    @GetMapping
    fun getAllWorkoutLogs(
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<List<WorkoutLogDto>> {
        val extractedToken = extractBearerToken(token)
        val workoutLogs = workoutLogService.getAllWorkoutLogs(extractedToken)
        return ResponseEntity.ok(workoutLogs)
    }

    // 완료된 운동 조회
    @GetMapping("/completed")
    fun getCompletedWorkouts(
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<List<CompletedWorkoutDto>> {
        val extractedToken = extractBearerToken(token)
        val completedWorkouts = workoutLogService.getCompletedWorkouts(extractedToken)
        return ResponseEntity.ok(completedWorkouts)
    }

    // Bearer 토큰 추출 메서드
    private fun extractBearerToken(header: String): String {
        if (header.startsWith("Bearer ")) {
            return header.removePrefix("Bearer ").trim()
        }
        throw IllegalArgumentException("Invalid Authorization header format")
    }
}
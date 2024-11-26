package com.example.breakApp.workoutlog.service

import com.example.breakApp.workoutlog.dto.WorkoutLogDto
import com.example.breakApp.workoutlog.entity.WorkoutLog
import com.example.breakApp.workoutlog.repository.WorkoutLogRepository
import com.example.breakApp.common.authority.JwtTokenProvider
import org.springframework.stereotype.Service

@Service
class WorkoutLogService(
    private val workoutLogRepository: WorkoutLogRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {

    fun getWorkoutLogsForRoutine(routineId: Long, token: String): List<WorkoutLogDto> {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val workoutLogs = workoutLogRepository.findAllByRoutineRoutineId(routineId)

        return workoutLogs.filter { it.member.userId == userId }
            .map {
                WorkoutLogDto(
                    logId = it.logId!!,
                    exerciseId = it.exercise.exerciseId!!,
                    routineId = it.routine.routineId!!,
                    setId = it.setId.setId!!,
                    repetitions = it.repetitions,
                    weight = it.weight,
                    isCompleted = it.isCompleted,
                    startTime = it.startTime,
                    endTime = it.endTime,
                    duration = it.duration
                )
            }
    }
}
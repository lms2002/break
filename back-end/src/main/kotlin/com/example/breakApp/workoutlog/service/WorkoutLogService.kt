package com.example.breakApp.workoutlog.service

import com.example.breakApp.common.authority.JwtTokenProvider
import com.example.breakApp.notification.service.NotificationService
import com.example.breakApp.routine.repository.RoutineRepository
import com.example.breakApp.set.entity.toDto
import com.example.breakApp.set.repository.ExerciseSetRepository
import com.example.breakApp.workoutlog.dto.CompletedExerciseDto
import com.example.breakApp.workoutlog.dto.CompletedWorkoutDto
import com.example.breakApp.workoutlog.dto.WorkoutLogDto
import com.example.breakApp.workoutlog.entity.WorkoutLog
import com.example.breakApp.workoutlog.repository.WorkoutLogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import com.example.breakApp.workoutlog.entity.toDto



@Service
class WorkoutLogService(
    private val workoutLogRepository: WorkoutLogRepository,
    private val routineRepository: RoutineRepository,
    private val exerciseSetRepository: ExerciseSetRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val notificationService: NotificationService
) {

    @Transactional
    fun startWorkout(routineId: Long, token: String): WorkoutLogDto {
        val userId = jwtTokenProvider.getUserIdFromToken(token)

        val routine = routineRepository.findById(routineId).orElseThrow {
            throw RuntimeException("Routine not found")
        }

        if (routine.member.userId != userId) {
            throw RuntimeException("User not authorized to start this routine")
        }

        val workoutLog = WorkoutLog(
            member = routine.member,
            routine = routine,
            startTime = LocalDateTime.now() // 운동 시작 시간 기록
        )

        val savedLog = workoutLogRepository.save(workoutLog)
        return savedLog.toDto()
    }

    @Transactional
    fun endWorkout(logId: Long, token: String): CompletedWorkoutDto {
        val userId = jwtTokenProvider.getUserIdFromToken(token)

        val workoutLog = workoutLogRepository.findById(logId).orElseThrow {
            throw RuntimeException("Workout log with ID $logId not found")
        }

        if (workoutLog.member.userId != userId) {
            throw RuntimeException("User not authorized to end this workout")
        }

        workoutLog.endTime = LocalDateTime.now()
        workoutLogRepository.save(workoutLog)

        val completedSets = exerciseSetRepository.findByRoutine(workoutLog.routine)
            .filter { it.isCompleted }

        val completedExercises = completedSets.groupBy { it.exercise.exerciseId }
            .map { (exerciseId, sets) ->
                CompletedExerciseDto(
                    exerciseId = exerciseId!!,
                    sets = sets.map { it.toDto() }
                )
            }

        // 루틴 완료 알림 생성
        notificationService.createNotification(
            userId = userId,
            message = "잘했어요! 오늘 '${workoutLog.routine.name}' 루틴을 완료했습니다! 꼭 수분 섭취를 해주세요!",
            type = "루틴 완료"
        )

        return CompletedWorkoutDto(
            routineId = workoutLog.routine.routineId!!,
            completedExercises = completedExercises
        )
    }

    // 전체 WorkoutLog 조회
    @Transactional(readOnly = true)
    fun getAllWorkoutLogs(token: String): List<WorkoutLogDto> {
        val userId = jwtTokenProvider.getUserIdFromToken(token)

        // 해당 사용자의 모든 WorkoutLog 조회
        val workoutLogs = workoutLogRepository.findByMemberUserId(userId)

        // WorkoutLog를 WorkoutLogDto로 변환하여 반환
        return workoutLogs.map { it.toDto() }
    }
    
    // 완료된 운동 조회
    fun getCompletedWorkouts(token: String): List<CompletedWorkoutDto> {
        val userId = jwtTokenProvider.getUserIdFromToken(token)

        val workoutLogs = workoutLogRepository.findByMemberUserId(userId)
            .filter { it.endTime != null }

        return workoutLogs.map { log ->
            val completedSets = exerciseSetRepository.findByRoutine(log.routine)
                .filter { it.isCompleted }

            val completedExercises = completedSets.groupBy { it.exercise.exerciseId }
                .map { (exerciseId, sets) ->
                    CompletedExerciseDto(
                        exerciseId = exerciseId!!,
                        sets = sets.map { it.toDto() }
                    )
                }

            CompletedWorkoutDto(
                routineId = log.routine.routineId!!,
                completedExercises = completedExercises
            )
        }
    }
}
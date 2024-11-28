package com.example.breakApp.notification.service

import com.example.breakApp.routine.repository.RoutineRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ReminderService(
    private val routineRepository: RoutineRepository,
    private val notificationService: NotificationService
) {
    // 운동 리마인더 함수
    @Scheduled(cron = "0 0 8 * * ?") // 매일 오전 8시에 실행
    fun sendRoutineReminders() {
        val now = LocalDateTime.now()
        val unstartedRoutines = routineRepository.findUnstartedRoutines(now.minusDays(1)) // 어제 기준 미완료 루틴

        unstartedRoutines.forEach { routine ->
            notificationService.createNotification(
                userId = routine.member.userId!!,
                message = "오늘의 운동을 잊지 마세요. '${routine.name}'를 지금 시작하세요!",
                type = "운동 리마인더"
            )
        }
    }
}

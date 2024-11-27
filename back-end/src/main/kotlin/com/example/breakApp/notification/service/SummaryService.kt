package com.example.breakApp.notification.service

import com.example.breakApp.common.authority.JwtTokenProvider
import com.example.breakApp.member.repository.MemberRepository
import com.example.breakApp.workoutlog.repository.WorkoutLogRepository
import jakarta.transaction.Transactional
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDateTime

@Service
class SummaryService(
    private val workoutLogRepository: WorkoutLogRepository,
    private val notificationService: NotificationService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberRepository: MemberRepository

) {
    /**
     * 주간 운동 요약 알림을 생성하는 함수.
     * 1. 사용자 정보에서 첫 운동 날짜를 조회하여 운동을 시작한 적이 없으면 운동 시작 유도 알림을 보냄.
     * 2. 첫 운동이 있다면, 지난 주 동안 완료한 루틴 개수를 조회하여 알림 메시지를 생성.
     * 3. 완료된 루틴 개수에 따라 "잘하고 있어요!" 또는 "다음 주에는 더 열심히 해봐요!"라는 메시지를 전송.
     */
    @Transactional
    fun sendWeeklySummary(token: String) {
        val userId = jwtTokenProvider.getUserIdFromToken(token)

        // 첫 운동을 시작한 날짜 조회 (운동 시작 준비 여부 확인)
        val member = memberRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val firstWorkoutDate = workoutLogRepository.findFirstWorkoutDate(member)

        if (firstWorkoutDate == null) {
            // 첫 운동이 없으면 "운동을 시작하세요!" 알림을 보냄
            notificationService.createNotification(
                userId = userId,
                message = "운동을 시작할 준비가 되었나요? 루틴을 등록하고 운동을 시작하세요!",
                type = "운동 리마인더"
            )
            return
        }

        // 첫 운동부터 주간 요약 알림을 보냄
        // 지난 주 시작 날짜 계산
        val weekStart = firstWorkoutDate.with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay()

        // 완료된 루틴 개수 계산
        val completedCount = workoutLogRepository.countCompletedWorkoutsForUser(userId, weekStart)

        // 알림 메시지 생성
        val message = if (completedCount > 0) {
            "이번 주 $completedCount 개의 루틴을 완료했습니다. 잘하고 있어요!"
        } else {
            "이번 주에 완료된 루틴이 없습니다. 다음 주에는 더 열심히 해봐요!"
        }

        // 알림 생성
        notificationService.createNotification(
            userId = userId,
            message = message,
            type = "주간 요약"
        )
    }
}
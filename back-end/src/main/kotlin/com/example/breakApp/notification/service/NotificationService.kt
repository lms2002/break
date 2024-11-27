package com.example.breakApp.notification.service

import com.example.breakApp.common.authority.JwtTokenProvider
import com.example.breakApp.member.repository.MemberRepository
import com.example.breakApp.notification.dto.NotificationDto
import com.example.breakApp.notification.entity.Notification
import com.example.breakApp.notification.entity.toDto
import com.example.breakApp.notification.repository.NotificationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class NotificationService(
    private val notificationRepository: NotificationRepository,
    private val memberRepository: MemberRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {

    // 알림 조회
    fun getNotifications(token: String): List<NotificationDto> {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val member = memberRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        // 삭제되지 않은 알림 조회
        return notificationRepository.findByMemberAndDeletedAtIsNull(member).map { it.toDto() }
    }

    // 알림 읽음 처리
    @Transactional
    fun markAsRead(notificationId: Long, token: String) {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val member = memberRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val notification = notificationRepository.findById(notificationId)
            .orElseThrow { RuntimeException("Notification not found") }

        if (notification.member != member) {
            throw RuntimeException("User not authorized to modify this notification")
        }

        notification.isRead = true
        notificationRepository.save(notification)
    }

    // 알림 생성
    @Transactional
    fun createNotification(userId: Long, message: String, type: String, expirationDate: LocalDateTime? = null) {
        val member = memberRepository.findById(userId).orElseThrow {
            RuntimeException("User with ID $userId not found")
        }

        val notification = Notification(
            member = member,
            message = message,
            type = type,
            createdAt = LocalDateTime.now(),
        )

        notificationRepository.save(notification)
    }

    // 알림 삭제
    @Transactional
    fun deleteNotification(notificationId: Long, token: String) {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val notification = notificationRepository.findById(notificationId).orElseThrow {
            RuntimeException("Notification not found")
        }

        // 사용자가 삭제할 알림에 대한 권한이 있는지 확인
        if (notification.member.userId != userId) {
            throw RuntimeException("User not authorized to delete this notification")
        }

        // 삭제된 시간 설정
        notification.deletedAt = LocalDateTime.now()

        // 알림 저장 (삭제된 것으로 표시)
        notificationRepository.save(notification)
    }
}
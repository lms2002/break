package com.example.breakApp.notification.dto

import java.time.LocalDateTime

data class NotificationDto(
    val notificationId: Long, // 알림 ID
    val message: String, // 알림 메시지
    val type: String, // 알림 유형
    val isRead: Boolean, // 읽음 여부
    val createdAt: LocalDateTime, // 생성 시간
    val deletedAt: LocalDateTime? // 삭제된 시간
)
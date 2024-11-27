package com.example.breakApp.notification.entity

import com.example.breakApp.member.entity.Member
import com.example.breakApp.notification.dto.NotificationDto
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "Notification")
data class Notification(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    val notificationId: Long = 0, // 알림 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val member: Member, // 사용자 (외래 키)

    @Column(name = "message", nullable = false)
    val message: String, // 알림 메시지

    @Column(name = "type", nullable = false)
    val type: String, // 알림 유형 (예: "루틴 완료", "운동 리마인더", "진행 상황")

    @Column(name = "is_read", nullable = false)
    var isRead: Boolean = false, // 읽음 여부

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(), // 생성 시간

    @Column(name = "deleted_at")
    var deletedAt: LocalDateTime? = null // 삭제된 시간
)
fun Notification.toDto(): NotificationDto {
    return NotificationDto(
        notificationId = this.notificationId,
        message = this.message,
        type = this.type,
        isRead = this.isRead,
        createdAt = this.createdAt,
        deletedAt = this.deletedAt
    )
}
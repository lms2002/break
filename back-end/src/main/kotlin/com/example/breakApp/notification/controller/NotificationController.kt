package com.example.breakApp.notification.controller

import com.example.breakApp.common.authority.JwtTokenProvider
import com.example.breakApp.notification.dto.NotificationDto
import com.example.breakApp.notification.service.NotificationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/notifications")
class NotificationController(
    private val notificationService: NotificationService,
    private val jwtTokenProvider: JwtTokenProvider
) {
    // 알림 조회
    @GetMapping
    fun getNotifications(@RequestHeader("Authorization") token: String): ResponseEntity<List<NotificationDto>> {
        val notifications = notificationService.getNotifications(extractBearerToken(token))
        return ResponseEntity.ok(notifications)
    }

    // 알림 읽음 처리
    @PutMapping("/{notificationId}/read")
    fun markAsRead(
        @RequestHeader("Authorization") token: String,
        @PathVariable notificationId: Long
    ): ResponseEntity<Unit> {
        notificationService.markAsRead(notificationId, extractBearerToken(token))
        return ResponseEntity.ok().build()
    }

    // 알림 생성
    @PostMapping("/create")
    fun createNotification(
        @RequestHeader("Authorization") token: String,
        @RequestParam message: String,
        @RequestParam type: String,
        @RequestParam expirationDate: String?
    ): ResponseEntity<Unit> {
        val userId = jwtTokenProvider.getUserIdFromToken(extractBearerToken(token))
        val expiration = expirationDate?.let { LocalDateTime.parse(it) }
        notificationService.createNotification(userId, message, type, expiration) // expiration 전달
        return ResponseEntity.ok().build()
    }

    // 알림 삭제
    @DeleteMapping("/{notificationId}")
    fun deleteNotification(
        @RequestHeader("Authorization") token: String,
        @PathVariable notificationId: Long
    ): ResponseEntity<Unit> {
        notificationService.deleteNotification(notificationId, extractBearerToken(token))
        return ResponseEntity.noContent().build()
    }

    private fun extractBearerToken(header: String): String {
        if (header.startsWith("Bearer ")) {
            return header.removePrefix("Bearer ").trim()
        }
        throw IllegalArgumentException("Invalid Authorization header format")
    }
}
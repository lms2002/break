package com.example.breakApp.notification.repository

import com.example.breakApp.notification.entity.Notification
import com.example.breakApp.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NotificationRepository : JpaRepository<Notification, Long> {
    // 특정 사용자의 알림 조회 (삭제된 알림도 포함)
    fun findByMember(member: Member): List<Notification>

    // 삭제되지 않은 알림만 조회
    fun findByMemberAndDeletedAtIsNull(member: Member): List<Notification>

    // 특정 사용자의 읽지 않은 알림 조회
    fun findByMemberAndIsReadFalse(member: Member): List<Notification>

}
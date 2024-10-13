package com.project.break_app.repository

import com.project.break_app.domain.User
import org.springframework.data.jpa.repository.JpaRepository

/**
 * User 엔티티와 상호작용하는 리포지토리 인터페이스
 * 사용자명(userName)을 기반으로 사용자 정보를 조회하는 메소드를 정의
 */
interface UserRepository : JpaRepository<User, Long> {
    // 사용자명(userName)으로 사용자 정보 조회
    fun findByUserName(userName: String): User?
}

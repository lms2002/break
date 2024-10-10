package com.project.break_app.repository

import com.project.break_app.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    // 사용자명으로 사용자 정보를 조회하는 메소드
    fun findByUsername(username: String): User
}

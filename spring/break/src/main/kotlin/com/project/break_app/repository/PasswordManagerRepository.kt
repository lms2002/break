package com.project.break_app.repository

import com.project.break_app.domain.PasswordManager
import org.springframework.data.jpa.repository.JpaRepository

// PasswordManager 엔티티에 대한 JpaRepository를 정의
interface PasswordManagerRepository : JpaRepository<PasswordManager, Long>

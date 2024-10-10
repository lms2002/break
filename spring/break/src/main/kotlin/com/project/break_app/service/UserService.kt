package com.project.break_app.service

import com.project.break_app.domain.User
import com.project.break_app.domain.PasswordManager
import com.project.break_app.repository.UserRepository
import com.project.break_app.repository.PasswordManagerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import com.project.break_app.domain.UserProfile


@Service
class UserService(
    @Autowired private val userRepository: UserRepository, // UserRepository 주입
    @Autowired private val passwordManagerRepository: PasswordManagerRepository, // PasswordManagerRepository 주입
    @Autowired private val passwordEncoder: BCryptPasswordEncoder // BCryptPasswordEncoder 주입
) {

    /**
     * 회원가입 처리 메소드
     * - 사용자의 사용자명(username) 중복 체크
     * - 사용자 정보를 User 테이블에 저장
     * - 비밀번호를 해싱한 후 PasswordManager 테이블에 저장
     */
    fun registerUser(user: User, password: String): User {
        // 1. 사용자명 중복 체크 (username으로 체크)
        if (userRepository.findByUsername(user.name) != null) {
            // 동일한 사용자명이 존재하면 예외를 던짐
            throw IllegalArgumentException("Username already exists!")
        }

        // 2. 사용자 정보 저장 (User 테이블에)
        val savedUser = userRepository.save(user)

        // 3. 비밀번호 해싱 후 저장 (PasswordManager 테이블에)
        val hashedPassword = passwordEncoder.encode(password) // BCrypt로 비밀번호 해싱
        val passwordManager = PasswordManager(userId = savedUser.userId, password = hashedPassword)
        passwordManagerRepository.save(passwordManager) // 비밀번호 저장

        // 4. 최종 저장된 사용자 객체 반환
        return savedUser
    }

    /**
     * 사용자명(username)을 기반으로 사용자를 조회하는 메소드
     * - UserRepository를 통해 username으로 사용자 조회
     */
    fun getUserByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    /**
     * 사용자 ID(userId)를 기반으로 PasswordManager 테이블에서 비밀번호를 조회하는 메소드
     */
    fun getPasswordManagerByUserId(userId: Long): PasswordManager? {
        return passwordManagerRepository.findById(userId).orElse(null)
    }
    fun getUserProfileByUsername(username: String): UserProfile {
        val user = userRepository.findByUsername(username) // UserRepository에서 사용자 정보를 조회
        return UserProfile(
            username = user.username,
            email = user.email,
            age = user.age,
            gender = user.gender
        )
    }
}

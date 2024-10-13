package com.project.break_app.service

import com.project.break_app.domain.User
import com.project.break_app.domain.PasswordManager
import com.project.break_app.repository.UserRepository
import com.project.break_app.repository.PasswordManagerRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import com.project.break_app.domain.UserProfile

/**
 * 사용자 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
class UserService(
    private val userRepository: UserRepository,  // UserRepository를 통한 데이터베이스 상호작용
    private val passwordManagerRepository: PasswordManagerRepository,  // PasswordManagerRepository를 통한 비밀번호 관리
    private val passwordEncoder: BCryptPasswordEncoder  // 비밀번호 암호화를 위한 BCryptPasswordEncoder
) {

    /**
     * 사용자 회원가입 처리 메소드
     * - 사용자명이 중복되지 않으면, 사용자 정보를 저장하고 비밀번호는 해싱하여 PasswordManager에 저장
     */
    fun registerUser(user: User, password: String): User {
        // 사용자명 중복 체크
        if (userRepository.findByUserName(user.userName) != null) {
            throw IllegalArgumentException("Username already exists!")
        }

        // 사용자 정보 저장
        val savedUser = userRepository.save(user)

        // 비밀번호 해싱 후 PasswordManager에 저장
        val hashedPassword = passwordEncoder.encode(password)
        val passwordManager = PasswordManager(userId = savedUser.userId, password = hashedPassword)
        passwordManagerRepository.save(passwordManager)

        return savedUser
    }

    /**
     * 사용자명을 기반으로 사용자 정보를 조회하는 메소드
     * - userName으로 User 엔티티를 조회
     */
    fun getUserByUserName(userName: String): User? {
        return userRepository.findByUserName(userName)
    }

    fun getPasswordManagerByUserId(userId: Long): PasswordManager? {
        return passwordManagerRepository.findById(userId).orElse(null)
    }

    /**
     * 사용자 프로필 조회 메소드
     * - userName을 기반으로 사용자 정보를 조회하여 UserProfile 객체로 반환
     */
    fun getUserProfileByUserName(userName: String): UserProfile? {
        // 사용자 정보 조회
        val user = userRepository.findByUserName(userName) ?: throw IllegalArgumentException("User not found")

        // UserProfile 객체로 변환하여 반환
        return UserProfile(
            userName = user.userName,  // userName 필드 사용
            email = user.email,
            age = user.age,
            gender = user.gender.toString()  // Gender enum을 문자열로 변환
        )
    }
}

package com.project.break_app.service

import com.project.break_app.domain.User
import com.project.break_app.domain.PasswordManager
import com.project.break_app.domain.UserProfile
import com.project.break_app.repository.UserRepository
import com.project.break_app.repository.PasswordManagerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val passwordManagerRepository: PasswordManagerRepository
) {

    /**
     * 사용자 정보를 데이터베이스에 저장하는 메소드
     * - 해싱된 비밀번호는 AuthController에서 처리됨
     */
    fun registerUser(user: User): User {
        return userRepository.save(user)  // User 정보 저장
    }

    /**
     * 해싱된 비밀번호를 PasswordManager 테이블에 저장하는 메소드
     */
    fun savePassword(passwordManager: PasswordManager) {
        passwordManagerRepository.save(passwordManager)  // PasswordManager에 비밀번호 저장
    }

    /**
     * 사용자명으로 사용자 조회 메소드
     */
    fun getUserByUserName(userName: String): User? {
        return userRepository.findByUserName(userName)  // 사용자명으로 사용자 정보 조회
    }

    /**
     * 사용자 ID로 비밀번호 조회 메소드
     */
    fun getPasswordManagerByUserId(userId: Int): PasswordManager? {
        return passwordManagerRepository.findById(userId).orElse(null)  // 사용자 ID로 비밀번호 정보 조회
    }
    /**
     * 사용자명으로 유저 프로필 정보 조회 메소드
     */
    fun getUserProfileByUserName(userName: String): UserProfile {
        val user = userRepository.findByUserName(userName)
            ?: throw IllegalArgumentException("User not found")

        return UserProfile(
            userName = user.userName,
            email = user.email,
            age = user.age,
            gender = user.gender.toString()
        )
    }
}

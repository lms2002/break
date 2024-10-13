package com.project.break_app.controller

import com.project.break_app.domain.Gender
import com.project.break_app.domain.User
import com.project.break_app.domain.PasswordManager
import com.project.break_app.dto.RegisterUserDTO
import com.project.break_app.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,
    private val passwordEncoder: BCryptPasswordEncoder
) {

    /**
     * 회원가입 요청 처리
     */
    @PostMapping("/register")
    fun registerUser(@RequestBody registerUserDTO: RegisterUserDTO): ResponseEntity<User> {
        // 1. User 객체 생성
        val user = User(
            userName = registerUserDTO.userName,
            email = registerUserDTO.email,
            age = registerUserDTO.age,
            gender = Gender.valueOf(registerUserDTO.gender.uppercase())
        )

        // 2. User 등록
        val registeredUser = userService.registerUser(user)

        // 3. 비밀번호 해싱 후 PasswordManager에 저장
        val hashedPassword = passwordEncoder.encode(registerUserDTO.password)
        val passwordManager = PasswordManager(userId = registeredUser.userId, password = hashedPassword)
        userService.savePassword(passwordManager)

        // 4. 최종 저장된 사용자 정보 반환
        return ResponseEntity.ok(registeredUser)
    }

    /**
     * 로그인 요청 처리
     */
    @PostMapping("/login")
    fun login(@RequestParam userName: String, @RequestParam password: String): ResponseEntity<String> {
        // 1. 사용자 정보 조회
        val user = userService.getUserByUserName(userName)
            ?: return ResponseEntity.status(401).body("로그인 실패: 사용자명을 찾을 수 없음")

        // 2. PasswordManager에서 비밀번호 확인
        val passwordManager = userService.getPasswordManagerByUserId(user.userId)
            ?: return ResponseEntity.status(401).body("로그인 실패: 비밀번호를 찾을 수 없음")

        // 3. 비밀번호 일치 여부 확인
        return if (passwordEncoder.matches(password, passwordManager.password)) {
            ResponseEntity.ok("로그인 성공! 사용자 ID: ${user.userId}")
        } else {
            ResponseEntity.status(401).body("로그인 실패: 비밀번호가 일치하지 않음")
        }
    }
}

package com.project.break_app.controller

import com.project.break_app.domain.User
import com.project.break_app.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

/**
 * 사용자 인증 관련 API를 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,  // UserService 주입
    private val passwordEncoder: BCryptPasswordEncoder  // 비밀번호 암호화 주입
) {

    /**
     * 회원가입 요청 처리
     */
    @PostMapping("/register")
    fun registerUser(@RequestBody user: User, @RequestParam password: String): ResponseEntity<User> {
        // UserService를 사용해 회원가입 처리
        val registeredUser = userService.registerUser(user, password)
        return ResponseEntity.ok(registeredUser)
    }

    /**
     * 로그인 요청 처리
     */
    @PostMapping("/login")
    fun login(@RequestParam userName: String, @RequestParam password: String): ResponseEntity<String> {
        // 사용자 정보 조회
        val user = userService.getUserByUserName(userName)

        return if (user != null) {
            // 비밀번호 확인
            val passwordManager = userService.getPasswordManagerByUserId(user.userId)
            if (passwordManager != null && passwordEncoder.matches(password, passwordManager.password)) {
                ResponseEntity.ok("로그인 성공! 사용자 ID: ${user.userId}")
            } else {
                ResponseEntity.status(401).body("로그인 실패: 비밀번호가 일치하지 않음")
            }
        } else {
            ResponseEntity.status(401).body("로그인 실패: 사용자명을 찾을 수 없음")
        }
    }
}

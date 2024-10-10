package com.project.break_app.controller

import com.project.break_app.domain.User
import com.project.break_app.security.JwtTokenUtil
import com.project.break_app.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService,  // UserService 주입
    private val jwtTokenUtil: JwtTokenUtil, // JWT 토큰 유틸리티 주입
    private val passwordEncoder: BCryptPasswordEncoder // 비밀번호 인코더 주입
) {

    /**
     * 로그인 요청 처리
     * - username과 password를 받아서 로그인 처리
     * - 성공 시, JWT 토큰을 발급하여 반환
     * - 실패 시, 적절한 응답 반환
     */
    @PostMapping("/login")
    fun login(@RequestParam username: String, @RequestParam password: String): ResponseEntity<String> {
        // 1. 사용자명으로 사용자 정보 조회
        val user: User? = userService.getUserByUsername(username)

        return if (user != null) {
            // 2. PasswordManager에서 비밀번호를 가져옴
            val passwordManager = userService.getPasswordManagerByUserId(user.userId)

            // 3. 비밀번호 확인
            if (passwordManager != null && passwordEncoder.matches(password, passwordManager.password)) {
                // 비밀번호가 일치하는 경우 JWT 토큰 생성
                val token = jwtTokenUtil.generateToken(username) // JWT 토큰 생성
                ResponseEntity.ok("Bearer $token") // JWT 토큰을 응답으로 반환
            } else {
                // 비밀번호가 일치하지 않는 경우
                ResponseEntity.status(401).body("로그인 실패: 잘못된 비밀번호")
            }
        } else {
            // 사용자 정보가 없는 경우
            ResponseEntity.status(401).body("로그인 실패: 잘못된 사용자명")
        }
    }
}

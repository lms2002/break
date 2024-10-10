package com.project.break_app.controller

import com.project.break_app.domain.UserProfile // UserProfile 객체는 사용자 프로필 정보를 담는 클래스
import com.project.break_app.service.UserService // UserService 주입
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService // UserService 주입
) {

    /**
     * 사용자 프로필 조회 API
     * - JWT 토큰으로 인증된 사용자만 접근 가능
     * - SecurityContextHolder를 통해 현재 인증된 사용자의 정보를 가져옴
     */
    @GetMapping("/profile")
    fun getUserProfile(): ResponseEntity<UserProfile> {
        // 1. SecurityContextHolder에서 현재 인증된 사용자의 사용자명(username) 가져오기
        val username = SecurityContextHolder.getContext().authentication.principal as String

        // 2. UserService에서 사용자명으로 사용자 프로필 정보 조회
        val userProfile = userService.getUserProfileByUsername(username)

        // 3. 조회된 사용자 프로필 정보를 응답으로 반환
        return ResponseEntity.ok(userProfile)
    }
}

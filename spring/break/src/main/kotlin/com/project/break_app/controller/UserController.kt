package com.project.break_app.controller

import com.project.break_app.domain.UserProfile
import com.project.break_app.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * 사용자 관련 API를 처리하는 컨트롤러
 */
@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService  // UserService 주입
) {

    /**
     * 사용자 프로필 조회 요청 처리
     */
    @GetMapping("/profile")
    fun getUserProfile(@RequestParam userName: String): ResponseEntity<UserProfile> {
        // UserService를 통해 사용자 프로필 조회
        val userProfile = userService.getUserProfileByUserName(userName)
        return ResponseEntity.ok(userProfile)  // 조회된 프로필 반환
    }
}

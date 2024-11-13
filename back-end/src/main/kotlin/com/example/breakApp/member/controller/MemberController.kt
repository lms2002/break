package com.example.breakApp.member.controller

import com.example.breakApp.common.authority.JwtTokenProvider
import com.example.breakApp.common.authority.TokenInfo
import com.example.breakApp.common.dto.BaseResponse
import com.example.breakApp.common.dto.CustomUser
import com.example.breakApp.member.dto.*
import com.example.breakApp.member.service.MemberService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RequestMapping("/api/member")
@RestController
class MemberController(
    private val memberService: MemberService,
    private val jwtTokenProvider: JwtTokenProvider,
) {
    /**
     * 회원가입
     */
    @PostMapping("/signup")
    fun signUp(@RequestBody @Valid memberDtoRequest: MemberDtoRequest): BaseResponse<Unit> {
        val resultMsg: String = memberService.signUp(memberDtoRequest)
        return BaseResponse(message = resultMsg)
    }
    /**
     * 로그인 ID 중복 확인
     */
    @GetMapping("/check-login-id")
    fun checkLoginId(@RequestParam loginId: String): ResponseEntity<String> {
        return if (memberService.isLoginIdDuplicate(loginId)) {
            ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 로그인 ID입니다.")
        } else {
            ResponseEntity.ok("사용 가능한 로그인 ID입니다.")
        }
    }
    /**
     * 이메일 확인
     */
    @PostMapping("/request-email-verification")
    fun requestEmailVerification(@RequestParam email: String): ResponseEntity<String> {
        val result = memberService.requestEmailVerification(email)
        return ResponseEntity.ok(result)
    }

    /**
     * 이메일 인증을 처리하는 메서드
     */
    @GetMapping("/verify-email")
    fun verifyEmail(@RequestParam("email") email: String, @RequestParam("token") token: String): BaseResponse<Unit> {
        val resultMsg = memberService.verifyEmail(email, token)
        return BaseResponse(message = resultMsg)
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    fun login(@RequestBody @Valid loginDto: LoginDto): BaseResponse<TokenInfo> {
        val tokenInfo = memberService.login(loginDto)
        return BaseResponse(data = tokenInfo)
    }

    /**
     * 내 정보 보기
     */
    @GetMapping("/info")
    fun searchMyInfo(): BaseResponse<MemberDtoResponse> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        val response = memberService.searchMyInfo(userId)
        return BaseResponse(data = response)
    }

    /**
     * 내 정보 수정
     */
    @PutMapping("/info")
    fun saveMyInfo(@RequestBody @Valid updateDtoRequest: UpdateDtoRequest): BaseResponse<Unit> {
        val userId = (SecurityContextHolder.getContext().authentication.principal as CustomUser).userId
        updateDtoRequest.id = userId

        val responseMsg: String = memberService.saveMyInfo(updateDtoRequest)
        return BaseResponse(message = responseMsg)
    }

    /**
     * Access Token 갱신
     */
    @PostMapping("/refresh")
    fun refreshAccessToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<AccessTokenResponse> {
        val newAccessToken = memberService.refreshAccessToken(request.refreshToken)
        return ResponseEntity.ok(AccessTokenResponse(newAccessToken))
    }

    /**
     * 아이디 찾기 (이메일로 전송)
     */
    @PostMapping("/find-id")
    fun findIdByEmail(@RequestBody request: FindIdRequest): ResponseEntity<String> {
        return try {
            memberService.findIdByEmail(request.email)
            ResponseEntity.ok("아이디가 이메일로 발송되었습니다.")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("이메일이 등록되어 있지 않습니다.")
        }
    }

    /**
     * 비밀번호 재설정 요청
     */
    @PostMapping("/reset-password")
    fun resetPassword(@RequestBody request: ResetPasswordRequest): ResponseEntity<String> {
        return try {
            memberService.resetPassword(request.email)
            ResponseEntity.ok("임시 비밀번호가 이메일로 발송되었습니다.")
        } catch (e: NoSuchElementException) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("이메일을 찾을 수 없습니다.")
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("비밀번호 재설정 중 오류가 발생했습니다.")
        }
    }

    data class ResetPasswordRequest(val email: String)
    data class FindIdRequest(val email: String)
}
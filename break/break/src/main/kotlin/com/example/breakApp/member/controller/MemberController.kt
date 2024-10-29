package com.example.breakApp.member.controller

import com.example.breakApp.common.authority.JwtTokenProvider
import com.example.breakApp.common.authority.SecurityConfig
import com.example.breakApp.common.authority.TokenInfo
import com.example.breakApp.common.dto.BaseResponse
import com.example.breakApp.common.dto.CustomUser
import com.example.breakApp.member.dto.*
import com.example.breakApp.member.service.MemberService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.ResponseEntity               // ResponseEntity import 추가
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping

@RequestMapping("/api/member")
@RestController
class MemberController (
    private val memberService: MemberService,
    private val jwtTokenProvider: JwtTokenProvider
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
        updateDtoRequest.id = userId  // 업데이트 요청에 사용자 ID 설정

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
}

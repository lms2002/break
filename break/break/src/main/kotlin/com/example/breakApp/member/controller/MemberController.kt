package com.example.breakApp.member.controller

import com.example.breakApp.common.authority.JwtTokenProvider
import com.example.breakApp.common.authority.TokenInfo
import com.example.breakApp.common.dto.BaseResponse
import com.example.breakApp.member.dto.LoginDto
import com.example.breakApp.member.dto.MemberDtoRequest
import com.example.breakApp.member.service.MemberService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.example.breakApp.member.dto.RefreshTokenRequest  // RefreshTokenRequest import 추가
import com.example.breakApp.member.dto.AccessTokenResponse   // AccessTokenResponse import 추가
import org.springframework.http.ResponseEntity               // ResponseEntity import 추가

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
     * Access Token 갱신
     */
    @PostMapping("/refresh")
    fun refreshAccessToken(@RequestBody request: RefreshTokenRequest): ResponseEntity<AccessTokenResponse> {
        val newAccessToken = memberService.refreshAccessToken(request.refreshToken)
        return ResponseEntity.ok(AccessTokenResponse(newAccessToken))
    }
}

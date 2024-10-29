package com.example.breakApp.member.controller

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

@RequestMapping("/api/member")
@RestController // 컨트롤러가 HTTP 요청을 처리하고 그 결과를 JSON 형태로 응답하게 만듦
class MemberController (
    private val memberService: MemberService
) {
    /**
     * 회원가입
     */
    @PostMapping("/signup")
    fun signUp(@RequestBody @Valid memberDtoRequest: MemberDtoRequest): BaseResponse<Unit> { // Unit는 비어있는 값
        val resultMsg: String = memberService.signUp(memberDtoRequest) // 회원가입 완료 메시지 변수에 저장
        return BaseResponse(message = resultMsg) // 회원가입 완료 메시지 클라이언트에게 전달
    }

    /**
     * 로그인
     */
    @PostMapping("/login")
    fun login(@RequestBody@Valid loginDto: LoginDto): BaseResponse<TokenInfo>{
        val tokenInfo = memberService.login(loginDto)
        return BaseResponse(data = tokenInfo)
    }
}
package com.example.breakApp.api

import com.example.breakApp.api.model.*
import retrofit2.Response
import retrofit2.http.*

// API 서비스 인터페이스
interface ApiService {

    // 테스트용 API 연결 확인
    @GET("test")
    suspend fun testConnection(): Response<BaseResponse<Map<String, String>>>

    // 아이디 찾기
    @POST("api/member/find-id")
    suspend fun findIdByEmail(@Body request: FindIdRequest): Response<BaseResponse<String>>

    // 비밀번호 재설정 요청
    @POST("api/member/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<BaseResponse<String>>

    // 회원가입
    @POST("member/signup")
    suspend fun signUp(@Body memberDtoRequest: MemberDtoRequest): Response<BaseResponse<Unit>>

    // 로그인
    @POST("api/member/login")
    suspend fun login(@Body loginDto: LoginDto): Response<BaseResponse<TokenInfo>>

    // 내 정보 조회
    @GET("api/member/info")
    suspend fun getMyInfo(@Header("Authorization") token: String): Response<BaseResponse<MemberDtoResponse>>

    // 내 정보 수정
    @PUT("api/member/info")
    suspend fun updateMyInfo(@Body updateDtoRequest: UpdateDtoRequest, @Header("Authorization") token: String): Response<BaseResponse<Unit>>

    // 이메일 인증 요청
    @POST("api/member/request-email-verification")
    suspend fun requestEmailVerification(@Body emailRequest: EmailRequest): Response<BaseResponse<String>>

    // 이메일 인증 확인
    @GET("api/member/verify-email")
    suspend fun verifyEmail(@Query("email") email: String, @Query("token") token: String): Response<BaseResponse<Unit>>

    // 인바디 생성
    @POST("api/inbody/create")
    suspend fun createInBody(@Body createInBodyDto: CreateInBodyDto, @Header("Authorization") token: String): Response<BaseResponse<InBodyResponse>>

    // 인바디 목록 조회
    @GET("api/inbody/list")
    suspend fun getInBodyList(@Header("Authorization") token: String): Response<BaseResponse<List<InBodyResponse>>>

    // 인바디 업데이트
    @PUT("api/inbody/update/{id}")
    suspend fun updateInBody(@Path("id") id: Long, @Body updateDto: CreateInBodyDto, @Header("Authorization") token: String): Response<BaseResponse<InBodyResponse>>

    // 인바디 삭제
    @DELETE("api/inbody/delete/{id}")
    suspend fun deleteInBody(@Path("id") id: Long, @Header("Authorization") token: String): Response<BaseResponse<Unit>>

    // 이메일 인증 코드 확인 요청
    @POST("api/member/verify-email")
    suspend fun verifyEmailCode(@Body request: VerifyCodeRequest): Response<BaseResponse<Unit>>
}
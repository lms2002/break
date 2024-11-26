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
    @POST("member/find-id")
    suspend fun findIdByEmail(@Body request: FindIdRequest): Response<String>

    // 비밀번호 재설정 요청
    @POST("member/reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<String>

    // 회원가입
    @POST("member/signup")
    suspend fun signUp(@Body memberDtoRequest: MemberDtoRequest): Response<BaseResponse<Unit>>

    // 로그인
    @POST("member/login")
    suspend fun login(@Body loginDto: LoginDto): Response<BaseResponse<TokenInfo>>

    // 내 정보 조회
    @GET("member/info")
    suspend fun getMyInfo(): Response<BaseResponse<MemberDtoResponse>>

    // 내 정보 수정
    @PUT("member/info")
    suspend fun updateMyInfo(@Body updateDtoRequest: UpdateDtoRequest): Response<BaseResponse<Unit>>

    // 이메일 인증 요청
    @POST("member/request-email-verification")
    suspend fun requestEmailVerification(@Query("email") email: String): Response<String>

    // 이메일 인증 확인
    @GET("member/verify-email")
    suspend fun verifyEmail(@Query("email") email: String, @Query("token") token: String): Response<BaseResponse<Unit>>

    // 인바디 데이터 생성
    @POST("inbody")
    suspend fun createInBody(@Body createInBodyDto: CreateInBodyDto): Response<BaseResponse<InBodyResponse>>

    // 사용자 전체 인바디 데이터 조회
    @GET("inbody")
    suspend fun getInBodyList(): Response<BaseResponse<List<InBodyResponse>>>

    // 인바디 데이터 수정
    @PUT("inbody/{id}")
    suspend fun updateInBody(
        @Path("id") id: Long,
        @Body updateInBodyDto: CreateInBodyDto
    ): Response<BaseResponse<InBodyResponse>>

    // 인바디 데이터 삭제
    @DELETE("inbody/{id}")
    suspend fun deleteInBody(@Path("id") id: Long): Response<BaseResponse<Unit>>

    // 모든 운동 데이터를 가져오는 API
    @GET("exercises")
    suspend fun getAllExercises(): Response<List<Exercise>>

    // 이름으로 운동 데이터를 가져오는 API
    @GET("exercises/{name}")
    suspend fun getExerciseByName(
        @Path("name") name: String
    ): Response<BaseResponse<Exercise>>

    // 카테고리별 운동 데이터를 가져오는 API
    @GET("exercises/category/{bodyPart}")
    suspend fun getExercisesByCategory(
        @Path("bodyPart") bodyPart: String
    ): Response<List<Exercise>>

    @GET("exercises/target/{target}")
    suspend fun getExercisesByTargetArea(
        @Path("target") target: String
    ): Response<List<Exercise>> // List<Exercise>로 수정

    // Create a new routine
    @POST("routines")
    suspend fun createRoutine(
        @Body routineDto: RoutineDto
    ): Response<RoutineDto>

    // Get a list of all routines for the user
    @GET("routines")
    suspend fun getRoutineList(
    ): Response<List<RoutineDto>>

    // Get a specific routine by ID
    @GET("routines/{routineId}")
    suspend fun getRoutineById(
        @Path("routineId") routineId: Long
    ): Response<RoutineDto>

    // Update a routine by ID
    @PUT("routines/{routineId}")
    suspend fun updateRoutine(
        @Path("routineId") routineId: Long,
        @Body updatedRoutineDto: RoutineDto
    ): Response<RoutineDto>

    // Delete a routine by ID
    @DELETE("routines/{routineId}")
    suspend fun deleteRoutine(
        @Path("routineId") routineId: Long
    ): Response<Unit>

    // ApiService.kt
    @GET("routine-exercise/{routineId}")
    suspend fun getExercisesByRoutineId(
        @Path("routineId") routineId: Long
    ): Response<List<Exercise>>

    @POST("routine-exercise")
    suspend fun addExercisesToRoutine(
        @Body request: CreateRoutineExerciseRequest
    ): Response<Unit>

    // 토큰 검증
    @POST("auth/validate-token")
    suspend fun validateToken(): Response<BaseResponse<Boolean>>
}

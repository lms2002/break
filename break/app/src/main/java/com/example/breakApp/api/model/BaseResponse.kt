package com.example.breakApp.api.model

// API 응답의 기본 구조를 정의하는 클래스
data class BaseResponse<T>(
    val resultCode: String,  // 응답 코드 (예: SUCCESS, ERROR 등)
    val data: T?,            // 데이터 필드 (제네릭으로 받아서 다양한 타입을 처리)
    val message: String      // 메시지 (성공 메시지 또는 에러 메시지)
)
package com.project.break_app.domain

// 사용자 프로필 정보를 담는 데이터 클래스
data class UserProfile(
    val username: String,
    val email: String,
    val age: Int,
    val gender: String
)

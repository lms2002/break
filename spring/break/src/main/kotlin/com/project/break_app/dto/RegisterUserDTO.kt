package com.project.break_app.dto

// 회원가입 시 받을 정보를 위한 DTO
data class RegisterUserDTO(
    val userName: String,
    val email: String,
    val age: Int,
    val gender: String,
    val password: String
)

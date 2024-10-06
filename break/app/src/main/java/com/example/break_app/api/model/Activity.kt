package com.example.break_app.api.model

data class Activity(
    val id: Long,                 // 활동 ID
    val name: String,             // 활동 이름
    val distance: Double,         // 활동 거리 (미터 단위)
    val moving_time: Int,         // 움직인 시간 (초 단위)
    val elapsed_time: Int,        // 경과 시간 (초 단위)
    val total_elevation_gain: Double, // 고도 상승량 (미터 단위)
    val type: String,             // 활동 유형 (예: "Ride", "Run")
    val start_date: String        // 활동 시작 시간 (ISO 8601 형식)
)
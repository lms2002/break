package com.example.breakApp.common.dto

import com.example.breakApp.common.status.ResultCode

data class BaseResponse<T> (
    val resultCode: String = ResultCode.SUCCESS.name, // 결과코드
    val data: T? = null, // 데이터 처리 시 값을 담을 제네릭 변수
    val message: String = ResultCode.SUCCESS.msg,
)
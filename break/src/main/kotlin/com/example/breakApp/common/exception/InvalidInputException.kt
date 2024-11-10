package com.example.breakApp.common.exception

// 잘못 된 값을 전달 할 시 예외를 던지는 클래스
class InvalidInputException (
    val fieldName: String = "",
    message: String = "Invalid Input"
) :RuntimeException(message)
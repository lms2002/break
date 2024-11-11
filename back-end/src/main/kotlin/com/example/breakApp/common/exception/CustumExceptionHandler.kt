package com.example.breakApp.common.exception

import com.example.breakApp.common.dto.BaseResponse
import com.example.breakApp.common.status.ResultCode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {
    // 유효성 검사 실패 시 발생하는 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun methodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mutableMapOf<String, String>()
        ex.bindingResult.allErrors.forEach { error ->
            val fieldName = (error as FieldError).field
            val errorMessage = error.defaultMessage
            errors[fieldName] = errorMessage ?: "Not Exception Message"
        }

        return ResponseEntity(BaseResponse(ResultCode.ERROR.name, errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }

    // 잘못된 입력값이 있을 때 발생하는 예외 처리
    @ExceptionHandler(InvalidInputException::class)
    protected fun invalidInputException(ex: InvalidInputException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf(ex.fieldName to (ex.message ?: "Not Exception Message"))
        return ResponseEntity(BaseResponse(ResultCode.ERROR.name, errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }

    // 로그인 실패 시 발생하는 예외 처리
    @ExceptionHandler(BadCredentialsException::class)
    protected fun badCredentialsException(ex: BadCredentialsException): ResponseEntity<BaseResponse<Map<String, String>>> {
        val errors = mapOf("로그인 실패" to "아이디 혹은 비밀번호를 다시 확인하세요.")
        return ResponseEntity(BaseResponse(ResultCode.ERROR.name, errors, ResultCode.ERROR.msg), HttpStatus.BAD_REQUEST)
    }
}
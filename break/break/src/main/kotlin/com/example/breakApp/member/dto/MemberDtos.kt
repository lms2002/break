package com.example.breakApp.member.dto

import com.example.breakApp.common.annotation.ValidEnum
import com.example.breakApp.common.status.Gender
import com.example.breakApp.member.entity.Member
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

/**
 * 회원가입 시 클라이언트로부터 데이터를 받기 위한 DTO 클래스
 */
data class MemberDtoRequest(

    // 로그인 ID는 필수 입력 값이며 빈 값일 수 없음
    @field:NotBlank
    @field:Pattern(
        regexp = "^[a-zA-Z0-9]{5,20}$",
        message = "로그인 ID는 영문자와 숫자로 이루어진 5~20자이어야 합니다."
    )
    @JsonProperty("loginId")  // JSON에서 "loginId" 키와 매핑
    private val _loginId: String?,

    // 패스워드는 필수 입력 값이며, 영문, 숫자, 특수문자를 포함한 8~20자리의 패턴을 만족해야 함
    @field:NotBlank
    @field:Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#\$%^&*])[a-zA-Z0-9!@#\$%^&*]{8,20}\$",
        message = "영문, 숫자, 특수문자를 포함한 8~20자리로 입력해주세요"
    )
    @JsonProperty("password")  // JSON에서 "password" 키와 매핑
    private val _password: String?,

    // 사용자 이름은 필수 입력 값이며 빈 값일 수 없음
    @field:NotBlank
    @JsonProperty("userName")  // JSON에서 "userName" 키와 매핑
    private val _userName: String?,

    // 이메일은 필수 입력 값이며, 유효한 이메일 형식이어야 함
    @field:NotBlank
    @field:Email  // 이메일 유효성 검사
    @JsonProperty("email")  // JSON에서 "email" 키와 매핑
    private val _email: String?,

    // 성별은 필수 입력 값이며, MALE 또는 FEMALE 값만 허용
    @field:NotBlank
    @field:ValidEnum(enumClass = Gender::class, message = "MALE 이나 FEMALE 중 하나를 선택해주세요.")  // Enum 값 검증
    @JsonProperty("gender")  // JSON에서 "gender" 키와 매핑
    private val _gender: String?
) {
    // loginId 값을 가져오는 프로퍼티, 값이 null일 경우 예외 발생
    val loginId: String
        get() = _loginId!!

    // password 값을 가져오는 프로퍼티, 값이 null일 경우 예외 발생
    val password: String
        get() = _password!!

    // userName 값을 가져오는 프로퍼티, 값이 null일 경우 예외 발생
    val userName: String
        get() = _userName!!

    // email 값을 가져오는 프로퍼티, 값이 null일 경우 예외 발생
    val email: String
        get() = _email!!

    // 성별 값을 Gender Enum으로 변환하여 반환, 값이 null일 경우 예외 발생
    val gender: Gender
        get() = Gender.valueOf(_gender!!)

    fun toEntity(): Member =
        Member(
            loginId = loginId,  // null을 전달하거나 적절한 Long 값으로 변경
            password = password,
            userName = userName,
            email = email,
            gender = gender
        )
}

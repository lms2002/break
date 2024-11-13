package com.example.breakApp.common.annotation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

// 어노테이션이 필드에만 적용된다는 것을 의미
@Target(AnnotationTarget.FIELD) // 어노테이션이 런타임 중에도 유지되어야 한다는 의미 (즉, 실행 중에 접근 가능)
@Retention(AnnotationRetention.RUNTIME) // 어노테이션이 API 문서나 코드 문서에서 문서화될 수 있도록 명시
@MustBeDocumented // 유효성 검사를 ValidEnumValidator 클래스에서 처리하도록 설정
@Constraint(validatedBy = [ValidEnumValidator::class])
annotation class ValidEnum(
    val message: String = "Invalid enum value",  // 기본 에러 메시지, 유효성 검사가 실패했을 때 반환
    val groups: Array<KClass<*>> = [],  // 검증 그룹을 지정할 수 있는 필드, 기본값은 빈 배열
    val payload: Array<KClass<out Payload>> = [],  // 유효성 검증 시 추가적인 정보를 전달하기 위한 필드
    val enumClass: KClass<out Enum<*>>  // 유효성을 검사할 Enum 클래스를 지정하는 필드
)

// 유효성 검사 클래스. ConstraintValidator를 상속받아 구현
class ValidEnumValidator : ConstraintValidator<ValidEnum, Any> {
    // Enum 클래스의 모든 상수 값을 저장하는 배열. 나중에 유효성 검사에 사용됨
    private lateinit var enumValues: Array<out Enum<*>>

    // 어노테이션 초기화. Enum 클래스의 모든 상수 값을 enumValues에 저장
    override fun initialize(annotation: ValidEnum) {
        // 어노테이션에서 enumClass를 가져와 해당 Enum 클래스의 모든 상수를 enumValues에 저장
        enumValues = annotation.enumClass.java.enumConstants
    }

    // 실제 유효성 검사를 처리하는 메서드. 값이 유효한지 여부를 반환
    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        // 값이 null인 경우 유효성 검사 통과 (nullable 허용)
        if (value == null) {
            return true  // nullable한 필드일 때, null 값은 허용
        }
        // Enum 값들 중에 입력된 값과 일치하는 값이 있는지 확인
        return enumValues.any { it.name == value.toString() }
    }
}
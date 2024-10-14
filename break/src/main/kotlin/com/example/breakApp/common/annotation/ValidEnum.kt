package com.example.breakApp.common.annotation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)  // 어노테이션이 필드에만 적용됨
@Retention(AnnotationRetention.RUNTIME)  // 런타임 동안 어노테이션 유지
@MustBeDocumented  // 문서화 필요
@Constraint(validatedBy = [ValidEnumValidator::class])  // 이 어노테이션이 ValidEnumValidator로 유효성 검사를 수행함?
annotation class ValidEnum(
    val message: String = "Invalid enum value",  // 기본 에러 메시지
    val groups: Array<KClass<*>> = [],  // 검증 그룹
    val payload: Array<KClass<out Payload>> = [],  // 추가 정보를 위한 필드
    val enumClass: KClass<out Enum<*>>  // Enum 클래스 지정
)

class ValidEnumValidator : ConstraintValidator<ValidEnum, Any> {
    private lateinit var enumValues: Array<out Enum<*>>  // Enum의 값들을 저장할 배열

    override fun initialize(annotation: ValidEnum) {
        // 어노테이션에서 enumClass 값을 가져와 해당 enum의 모든 상수를 enumValues에 저장
        enumValues = annotation.enumClass.java.enumConstants
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true  // 값이 null일 경우 유효성 검사에서 통과 (nullable 허용)
        }
        // 입력된 값이 Enum의 상수 중 하나와 일치하는지 검사
        return enumValues.any { it.name == value.toString() }
    }
}


package com.example.breakApp.exercise.entity

import jakarta.persistence.*

// 이 패키지는 Exercise 엔티티를 정의하는 클래스입니다.

@Entity
@Table(name = "exercise")
data class Exercise(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val exerciseId: Long? = null,

    // @Entity: 이 클래스가 JPA 엔티티임을 나타냅니다.
    // @Table(name = "exercise"): 데이터베이스에서 이 엔티티가 "exercise" 테이블과 매핑됨을 정의합니다.
    // @Id와 @GeneratedValue: exerciseId 필드를 기본 키로 설정하며, 자동으로 증가하는 IDENTITY 전략을 사용하여 값을 자동 생성합니다.
    // exerciseId 필드는 Long 타입의 기본 키로, 데이터베이스에서 생성된 ID를 저장합니다.

    @Column(nullable = false)
    val name: String,

    // @Column(nullable = false): name 필드는 NULL 값을 허용하지 않는 칼럼으로 설정됩니다.
    // 이 필드는 운동의 이름을 저장합니다.

    @Column(columnDefinition = "TEXT", nullable = true)
    val description: String? = null,

    // @Column(columnDefinition = "TEXT", nullable = true): description 필드는 TEXT 타입이며, NULL 값을 허용합니다.
    // 운동 설명을 저장하는 필드로, 내용이 없을 수도 있습니다.

    @Column(nullable = false)
    val category: String = "General",

    // @Column(nullable = false): category 필드는 NULL 값을 허용하지 않으며 기본값으로 "General"을 설정합니다.
    // 운동의 카테고리를 나타내는 필드입니다.

    @Column(nullable = false)
    val targetArea: String = "Full Body"

    // @Column(nullable = false): targetArea 필드도 NULL 값을 허용하지 않으며 기본값으로 "Full Body"를 설정합니다.
    // 운동의 타겟 부위를 나타내는 필드입니다.
)

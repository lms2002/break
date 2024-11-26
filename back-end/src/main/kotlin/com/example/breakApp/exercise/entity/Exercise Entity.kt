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

    @Column(nullable = true)
    val name: String?,

    @Column(columnDefinition = "TEXT", nullable = true)
    val instructions: String? = null,

    @Column(nullable = false)
    val bodyPart: String = "other",

    @Column(nullable = false)
    val target: String = "other",

    @Column(nullable = true)
    val equipment: String? = null,

    @Column(nullable = true)
    val gifUrl: String? = null // GIF URL 필드 추가
)

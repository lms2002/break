package com.example.breakApp.exercise.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "Exercise")
class Exercise(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    var exerciseId: Long? = null,  // 운동 ID (자동 증가)

    @Column(name = "name", nullable = false, length = 50)
    var name: String,  // 운동 이름 (필수)

    @Column(name = "description", columnDefinition = "TEXT")
    var description: String?,  // 운동 설명 (선택)

    @Column(name = "category", nullable = false, length = 50)
    var category: String,  // 운동 카테고리 (필수)

    @Column(name = "target_area", nullable = false, length = 50)
    var targetArea: String,  // 타겟 부위 (필수)

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDate = LocalDate.now(),  // 생성일 (기본값: 현재 날짜)

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDate = LocalDate.now()  // 수정일 (기본값: 현재 날짜)
)
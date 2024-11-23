package com.example.breakApp.set.entity

import jakarta.persistence.*

@Entity
@Table(name = "exercise_set")
class ExerciseSet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "set_id")
    val setId: Long? = null,  // 세트 ID

    @Column(name = "routine_id", nullable = false)
    val routineId: Long,  // 루틴 ID

    @Column(name = "exercise_id", nullable = false)
    val exerciseId: Long,  // 운동 ID

    @Column(name = "set_number", nullable = false)
    var setNumber: Int,  // 세트 번호

    @Column(name = "repetitions", nullable = false)
    var repetitions: Int = 10,  // 반복 횟수, 기본값 10

    @Column(name = "weight", nullable = false)
    var weight: Float = 10.0f,  // 중량, 기본값 10

    @Column(name = "is_completed", nullable = false)
    var isCompleted: Boolean = false  // 세트 완료 여부
)
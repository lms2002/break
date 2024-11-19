package com.example.breakApp.routine.entity

import com.example.breakApp.exercise.entity.Exercise // 운동 엔티티 참조
import jakarta.persistence.* // JPA 관련 어노테이션 사용

@Entity
@Table(name = "routine_exercise_manager") // 테이블 이름 매핑
class RoutineExerciseManager(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 자동 생성
    @Column(name = "routine_exercise_manager_id") // 테이블 필드 이름과 매핑
    val id: Long? = null, // 고유 ID

    @ManyToOne(fetch = FetchType.LAZY) // Routine과의 다대일 관계 설정
    @JoinColumn(name = "routine_id", nullable = false) // 외래 키 매핑
    val routine: Routine, // Routine 엔티티 참조

    @ManyToOne(fetch = FetchType.LAZY) // Exercise와의 다대일 관계 설정
    @JoinColumn(name = "exercise_id", nullable = false) // 외래 키 매핑
    val exercise: Exercise, // Exercise 엔티티 참조

    @Column(nullable = false, columnDefinition = "INT DEFAULT 1") // 기본값 설정
    var sets: Int = 1, // 운동 세트 수, 기본값 1

    @Column(nullable = false, columnDefinition = "INT DEFAULT 10") // 기본값 설정
    var repetitions: Int = 10, // 운동 반복 횟수, 기본값 10

    @Column(nullable = false, columnDefinition = "FLOAT DEFAULT 10") // 기본값 설정
    var weight: Float = 10f, // 운동 중량, 기본값 10

    @Column(name = "rest_time_seconds", nullable = false, columnDefinition = "INT DEFAULT 30") // 기본값 설정
    var restTimeSeconds: Int = 30 // 세트 간 휴식 시간, 기본값 30초
)

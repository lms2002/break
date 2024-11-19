package com.example.breakApp.routine.entity

import com.example.breakApp.exercise.entity.Exercise
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "routine_exercise_manager") // 테이블 이름 매핑
@IdClass(RoutineExerciseManagerId::class) // 복합 키를 지정하는 클래스
class RoutineExerciseManager(
    @Id
    @ManyToOne(fetch = FetchType.LAZY) // Routine과 다대일 관계 설정
    @JoinColumn(name = "routine_id", nullable = false) // 외래 키 매핑
    val routine: Routine,

    @Id
    @ManyToOne(fetch = FetchType.LAZY) // Exercise와 다대일 관계 설정
    @JoinColumn(name = "exercise_id", nullable = false) // 외래 키 매핑
    val exercise: Exercise,

    @Column(nullable = false, columnDefinition = "INT DEFAULT 1") // 기본값 설정
    var sets: Int = 1, // 세트 수 (기본값 1)

    @Column(nullable = false, columnDefinition = "INT DEFAULT 10") // 기본값 설정
    var repetitions: Int = 10, // 반복 횟수 (기본값 10)

    @Column(nullable = false, columnDefinition = "FLOAT DEFAULT 10") // 기본값 설정
    var weight: Float = 10f, // 중량 (기본값 10)

    @Column(name = "rest_time_seconds", nullable = false, columnDefinition = "INT DEFAULT 30") // 기본값 설정
    var restTimeSeconds: Int = 30, // 세트 간 휴식 시간 (기본값 30초)

    @CreationTimestamp
    @Column(name = "created_at", updatable = false) // 생성일
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    @Column(name = "updated_at") // 수정일
    val updatedAt: LocalDateTime? = null
)
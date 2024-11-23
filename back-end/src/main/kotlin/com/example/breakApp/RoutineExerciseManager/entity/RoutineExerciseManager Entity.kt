package com.example.breakApp.RoutineExerciseManager.entity

import com.example.breakApp.exercise.entity.Exercise
import com.example.breakApp.routine.entity.Routine
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "routine_exercise_manager")
@IdClass(RoutineExerciseManagerId::class)
class RoutineExerciseManager(
    @Id
    @Column(name = "routine_id")
    val routineId: Long,  // 루틴 ID

    @Id
    @Column(name = "exercise_id")
    val exerciseId: Long,  // 운동 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", insertable = false, updatable = false)
    val routine: Routine,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", insertable = false, updatable = false)
    val exercise: Exercise,

    @Column(name = "created_at")
    val createdAt: LocalDateTime = LocalDateTime.now(),  // 생성일

    @Column(name = "updated_at")
    val updatedAt: LocalDateTime = LocalDateTime.now()  // 수정일
)
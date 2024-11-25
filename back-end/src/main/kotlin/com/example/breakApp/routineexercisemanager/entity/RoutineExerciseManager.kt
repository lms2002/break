package com.example.breakApp.routineexercisemanager.entity

import com.example.breakApp.exercise.entity.Exercise
import com.example.breakApp.routine.entity.Routine
import jakarta.persistence.*
import java.io.Serializable
import java.time.LocalDateTime

@IdClass(RoutineExerciseManager.RoutineExerciseManagerId::class)
@Entity
@Table(name = "routine_exercise_manager")
class RoutineExerciseManager(
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    val routine: Routine,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    val exercise: Exercise,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    // 내부 클래스로 RoutineExerciseManagerId를 포함시킵니다.
    data class RoutineExerciseManagerId(
        val routine: Long = 0,
        val exercise: Long = 0
    ) : Serializable
}

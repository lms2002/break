package com.example.breakApp.set.entity

import com.example.breakApp.exercise.entity.Exercise
import com.example.breakApp.routine.entity.Routine
import com.example.breakApp.workoutlog.dto.CompletedSetDto
import jakarta.persistence.*

@Entity
@Table(name = "exercise_set")
class ExerciseSet(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "set_id")
    var setId: Long? = null,  // 세트 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    var routine: Routine,  // 루틴 엔티티

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    var exercise: Exercise,  // 운동 엔티티

    @Column(name = "set_number", nullable = false)
    var setNumber: Int,  // 세트 번호

    @Column(name = "repetitions", nullable = false)
    var repetitions: Int = 10,  // 반복 횟수, 기본값 10

    @Column(name = "weight", nullable = false)
    var weight: Float = 10.0f,  // 중량, 기본값 10

    @Column(name = "is_completed", nullable = false)
    var isCompleted: Boolean = false  // 세트 완료 여부
)
fun ExerciseSet.toDto(): CompletedSetDto {
    return CompletedSetDto(
        setId = this.setId!!,
        setNumber = this.setNumber,
        repetitions = this.repetitions,
        weight = this.weight
    )
}
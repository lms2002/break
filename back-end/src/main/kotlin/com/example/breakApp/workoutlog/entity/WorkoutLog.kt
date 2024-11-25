package com.example.breakApp.workoutlog.entity

import com.example.breakApp.member.entity.Member
import com.example.breakApp.exercise.entity.Exercise
import com.example.breakApp.routine.entity.Routine
import com.example.breakApp.set.entity.ExerciseSet
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "workout_log")
data class WorkoutLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    val logId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    val routine: Routine,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    val exercise: Exercise,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "set_id", nullable = false)
    val setId: ExerciseSet,

    @Column(name = "repetitions", nullable = false)
    val repetitions: Int = 10,

    @Column(name = "weight", nullable = false)
    val weight: Float = 10.0f,

    @Column(name = "is_completed", nullable = false)
    val isCompleted: Boolean = false,

    @Column(name = "start_time", nullable = false)
    val startTime: LocalDateTime,

    @Column(name = "end_time", nullable = true)
    var endTime: LocalDateTime? = null,

    @Column(name = "duration", insertable = false, updatable = false)
    val duration: Int? = null
)

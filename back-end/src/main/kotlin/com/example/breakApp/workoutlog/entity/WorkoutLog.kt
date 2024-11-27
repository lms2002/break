package com.example.breakApp.workoutlog.entity

import com.example.breakApp.exercise.entity.Exercise
import com.example.breakApp.member.entity.Member
import com.example.breakApp.routine.dto.RoutineDto
import com.example.breakApp.workoutlog.dto.WorkoutLogDto
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
    val logId: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    val routine: Routine,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = true)
    val exercise: Exercise? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "set_id", nullable = true)
    val exerciseSet: ExerciseSet? = null,

    @Column(name = "repetitions", nullable = false)
    val repetitions: Int = 10,

    @Column(name = "weight", nullable = false)
    val weight: Float = 10f,

    @Column(name = "start_time", nullable = false)
    val startTime: LocalDateTime,

    @Column(name = "end_time")
    var endTime: LocalDateTime? = null,

    @Column(name = "duration", insertable = false, updatable = false)
    val duration: Int? = null
)
fun WorkoutLog.toDto(): WorkoutLogDto {
    return WorkoutLogDto(
        logId = this.logId!!,
        routine = RoutineDto(
            routineId = this.routine.routineId,
            userId = this.routine.member.userId!!, // Routine과 연결된 사용자 ID
            name = this.routine.name,
            createdAt = this.routine.createdAt,
            updatedAt = this.routine.updatedAt
        ),
        startTime = this.startTime,
        endTime = this.endTime,
        duration = this.endTime?.let {
            java.time.Duration.between(this.startTime, it).toMinutes()
        }
    )
}
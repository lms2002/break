package com.example.breakApp.routine.entity

import com.example.breakApp.member.entity.Member
import com.example.breakApp.routine.dto.RoutineDto
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "Routine")
class Routine(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_id")
    var routineId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val member: Member,

    @Column(nullable = false, length = 50)
    var name: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)
fun Routine.toDto(): RoutineDto {
    return RoutineDto(
        routineId = this.routineId,
        userId = this.member.userId!!,
        name = this.name,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
package com.example.breakApp.routine.entity

import com.example.breakApp.member.entity.Member
import jakarta.persistence.*

@Entity
@Table(name = "Routine")
class Routine(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_id")
    var routineId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    var member: Member,

    @Column(nullable = false, length = 50)
    var name: String,

    @Column(columnDefinition = "TEXT")
    var description: String
)
package com.example.breakApp.exercise.entity

import jakarta.persistence.*
import java.sql.Timestamp

@Entity
@Table(name = "exercise")
data class Exercise(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    val exerciseId: Long = 0,

    @Column(nullable = false, unique = true)
    val name: String,

    @Column(columnDefinition = "TEXT")
    val description: String? = null,

    @Column(nullable = false)
    val category: String,

    @Column(name = "target_area", nullable = false)
    val targetArea: String,

    @Column(name = "created_at", updatable = false)
    val createdAt: Timestamp = Timestamp(System.currentTimeMillis()),

    @Column(name = "updated_at")
    val updatedAt: Timestamp = Timestamp(System.currentTimeMillis())
)

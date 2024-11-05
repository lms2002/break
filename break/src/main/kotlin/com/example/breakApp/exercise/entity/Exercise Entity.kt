package com.example.breakApp.exercise.entity

import jakarta.persistence.*

@Entity
@Table(name = "exercise")
data class Exercise(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val exerciseId: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Column(columnDefinition = "TEXT", nullable = true)
    val description: String? = null,

    @Column(nullable = false)
    val category: String = "General",

    @Column(nullable = false)
    val targetArea: String = "Full Body"
)
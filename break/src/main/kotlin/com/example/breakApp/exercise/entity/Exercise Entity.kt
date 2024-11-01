package com.example.breakApp.exercise.entity

import jakarta.persistence.*

@Entity
@Table(name = "exercise", uniqueConstraints = [UniqueConstraint(columnNames = ["name"])])
data class Exercise(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val exerciseId: Long? = null,

    @Column(nullable = false)
    val name: String,

    @Column(columnDefinition = "TEXT")
    val description: String,

    @Column(nullable = false)
    val category: String,

    @Column(nullable = false)
    val targetArea: String
)
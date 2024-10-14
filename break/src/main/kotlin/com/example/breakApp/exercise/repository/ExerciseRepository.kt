package com.example.breakApp.exercise.repository

import com.example.breakApp.exercise.entity.Exercise
import org.springframework.data.jpa.repository.JpaRepository

interface ExerciseRepository : JpaRepository<Exercise, Long> {
    fun findByName(name: String): Exercise?
}
package com.example.breakApp.exercise.repository

import com.example.breakApp.exercise.entity.Exercise
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExerciseRepository : JpaRepository<Exercise, Long> {
    fun findByName(name: String): Exercise?
}
package com.example.breakApp.exercise.controller

import com.example.breakApp.exercise.entity.Exercise
import com.example.breakApp.exercise.repository.ExerciseRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/exercises")
class ExerciseController(
    private val exerciseRepository: ExerciseRepository
) {

    @GetMapping
    fun getAllExercises(): List<Exercise> {
        return exerciseRepository.findAll()
    }

    @GetMapping("/{name}")
    fun getExerciseByName(@PathVariable name: String): ResponseEntity<Exercise> {
        val exercise = exerciseRepository.findByName(name)
        return if (exercise != null) {
            ResponseEntity.ok(exercise)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping("/category/{category}")
    fun getExercisesByCategory(@PathVariable category: String): List<Exercise> {
        return exerciseRepository.findByCategory(category)
    }

    @GetMapping("/target/{targetArea}")
    fun getExercisesByTargetArea(@PathVariable targetArea: String): List<Exercise> {
        return exerciseRepository.findByTargetArea(targetArea)
    }
}

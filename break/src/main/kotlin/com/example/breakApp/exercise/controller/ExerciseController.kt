package com.example.breakApp.exercise.controller

import com.example.breakApp.exercise.entity.Exercise
import com.example.breakApp.exercise.repository.ExerciseRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
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
    fun getExerciseByName(@PathVariable name: String): Exercise? {
        return exerciseRepository.findByName(name)
    }
}

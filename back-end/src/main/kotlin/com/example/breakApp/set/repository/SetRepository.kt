package com.example.breakApp.set.repository

import com.example.breakApp.exercise.entity.Exercise
import com.example.breakApp.routine.entity.Routine
import com.example.breakApp.set.entity.ExerciseSet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExerciseSetRepository : JpaRepository<ExerciseSet, Long> {
    fun findByRoutineAndExercise(routine: Routine, exercise: Exercise): List<ExerciseSet>
    fun findByRoutine(routine: Routine): List<ExerciseSet>
}
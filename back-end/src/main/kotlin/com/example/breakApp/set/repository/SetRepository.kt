package com.example.breakApp.set.repository

import com.example.breakApp.set.entity.ExerciseSet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExerciseSetRepository : JpaRepository<ExerciseSet, Long> {
    fun findByRoutineIdAndExerciseId(routineId: Long, exerciseId: Long): List<ExerciseSet>
    fun findByRoutineId(routineId: Long): List<ExerciseSet>
}
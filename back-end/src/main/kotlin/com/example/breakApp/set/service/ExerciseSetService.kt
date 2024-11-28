package com.example.breakApp.set.service

import com.example.breakApp.common.authority.JwtTokenProvider
import com.example.breakApp.exercise.repository.ExerciseRepository
import com.example.breakApp.routine.repository.RoutineRepository
import com.example.breakApp.set.dto.ExerciseSetDto
import com.example.breakApp.set.entity.ExerciseSet
import com.example.breakApp.set.repository.ExerciseSetRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExerciseSetService(
    private val exerciseSetRepository: ExerciseSetRepository,
    private val exerciseRepository: ExerciseRepository,
    private val routineRepository: RoutineRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @Transactional
    fun createExerciseSet(exerciseSetDto: ExerciseSetDto, token: String): ExerciseSetDto {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val routine = routineRepository.findById(exerciseSetDto.routineId)
            .orElseThrow { RuntimeException("Routine not found with ID: ${exerciseSetDto.routineId}") }

        if (routine.member.userId != userId) {
            throw RuntimeException("User not authorized to access this routine")
        }

        val exercise = exerciseRepository.findById(exerciseSetDto.exerciseId)
            .orElseThrow { RuntimeException("Exercise not found with ID: ${exerciseSetDto.exerciseId}") }

        val exerciseSet = ExerciseSet(
            routine = routine,
            exercise = exercise,
            setNumber = exerciseSetDto.setNumber,
            repetitions = exerciseSetDto.repetitions,
            weight = exerciseSetDto.weight,
            isCompleted = exerciseSetDto.isCompleted
        )
        val savedSet = exerciseSetRepository.save(exerciseSet)
        return savedSet.toDto()
    }

    fun getSetsByRoutineAndExercise(routineId: Long, exerciseId: Long, token: String): List<ExerciseSetDto> {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val routine = routineRepository.findById(routineId)
            .orElseThrow { RuntimeException("Routine not found with ID: $routineId") }

        if (routine.member.userId != userId) {
            throw RuntimeException("User not authorized to access this routine")
        }

        val exercise = exerciseRepository.findById(exerciseId)
            .orElseThrow { RuntimeException("Exercise not found with ID: $exerciseId") }

        return exerciseSetRepository.findByRoutineAndExercise(routine, exercise).map { it.toDto() }
    }

    fun getSetsByRoutine(routineId: Long, token: String): List<ExerciseSetDto> {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val routine = routineRepository.findById(routineId)
            .orElseThrow { RuntimeException("Routine not found with ID: $routineId") }

        if (routine.member.userId != userId) {
            throw RuntimeException("User not authorized to access this routine")
        }

        return exerciseSetRepository.findByRoutine(routine).map { it.toDto() }
    }
    @Transactional
    fun updateExerciseSet(setId: Long, updatedSetDto: ExerciseSetDto, token: String): ExerciseSetDto {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val existingSet = exerciseSetRepository.findById(setId)
            .orElseThrow { RuntimeException("Set not found with ID: $setId") }

        if (existingSet.routine.member.userId != userId) {
            throw RuntimeException("User not authorized to update this set")
        }

        val exercise = exerciseRepository.findById(updatedSetDto.exerciseId)
            .orElseThrow { RuntimeException("Exercise not found with ID: ${updatedSetDto.exerciseId}") }

        existingSet.apply {
            setNumber = updatedSetDto.setNumber
            repetitions = updatedSetDto.repetitions
            weight = updatedSetDto.weight
            isCompleted = updatedSetDto.isCompleted
            this.exercise = exercise
        }

        val updatedSet = exerciseSetRepository.save(existingSet)
        return updatedSet.toDto()
    }

    @Transactional
    fun deleteExerciseSet(setId: Long, token: String) {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val existingSet = exerciseSetRepository.findById(setId)
            .orElseThrow { RuntimeException("Set not found with ID: $setId") }

        if (existingSet.routine.member.userId != userId) {
            throw RuntimeException("User not authorized to delete this set")
        }

        exerciseSetRepository.delete(existingSet)
    }
}

private fun ExerciseSet.toDto(): ExerciseSetDto = ExerciseSetDto(
    setId = this.setId,
    routineId = this.routine.routineId!!,
    exerciseId = this.exercise.exerciseId!!,
    setNumber = this.setNumber,
    repetitions = this.repetitions,
    weight = this.weight,
    isCompleted = this.isCompleted,
    createdAt = this.createdAt
)
package com.example.breakApp.routineexercisemanager.service

import com.example.breakApp.routineexercisemanager.dto.CreateRoutineExerciseRequest
import com.example.breakApp.routineexercisemanager.dto.RoutineExerciseManagerDto
import com.example.breakApp.routineexercisemanager.entity.RoutineExerciseManager
import com.example.breakApp.routineexercisemanager.repository.RoutineExerciseManagerRepository
import com.example.breakApp.exercise.repository.ExerciseRepository
import com.example.breakApp.routine.repository.RoutineRepository
import com.example.breakApp.common.authority.JwtTokenProvider
import com.example.breakApp.exercise.dto.ExerciseDetailDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoutineExerciseManagerService(
    private val routineExerciseManagerRepository: RoutineExerciseManagerRepository,
    private val routineRepository: RoutineRepository,
    private val exerciseRepository: ExerciseRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Transactional
    fun addExercisesToRoutine(request: CreateRoutineExerciseRequest, token: String): List<RoutineExerciseManagerDto> {
        val userId = jwtTokenProvider.getUserIdFromToken(token)

        println("User ID from token: $userId")
        println("Routine ID from request: ${request.routineId}")

        val routine = routineRepository.findById(request.routineId)
            .orElseThrow {
                println("Routine with ID ${request.routineId} not found for user $userId")
                RuntimeException("Routine not found")
            }

        // 현재 사용자(userId)와 루틴 소유자 비교
        if (routine.member.userId != userId) {
            println("User $userId is not authorized to modify routine with ID ${request.routineId}")
            throw RuntimeException("User not authorized to access this routine")
        }

        val createdRelations = request.exercises.map { exerciseRequest ->
            val exercise = exerciseRepository.findById(exerciseRequest.exerciseId)
                .orElseThrow {
                    println("Exercise with ID ${exerciseRequest.exerciseId} not found")
                    RuntimeException("Exercise not found")
                }

            val routineExercise = RoutineExerciseManager(
                routine = routine,
                exercise = exercise
            )
            routineExerciseManagerRepository.save(routineExercise)
        }

        return createdRelations.map {
            RoutineExerciseManagerDto(
                routineId = it.routine.routineId!!,
                exerciseId = it.exercise.exerciseId!!,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt
            )
        }
    }



    fun getExercisesByRoutine(routineId: Long, token: String): List<ExerciseDetailDto> {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val routine = routineRepository.findById(routineId)
            .orElseThrow { RuntimeException("Routine not found") }

        // 루틴 소유자 확인
        if (routine.member.userId != userId) {
            throw RuntimeException("User not authorized to view this routine")
        }

        val exercises = routineExerciseManagerRepository.findByRoutineRoutineId(routineId)
        return exercises.map {
            ExerciseDetailDto(
                exerciseId = it.exercise.exerciseId!!,
                name = it.exercise.name,
                description = it.exercise.description,
                category = it.exercise.category,
                targetArea = it.exercise.targetArea
            )
        }
    }

    @Transactional
    fun removeExerciseFromRoutine(routineId: Long, exerciseId: Long, token: String) {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val routine = routineRepository.findById(routineId)
            .orElseThrow { RuntimeException("Routine not found") }

        // 루틴 소유자 확인
        if (routine.member.userId != userId) {
            throw RuntimeException("User not authorized to modify this routine")
        }

        val routineExercise = routineExerciseManagerRepository.findByRoutineRoutineIdAndExerciseExerciseId(routineId, exerciseId)
            ?: throw RuntimeException("Exercise not found in the routine")

        routineExerciseManagerRepository.delete(routineExercise)
    }
}

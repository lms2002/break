package com.example.breakApp.RoutineExerciseManager.service

import com.example.breakApp.RoutineExerciseManager.dto.RoutineExerciseManagerDto
import com.example.breakApp.RoutineExerciseManager.entity.RoutineExerciseManager
import com.example.breakApp.RoutineExerciseManager.entity.RoutineExerciseManagerId
import com.example.breakApp.RoutineExerciseManager.repository.RoutineExerciseManagerRepository
import com.example.breakApp.exercise.repository.ExerciseRepository
import com.example.breakApp.routine.repository.RoutineRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoutineExerciseManagerService(
    private val routineExerciseManagerRepository: RoutineExerciseManagerRepository,
    private val routineRepository: RoutineRepository,
    private val exerciseRepository: ExerciseRepository
) {

    // 루틴에 운동 추가
    @Transactional
    fun addExerciseToRoutine(routineExerciseManagerDto: RoutineExerciseManagerDto): RoutineExerciseManagerDto {
        val routine = routineRepository.findById(routineExerciseManagerDto.routineId)
            .orElseThrow { RuntimeException("Routine not found") }
        val exercise = exerciseRepository.findById(routineExerciseManagerDto.exerciseId)
            .orElseThrow { RuntimeException("Exercise not found") }

        val routineExerciseManager = RoutineExerciseManager(
            routineId = routineExerciseManagerDto.routineId,
            exerciseId = routineExerciseManagerDto.exerciseId,
            routine = routine,
            exercise = exercise
        )

        val savedManager = routineExerciseManagerRepository.save(routineExerciseManager)
        return savedManager.toDto()
    }

    // 루틴의 모든 운동 조회
    fun getExercisesByRoutine(routineId: Long): List<RoutineExerciseManagerDto> {
        return routineExerciseManagerRepository.findByRoutineId(routineId)
            .map { it.toDto() }
    }

    // 루틴에서 운동 삭제
    @Transactional
    fun removeExerciseFromRoutine(routineId: Long, exerciseId: Long) {
        val id = RoutineExerciseManagerId(routineId, exerciseId)
        if (!routineExerciseManagerRepository.existsById(id)) {
            throw RuntimeException("Routine-Exercise relation not found")
        }
        routineExerciseManagerRepository.deleteById(id)
    }
}

// 확장 함수로 엔티티를 DTO로 변환
private fun RoutineExerciseManager.toDto(): RoutineExerciseManagerDto = RoutineExerciseManagerDto(
    routineId = this.routineId,
    exerciseId = this.exerciseId,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)
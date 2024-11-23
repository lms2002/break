package com.example.breakApp.set.service

import com.example.breakApp.set.dto.ExerciseSetDto
import com.example.breakApp.set.entity.ExerciseSet
import com.example.breakApp.set.repository.ExerciseSetRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExerciseSetService(
    private val exerciseSetRepository: ExerciseSetRepository
) {
    // 새로운 세트 생성
    @Transactional
    fun createExerciseSet(exerciseSetDto: ExerciseSetDto): ExerciseSetDto {
        val exerciseSet = ExerciseSet(
            routineId = exerciseSetDto.routineId,
            exerciseId = exerciseSetDto.exerciseId,
            setNumber = exerciseSetDto.setNumber,
            repetitions = exerciseSetDto.repetitions,
            weight = exerciseSetDto.weight,
            isCompleted = exerciseSetDto.isCompleted
        )
        val savedSet = exerciseSetRepository.save(exerciseSet)
        return savedSet.toDto()
    }

    // 특정 루틴 및 운동에 대한 모든 세트 조회
    fun getSetsByRoutineAndExercise(routineId: Long, exerciseId: Long): List<ExerciseSetDto> {
        return exerciseSetRepository.findByRoutineIdAndExerciseId(routineId, exerciseId)
            .map { it.toDto() }
    }

    // 특정 루틴에 대한 모든 세트 조회
    fun getSetsByRoutine(routineId: Long): List<ExerciseSetDto> {
        return exerciseSetRepository.findByRoutineId(routineId).map { it.toDto() }
    }

    // 세트 업데이트
    @Transactional
    fun updateExerciseSet(setId: Long, updatedSetDto: ExerciseSetDto): ExerciseSetDto {
        val existingSet = exerciseSetRepository.findById(setId)
            .orElseThrow { RuntimeException("Set not found") }

        existingSet.apply {
            setNumber = updatedSetDto.setNumber
            repetitions = updatedSetDto.repetitions
            weight = updatedSetDto.weight
            isCompleted = updatedSetDto.isCompleted
        }
        val updatedSet = exerciseSetRepository.save(existingSet)
        return updatedSet.toDto()
    }

    // 세트 삭제
    @Transactional
    fun deleteExerciseSet(setId: Long) {
        if (!exerciseSetRepository.existsById(setId)) {
            throw RuntimeException("Set not found")
        }
        exerciseSetRepository.deleteById(setId)
    }
}

// 확장 함수로 엔티티를 DTO로 변환
private fun ExerciseSet.toDto(): ExerciseSetDto = ExerciseSetDto(
    setId = this.setId,
    routineId = this.routineId,
    exerciseId = this.exerciseId,
    setNumber = this.setNumber,
    repetitions = this.repetitions,
    weight = this.weight,
    isCompleted = this.isCompleted
)
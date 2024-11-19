package com.example.breakApp.routine.service

import com.example.breakApp.exercise.repository.ExerciseRepository
import com.example.breakApp.routine.dto.RoutineExerciseManagerDto
import com.example.breakApp.routine.dto.toDto
import com.example.breakApp.routine.repository.RoutineExerciseManagerRepository
import com.example.breakApp.routine.repository.RoutineRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoutineExerciseManagerService(
    private val repository: RoutineExerciseManagerRepository,
    private val routineRepository: RoutineRepository,
    private val exerciseRepository: ExerciseRepository,
) {

    /**
     * 특정 루틴에 포함된 모든 운동 데이터를 조회합니다.
     * @param routineId 루틴 ID
     * @return 해당 루틴에 포함된 RoutineExerciseManager 리스트
     */
    fun getAllByRoutineId(routineId: Long): List<RoutineExerciseManagerDto> {
        val entities = repository.findByRoutineRoutineId(routineId)
        return entities.map { it.toDto() }
    }

    /**
     * 새로운 RoutineExerciseManager를 생성합니다.
     * @param managerDto 생성 요청 DTO
     * @return 생성된 RoutineExerciseManager DTO
     */
    @Transactional
    fun create(managerDto: RoutineExerciseManagerDto): RoutineExerciseManagerDto {
        // DTO에서 전달받은 routineId와 exerciseId로 엔티티를 조회
        val routine = routineRepository.findById(managerDto.routineId)
            .orElseThrow { RuntimeException("Routine not found with ID: ${managerDto.routineId}") }

        val exercise = exerciseRepository.findById(managerDto.exerciseId)
            .orElseThrow { RuntimeException("Exercise not found with ID: ${managerDto.exerciseId}") }

        // DTO를 엔티티로 변환하여 저장
        val entity = managerDto.toEntity(routine, exercise)
        return repository.save(entity).toDto()
    }

    @Transactional
    fun update(id: Long, managerDto: RoutineExerciseManagerDto): RoutineExerciseManagerDto {
        val existing = repository.findById(id).orElseThrow { RuntimeException("Manager not found with ID: $id") }

        // Routine과 Exercise는 업데이트하지 않는다고 가정
        existing.apply {
            sets = managerDto.sets
            repetitions = managerDto.repetitions
            weight = managerDto.weight ?: 10f // 기본값 처리
            restTimeSeconds = managerDto.restTimeSeconds ?: 30 // 기본값 처리
        }
        return repository.save(existing).toDto()
    }


    /**
     * 특정 RoutineExerciseManager를 삭제합니다.
     * @param id 삭제할 RoutineExerciseManager의 ID
     */
    @Transactional
    fun delete(id: Long) {
        repository.deleteById(id)
    }
}

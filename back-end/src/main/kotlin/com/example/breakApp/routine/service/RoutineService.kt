package com.example.breakApp.routine.service

import com.example.breakApp.routine.dto.CreateRoutineDto
import com.example.breakApp.routine.dto.RoutineResponseDto
import com.example.breakApp.routine.dto.RoutineExerciseResponseDto
import com.example.breakApp.routine.entity.Routine
import com.example.breakApp.routine.entity.RoutineExerciseManager
import com.example.breakApp.routine.repository.RoutineRepository
import com.example.breakApp.routine.repository.RoutineExerciseManagerRepository
import com.example.breakApp.exercise.repository.ExerciseRepository
import com.example.breakApp.member.repository.MemberRepository
import com.example.breakApp.common.authority.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoutineService(
    private val routineRepository: RoutineRepository,
    private val routineExerciseManagerRepository: RoutineExerciseManagerRepository,
    private val exerciseRepository: ExerciseRepository,
    private val memberRepository: MemberRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {

    /**
     * 새로운 루틴 생성
     * @param createRoutineDto 루틴 생성 요청 데이터
     * @param token JWT 토큰 (Bearer 접두사 제거됨)
     * @return 생성된 루틴 데이터를 반환
     */
    @Transactional
    fun createRoutine(createRoutineDto: CreateRoutineDto, token: String): RoutineResponseDto {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val member = memberRepository.findById(userId).orElseThrow { RuntimeException("User not found") }

        // 루틴 저장
        val routine = routineRepository.save(
            Routine(
                member = member,
                name = createRoutineDto.name
            )
        )

        // 루틴-운동 저장
        val exercises = createRoutineDto.exercises.map { exerciseDto ->
            val exercise = exerciseRepository.findById(exerciseDto.exerciseId)
                .orElseThrow { RuntimeException("Exercise not found") }

            RoutineExerciseManager(
                routine = routine,
                exercise = exercise,
                sets = exerciseDto.sets,
                repetitions = exerciseDto.repetitions,
                weight = exerciseDto.weight,
                restTimeSeconds = exerciseDto.restTimeSeconds
            )
        }
        routineExerciseManagerRepository.saveAll(exercises)

        return RoutineResponseDto(
            routineId = routine.routineId!!,
            userId = member.userId!!,
            name = routine.name,
            exercises = exercises.map {
                RoutineExerciseResponseDto(
                    exerciseId = it.exercise.exerciseId!!,
                    sets = it.sets,
                    repetitions = it.repetitions,
                    weight = it.weight,
                    restTimeSeconds = it.restTimeSeconds
                )
            }
        )
    }

    /**
     * 사용자의 모든 루틴 조회
     * @param token JWT 토큰 (Bearer 접두사 제거됨)
     * @return 사용자에 해당하는 루틴 리스트
     */
    fun getRoutineList(token: String): List<RoutineResponseDto> {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val member = memberRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val routineList = routineRepository.findByMember(member)

        return routineList.map { routine ->
            val exercises = routineExerciseManagerRepository.findByRoutineRoutineId(routine.routineId!!)
            RoutineResponseDto(
                routineId = routine.routineId!!,
                userId = member.userId!!,
                name = routine.name,
                exercises = exercises.map {
                    RoutineExerciseResponseDto(
                        exerciseId = it.exercise.exerciseId!!,
                        sets = it.sets,
                        repetitions = it.repetitions,
                        weight = it.weight,
                        restTimeSeconds = it.restTimeSeconds
                    )
                }
            )
        }
    }

    /**
     * 특정 루틴 조회
     * @param routineId 조회할 루틴 ID
     * @param token JWT 토큰 (Bearer 접두사 제거됨)
     * @return 조회된 루틴 데이터
     */
    fun getRoutineById(routineId: Long, token: String): RoutineResponseDto {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val member = memberRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val routine = routineRepository.findById(routineId).orElseThrow { RuntimeException("Routine not found") }

        if (routine.member != member) {
            throw RuntimeException("User not authorized to access this routine")
        }

        val exercises = routineExerciseManagerRepository.findByRoutineRoutineId(routineId)
        return RoutineResponseDto(
            routineId = routine.routineId!!,
            userId = member.userId!!,
            name = routine.name,
            exercises = exercises.map {
                RoutineExerciseResponseDto(
                    exerciseId = it.exercise.exerciseId!!,
                    sets = it.sets,
                    repetitions = it.repetitions,
                    weight = it.weight,
                    restTimeSeconds = it.restTimeSeconds
                )
            }
        )
    }

    /**
     * 루틴 업데이트
     * @param routineId 업데이트할 루틴 ID
     * @param updateDto 업데이트 요청 데이터
     * @param token JWT 토큰 (Bearer 접두사 제거됨)
     * @return 업데이트된 루틴 데이터를 반환
     */
    @Transactional
    fun updateRoutineExercises(routineId: Long, updateDto: CreateRoutineDto, token: String): RoutineResponseDto {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val member = memberRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val routine = routineRepository.findById(routineId).orElseThrow { RuntimeException("Routine not found") }

        // 사용자가 해당 루틴을 소유하고 있는지 확인
        if (routine.member != member) {
            throw RuntimeException("User not authorized to update this routine")
        }

        // **기존 운동 삭제**
        routineExerciseManagerRepository.deleteByRoutine(routine)

        // **새로운 운동 추가**
        val exercises = updateDto.exercises.map { exerciseDto ->
            val exercise = exerciseRepository.findById(exerciseDto.exerciseId)
                .orElseThrow { RuntimeException("Exercise not found") }

            RoutineExerciseManager(
                routine = routine,
                exercise = exercise,
                sets = exerciseDto.sets,
                repetitions = exerciseDto.repetitions,
                weight = exerciseDto.weight,
                restTimeSeconds = exerciseDto.restTimeSeconds
            )
        }
        routineExerciseManagerRepository.saveAll(exercises)

        // **결과 반환**
        return RoutineResponseDto(
            routineId = routine.routineId!!,
            userId = member.userId!!,
            name = routine.name,
            exercises = exercises.map {
                RoutineExerciseResponseDto(
                    exerciseId = it.exercise.exerciseId!!,
                    sets = it.sets,
                    repetitions = it.repetitions,
                    weight = it.weight,
                    restTimeSeconds = it.restTimeSeconds
                )
            }
        )
    }


    /**
     * 루틴 삭제
     * @param routineId 삭제할 루틴 ID
     * @param token JWT 토큰 (Bearer 접두사 제거됨)
     */
    @Transactional
    fun deleteRoutine(routineId: Long, token: String) {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val member = memberRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val routine = routineRepository.findById(routineId).orElseThrow { RuntimeException("Routine not found") }

        if (routine.member != member) {
            throw RuntimeException("User not authorized to delete this routine")
        }

        routineRepository.delete(routine)
    }
}
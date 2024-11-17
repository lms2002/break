package com.example.breakApp.routine.service

import com.example.breakApp.routine.dto.CreateRoutineDto
import com.example.breakApp.routine.dto.RoutineResponseDto
import com.example.breakApp.routine.entity.Routine
import com.example.breakApp.routine.repository.RoutineRepository
import com.example.breakApp.member.repository.MemberRepository
import com.example.breakApp.common.authority.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class RoutineService @Autowired constructor(
    private val routineRepository: RoutineRepository,
    private val memberRepository: MemberRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {

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
            RoutineResponseDto(
                routineId = routine.routineId!!,
                userId = member.userId!!,
                name = routine.name,
                description = routine.description
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
        // JWT 토큰에서 사용자 ID 추출
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val member = memberRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val routine = routineRepository.findById(routineId).orElseThrow { RuntimeException("Routine not found") }

        // 루틴이 해당 사용자 소유인지 확인
        if (routine.member != member) {
            throw RuntimeException("User not authorized to access this routine")
        }

        // 조회된 루틴 데이터를 DTO로 변환하여 반환
        return RoutineResponseDto(
            routineId = routine.routineId!!,
            userId = member.userId!!,
            name = routine.name,
            description = routine.description
        )
    }


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

        val routine = routineRepository.save(
            Routine(
                member = member,
                name = createRoutineDto.name,
                description = createRoutineDto.description
            )
        )

        return RoutineResponseDto(
            routineId = routine.routineId!!,
            userId = member.userId!!,
            name = routine.name,
            description = routine.description
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
    fun updateRoutine(routineId: Long, updateDto: CreateRoutineDto, token: String): RoutineResponseDto {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val member = memberRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val routine = routineRepository.findById(routineId).orElseThrow { RuntimeException("Routine not found") }

        if (routine.member != member) {
            throw RuntimeException("User not authorized to update this routine")
        }

        routine.apply {
            name = updateDto.name
            description = updateDto.description
        }
        routineRepository.save(routine)

        return RoutineResponseDto(
            routineId = routine.routineId!!,
            userId = member.userId!!,
            name = routine.name,
            description = routine.description
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
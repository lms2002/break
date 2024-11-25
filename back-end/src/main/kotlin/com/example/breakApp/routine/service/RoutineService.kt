package com.example.breakApp.routine.service

import com.example.breakApp.common.authority.JwtTokenProvider
import com.example.breakApp.member.repository.MemberRepository
import com.example.breakApp.routine.dto.RoutineDto
import com.example.breakApp.routine.entity.Routine
import com.example.breakApp.routine.repository.RoutineRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class RoutineService(
    private val routineRepository: RoutineRepository,
    private val memberRepository: MemberRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {

    // 새로운 루틴 생성
    @Transactional
    fun createRoutine(routineDto: RoutineDto, token: String): RoutineDto {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val member = memberRepository.findById(userId).orElseThrow { RuntimeException("User not found") }

        val routine = Routine(
            member = member,
            name = routineDto.name
        )
        val savedRoutine = routineRepository.save(routine)
        return savedRoutine.toDto()
    }

    // 사용자의 모든 루틴 조회
    fun getRoutineList(token: String): List<RoutineDto> {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val member = memberRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        return routineRepository.findByMember(member).map { it.toDto() }
    }

    // 특정 루틴 조회
    fun getRoutineById(routineId: Long, token: String): RoutineDto {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val member = memberRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val routine = routineRepository.findById(routineId).orElseThrow { RuntimeException("Routine not found") }

        if (routine.member != member) {
            throw RuntimeException("User not authorized to access this routine")
        }
        return routine.toDto()
    }

    // 루틴 업데이트
    @Transactional
    fun updateRoutine(routineId: Long, updatedRoutineDto: RoutineDto, token: String): RoutineDto {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val member = memberRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val routine = routineRepository.findById(routineId).orElseThrow { RuntimeException("Routine not found") }

        if (routine.member != member) {
            throw RuntimeException("User not authorized to update this routine")
        }

        routine.name = updatedRoutineDto.name
        routine.updatedAt = LocalDateTime.now()
        val updatedRoutine = routineRepository.save(routine)
        return updatedRoutine.toDto()
    }

    // 루틴 삭제
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

// 확장 함수로 엔티티를 DTO로 변환
private fun Routine.toDto(): RoutineDto = RoutineDto(
    routineId = this.routineId,
    userId = this.member.userId!!,
    name = this.name,
    createdAt = this.createdAt,
    updatedAt = this.updatedAt
)
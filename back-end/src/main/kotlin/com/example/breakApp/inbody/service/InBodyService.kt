package com.example.breakApp.inbody.service

import com.example.breakApp.common.authority.JwtTokenProvider
import com.example.breakApp.inbody.dto.CreateInBodyDto
import com.example.breakApp.inbody.dto.InBodyResponseDto
import com.example.breakApp.inbody.dto.MemberResponseDto
import com.example.breakApp.inbody.entity.InBody
import com.example.breakApp.inbody.repository.InBodyRepository
import com.example.breakApp.member.repository.MemberRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class InBodyService @Autowired constructor(
    private val inBodyRepository: InBodyRepository,
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberRepository: MemberRepository
) {
    /**
     * 인바디 데이터 생성
     * @param createInBodyDto 생성할 인바디 데이터 정보를 담은 DTO
     * @param token 사용자 인증 토큰
     * @return 생성된 인바디 데이터를 InBodyResponseDto 형태로 반환
     */
    fun createInBody(createInBodyDto: CreateInBodyDto, token: String): InBodyResponseDto {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val member = memberRepository.findById(userId)
            .orElseThrow { RuntimeException("User not found") }

        val inBody = inBodyRepository.save(
            InBody(
                member = member,
                measurementDate = createInBodyDto.measurementDate,
                age = createInBodyDto.age,
                height = createInBodyDto.height,
                weight = createInBodyDto.weight,
                bodyFatPercentage = createInBodyDto.bodyFatPercentage,
                muscleMass = createInBodyDto.muscleMass,
                bmi = createInBodyDto.bmi
            )
        )

        return InBodyResponseDto(
            inBodyId = inBody.inBodyId ?: throw IllegalStateException("inBodyId is null"),
            member = MemberResponseDto(
                userName = member.userName,
                gender = member.gender.name
            ),
            measurementDate = createInBodyDto.measurementDate,
            age = createInBodyDto.age,
            height = createInBodyDto.height,
            weight = createInBodyDto.weight,
            bodyFatPercentage = createInBodyDto.bodyFatPercentage,
            muscleMass = createInBodyDto.muscleMass,
            bmi = createInBodyDto.bmi
        )
    }
    /**
     * 특정 인바디 데이터 조회
     * @param id 조회할 인바디 데이터의 ID
     * @param token 사용자 인증 토큰
     * @return 조회된 인바디 데이터를 InBodyResponseDto 형태로 반환
     */
    fun getInBodyById(id: Long, token: String): InBodyResponseDto {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val member = memberRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val inBody = inBodyRepository.findById(id).orElseThrow { RuntimeException("InBody record not found") }

        if (inBody.member != member) {
            throw RuntimeException("User not authorized to access this record")
        }

        return InBodyResponseDto(
            inBodyId = inBody.inBodyId ?: throw IllegalStateException("inBodyId is null"),
            member = MemberResponseDto(
                userName = member.userName,
                gender = member.gender.name
            ),
            measurementDate = inBody.measurementDate,
            age = inBody.age,
            height = inBody.height,
            weight = inBody.weight,
            bodyFatPercentage = inBody.bodyFatPercentage,
            muscleMass = inBody.muscleMass,
            bmi = inBody.bmi
        )
    }


    /**
     * 모든 인바디 데이터 조회
     * @param token 사용자 인증 토큰
     * @return 사용자에 해당하는 모든 인바디 데이터를 InBodyResponseDto 리스트로 반환
     */
    fun getInBodyList(token: String): List<InBodyResponseDto> {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val member = memberRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val inBodyList = inBodyRepository.findByMember(member)

        return inBodyList.map { inBody ->
            InBodyResponseDto(
                inBodyId = inBody.inBodyId ?: throw IllegalStateException("inBodyId is null"),
                member = MemberResponseDto(
                    userName = member.userName,
                    gender = member.gender.name
                ),
                measurementDate = inBody.measurementDate,
                age = inBody.age,
                height = inBody.height,
                weight = inBody.weight,
                bodyFatPercentage = inBody.bodyFatPercentage,
                muscleMass = inBody.muscleMass,
                bmi = inBody.bmi
            )
        }
    }

    /**
     * 인바디 데이터 업데이트
     * @param id 업데이트할 인바디 데이터의 ID
     * @param updateDto 업데이트할 내용을 담은 DTO
     * @param token 사용자 인증 토큰
     * @return 업데이트된 인바디 데이터를 InBodyResponseDto 형태로 반환
     */
    fun updateInBody(id: Long, updateDto: CreateInBodyDto, token: String): InBodyResponseDto {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val member = memberRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val inBody = inBodyRepository.findById(id).orElseThrow { RuntimeException("InBody record not found") }

        if (inBody.member != member) {
            throw RuntimeException("User not authorized to update this record")
        }

        inBody.apply {
            measurementDate = updateDto.measurementDate
            age = updateDto.age
            height = updateDto.height
            weight = updateDto.weight
            bodyFatPercentage = updateDto.bodyFatPercentage
            muscleMass = updateDto.muscleMass
            bmi = updateDto.bmi
        }
        inBodyRepository.save(inBody)

        return InBodyResponseDto(
            inBodyId = inBody.inBodyId!!,
            member = MemberResponseDto(
                userName = member.userName,
                gender = member.gender.name
            ),
            measurementDate = inBody.measurementDate,
            age = inBody.age,
            height = inBody.height,
            weight = inBody.weight,
            bodyFatPercentage = inBody.bodyFatPercentage,
            muscleMass = inBody.muscleMass,
            bmi = inBody.bmi,
        )
    }

    /**
     * 인바디 데이터 삭제
     * @param id 삭제할 인바디 데이터의 ID
     * @param token 사용자 인증 토큰
     * 사용자가 소유한 인바디 데이터를 삭제하며, 삭제 권한이 없는 경우 예외를 발생
     */
    fun deleteInBody(id: Long, token: String) {
        val userId = jwtTokenProvider.getUserIdFromToken(token)
        val member = memberRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val inBody = inBodyRepository.findById(id).orElseThrow { RuntimeException("InBody record not found") }

        if (inBody.member != member) {
            throw RuntimeException("User not authorized to delete this record")
        }

        inBodyRepository.delete(inBody)
    }
}
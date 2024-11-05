package com.example.breakApp.inbody.controller

import com.example.breakApp.inbody.dto.CreateInBodyDto
import com.example.breakApp.inbody.dto.InBodyResponseDto
import com.example.breakApp.inbody.dto.MemberIdDto
import com.example.breakApp.inbody.entity.InBody
import com.example.breakApp.inbody.service.InBodyService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/inbody")
class InBodyController(private val inBodyService: InBodyService) {
    @PostMapping // 인바디 데이터 생성
    fun createInBody(@RequestBody createInBodyDto: CreateInBodyDto): ResponseEntity<InBody> {
        // 사용자 ID로 Member 객체 조회
        val member = inBodyService.getMemberById(createInBodyDto.userId)
        return if (member != null) {
            // 인바디 데이터 생성
            val inBody = InBody(
                member = member, // 조회된 회원 객체 설정
                measurementDate = createInBodyDto.measurementDate, // DTO에서 측정 날짜 설정
                weight = createInBodyDto.weight, // DTO에서 체중 설정
                bodyFatPercentage = createInBodyDto.bodyFatPercentage, // DTO에서 체지방률 설정
                muscleMass = createInBodyDto.muscleMass // DTO에서 근육량 설정
            )
            val createdInBody = inBodyService.createInBody(inBody)
            ResponseEntity(createdInBody, HttpStatus.CREATED) // 생성된 데이터와 함께 201 상태 코드 반환
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND) // 사용자가 없을 경우 404 상태 코드 반환
        }
    }

    @GetMapping("/user") // 특정 사용자의 인바디 데이터 조회
    fun getInBodyByUserId(@RequestBody memberIdDto: MemberIdDto): ResponseEntity<List<InBodyResponseDto>> {
        val member = inBodyService.getMemberById(memberIdDto.userId) // 사용자 ID로 Member 객체 조회
        return if (member != null) {
            val inBodyList = inBodyService.getInBodyByMember(member) // 인바디 데이터 조회
            // InBodyResponseDto로 변환
            val responseList = inBodyList.map { inBody ->
                InBodyResponseDto(
                    inBodyId = inBody.inBodyId,
                    measurementDate = inBody.measurementDate,
                    weight = inBody.weight,
                    bodyFatPercentage = inBody.bodyFatPercentage,
                    muscleMass = inBody.muscleMass,
                    bmi = inBody.bmi,  // 추가된 필드
                    visceralFatLevel = inBody.visceralFatLevel,
                    basalMetabolicRate = inBody.basalMetabolicRate
                )
            }
            ResponseEntity(responseList, HttpStatus.OK) // 조회된 데이터와 함께 200 상태 코드 반환
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND) // 사용자가 없을 경우 404 상태 코드 반환
        }
    }

    @PutMapping // 인바디 데이터 수정
    fun updateInBody(@RequestBody inBody: InBody): ResponseEntity<InBody> {
        val updatedInBody = inBodyService.updateInBody(inBody)
        return ResponseEntity(updatedInBody, HttpStatus.OK) // 수정된 데이터와 함께 200 상태 코드 반환
    }

    @DeleteMapping("/{inBodyId}") // 인바디 데이터 삭제
    fun deleteInBody(@PathVariable inBodyId: Long): ResponseEntity<Void> {
        inBodyService.deleteInBody(inBodyId)
        return ResponseEntity(HttpStatus.NO_CONTENT) // 삭제 완료 후 204 상태 코드 반환
    }
}

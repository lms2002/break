package com.example.breakApp.inbody.controller

import com.example.breakApp.common.dto.BaseResponse
import com.example.breakApp.common.status.ResultCode
import com.example.breakApp.inbody.dto.CreateInBodyDto
import com.example.breakApp.inbody.dto.InBodyResponseDto
import com.example.breakApp.inbody.service.InBodyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/inbody")
class InBodyController @Autowired constructor(
    private val inBodyService: InBodyService
) {
    /**
     * 새로운 인바디 데이터를 생성합니다.
     * @param createInBodyDto 생성할 인바디 데이터 정보를 담은 DTO
     * @param token 인증 토큰 (Bearer 접두사를 포함해야 함)
     * @return 생성된 인바디 데이터를 BaseResponse 형식으로 반환
     */
    @PostMapping
    fun createInBody(
        @RequestBody createInBodyDto: CreateInBodyDto,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<BaseResponse<InBodyResponseDto>> {
        val inBodyResponse = inBodyService.createInBody(createInBodyDto, token.removePrefix("Bearer "))
        return ResponseEntity.ok(
            BaseResponse(
                resultCode = ResultCode.INBODY_CREATED.name,
                data = inBodyResponse,
                message = ResultCode.INBODY_CREATED.msg
            )
        )
    }

    /**
     * 특정 인바디 데이터를 ID로 조회합니다.
     * @param id 조회할 인바디 데이터의 ID
     * @param token 인증 토큰 (Bearer 접두사를 포함해야 함)
     * @return 조회된 인바디 데이터를 BaseResponse 형식으로 반환
     */
    @GetMapping("/{id}")
    fun getInBodyById(
        @PathVariable id: Long,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<BaseResponse<InBodyResponseDto>> {
        val inBodyResponse = inBodyService.getInBodyById(id, token.removePrefix("Bearer "))
        return ResponseEntity.ok(
            BaseResponse(
                resultCode = ResultCode.INBODY_RETRIEVED.name,
                data = inBodyResponse,
                message = ResultCode.INBODY_RETRIEVED.msg
            )
        )
    }

    /**
     * 사용자와 연관된 모든 인바디 데이터를 조회합니다.
     * @param token 인증 토큰 (Bearer 접두사를 포함해야 함)
     * @return 모든 인바디 데이터를 BaseResponse 형식으로 반환
     */
    @GetMapping
    fun getInBodyList(@RequestHeader("Authorization") token: String): ResponseEntity<BaseResponse<List<InBodyResponseDto>>> {
        val inBodyResponseList = inBodyService.getInBodyList(token.removePrefix("Bearer "))
        return ResponseEntity.ok(BaseResponse(ResultCode.INBODY_RETRIEVED.name, inBodyResponseList, ResultCode.INBODY_RETRIEVED.msg))
    }

    /**
     * 특정 인바디 데이터를 업데이트합니다.
     * @param id 업데이트할 인바디 데이터의 ID
     * @param updateDto 업데이트할 데이터를 담은 DTO
     * @param token 인증 토큰 (Bearer 접두사를 포함해야 함)
     * @return 업데이트된 인바디 데이터를 BaseResponse 형식으로 반환
     */
    @PutMapping("/{id}")
    fun updateInBody(
        @PathVariable id: Long,
        @RequestBody updateDto: CreateInBodyDto,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<BaseResponse<InBodyResponseDto>> {
        val updatedInBodyResponse = inBodyService.updateInBody(id, updateDto, token.removePrefix("Bearer "))
        return ResponseEntity.ok(BaseResponse(ResultCode.INBODY_UPDATED.name, updatedInBodyResponse, ResultCode.INBODY_UPDATED.msg))
    }

    /**
     * 특정 인바디 데이터를 삭제합니다.
     * @param id 삭제할 인바디 데이터의 ID
     * @param token 인증 토큰 (Bearer 접두사를 포함해야 함)
     * @return 성공적으로 삭제된 경우 결과를 BaseResponse 형식으로 반환
     */
    @DeleteMapping("/{id}")
    fun deleteInBody(@PathVariable id: Long, @RequestHeader("Authorization") token: String): ResponseEntity<BaseResponse<Unit>> {
        inBodyService.deleteInBody(id, token.removePrefix("Bearer "))
        return ResponseEntity.ok(BaseResponse(ResultCode.INBODY_DELETED.name, null, ResultCode.INBODY_DELETED.msg))
    }
}
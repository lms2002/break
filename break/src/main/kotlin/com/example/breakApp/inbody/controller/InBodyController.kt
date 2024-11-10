package com.example.breakApp.inbody.controller

import com.example.breakApp.common.dto.BaseResponse
import com.example.breakApp.common.status.ResultCode
import com.example.breakApp.inbody.dto.CreateInBodyDto
import com.example.breakApp.inbody.dto.InBodyResponseDto
import com.example.breakApp.inbody.entity.InBody
import com.example.breakApp.inbody.service.InBodyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/inbody")
class InBodyController @Autowired constructor(
    private val inBodyService: InBodyService
) {
    @PostMapping("/create")
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

    @GetMapping("/list")
    fun getInBodyList(@RequestHeader("Authorization") token: String): ResponseEntity<BaseResponse<List<InBodyResponseDto>>> {
        val inBodyResponseList = inBodyService.getInBodyList(token.removePrefix("Bearer "))
        return ResponseEntity.ok(BaseResponse(ResultCode.INBODY_RETRIEVED.name, inBodyResponseList, ResultCode.INBODY_RETRIEVED.msg))
    }

    @PutMapping("/update/{id}")
    fun updateInBody(
        @PathVariable id: Long,
        @RequestBody updateDto: CreateInBodyDto,
        @RequestHeader("Authorization") token: String
    ): ResponseEntity<BaseResponse<InBodyResponseDto>> {
        val updatedInBodyResponse = inBodyService.updateInBody(id, updateDto, token.removePrefix("Bearer "))
        return ResponseEntity.ok(BaseResponse(ResultCode.INBODY_UPDATED.name, updatedInBodyResponse, ResultCode.INBODY_UPDATED.msg))
    }

    @DeleteMapping("/delete/{id}")
    fun deleteInBody(@PathVariable id: Long, @RequestHeader("Authorization") token: String): ResponseEntity<BaseResponse<Unit>> {
        inBodyService.deleteInBody(id, token.removePrefix("Bearer "))
        return ResponseEntity.ok(BaseResponse(ResultCode.INBODY_DELETED.name, null, ResultCode.INBODY_DELETED.msg))
    }
}

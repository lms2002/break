package com.example.breakApp.exercise.service

import com.example.breakApp.exercise.repository.ExerciseRepository
import com.example.breakApp.exercise.repository.TargetBodyPartRepository
import com.example.breakApp.exercise.repository.EquipmentRepository
import com.example.breakApp.exercise.dto.ExerciseDto
import com.example.breakApp.exercise.entity.Exercise
import com.example.breakApp.exercise.entity.TargetBodyPart
import com.example.breakApp.exercise.entity.Equipment
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.beans.factory.annotation.Value

@Service
class ExerciseService(
    private val exerciseRepository: ExerciseRepository,
    private val targetBodyPartRepository: TargetBodyPartRepository,
    private val equipmentRepository: EquipmentRepository
) {

    @Value("\${rapidapi.key}")
    private lateinit var apiKey: String

    private val apiUrl = "https://exercisedb.p.rapidapi.com/exercises"

    @Transactional  // 트랜잭션 처리
    fun fetchAndSaveExercises() {
        val restTemplate = RestTemplate()

        // 헤더 설정 (API Key는 환경변수에서 가져옵니다)
        val headers = HttpHeaders().apply {
            set("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            set("X-RapidAPI-Key", apiKey)  // 환경변수에서 API 키 불러오기
        }

        val entity = HttpEntity<String>(headers)

        // API 호출
        val response: ResponseEntity<Array<ExerciseDto>> = restTemplate.exchange(
            apiUrl,
            HttpMethod.GET,
            entity,
            Array<ExerciseDto>::class.java
        )

        response.body?.forEach { dto ->

            // 1. 타겟 부위 저장 또는 기존 데이터 참조
            val targetBodyPart = targetBodyPartRepository.findByNameIgnoreCase(dto.targetBodyPart)
                ?: targetBodyPartRepository.save(TargetBodyPart(name = dto.targetBodyPart))

            // 2. 장비 저장 또는 기존 데이터 참조
            val equipment = equipmentRepository.findByNameIgnoreCase(dto.equipment)
                ?: equipmentRepository.save(Equipment(name = dto.equipment))

            // 3. 운동 데이터 저장
            val exercise = Exercise(
                name = dto.name,
                targetBodyPart = targetBodyPart,  // 외래 키 연결
                equipment = equipment,  // 외래 키 연결
                gifUrl = dto.gifUrl ?: "",  // null 체크
                category = dto.category,
                description = dto.description ?: ""  // null 체크
            )

            exerciseRepository.save(exercise)
        }
    }
}
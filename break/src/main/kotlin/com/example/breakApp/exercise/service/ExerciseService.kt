package com.example.breakApp.exercise.service

import com.example.breakApp.exercise.dto.ExerciseDto
import com.example.breakApp.exercise.entity.Exercise
import com.example.breakApp.exercise.repository.ExerciseRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity

@Service
class ExerciseService(
    private val exerciseRepository: ExerciseRepository
) {

    private val apiUrl = "https://exercisedb.p.rapidapi.com/exercises"  // API URL

    @Transactional
    fun fetchAndSaveExercises() {
        val restTemplate = RestTemplate()

        // API 호출에 필요한 헤더 설정
        val headers = HttpHeaders().apply {
            set("X-RapidAPI-Host", "exercisedb.p.rapidapi.com")
            set("X-RapidAPI-Key", "e81c4d92ccmshf84508c82b45c37p134fe6jsn795e29a4eca3")  // 발급받은 API 키 사용
        }

        val entity = HttpEntity<String>(headers)

        // API 호출
        val response: ResponseEntity<Array<ExerciseDto>> = restTemplate.exchange(
            apiUrl,
            HttpMethod.GET,
            entity,
            Array<ExerciseDto>::class.java
        )

        // API 응답 처리 및 데이터 저장
        response.body?.forEach { dto ->
            if (exerciseRepository.findByName(dto.name) == null) {
                val exercise = Exercise(
                    name = dto.name,
                    description = dto.description ?: "No description available",
                    category = dto.category,
                    targetArea = dto.targetArea
                )
                exerciseRepository.save(exercise)
            }
        }
    }
}
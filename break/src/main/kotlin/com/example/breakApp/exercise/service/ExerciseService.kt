package com.example.breakApp.exercise.service

import com.example.breakApp.exercise.dto.ExerciseDto
import com.example.breakApp.exercise.entity.Exercise
import com.example.breakApp.exercise.repository.ExerciseRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.annotation.PostConstruct

@Service
class ExerciseService(
    private val exerciseRepository: ExerciseRepository
) {

    private val apiUrl = "https://exercisedb.p.rapidapi.com/exercises"
    private val apiKey = "e81c4d92ccmshf84508c82b45c37p134fe6jsn795e29a4eca3" // 실제 API 키를 입력하세요
    private val client = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()

    @PostConstruct
    @Transactional
    fun fetchAndSaveExercises() {
        try {
            // 요청 설정
            val request = Request.Builder()
                .url(apiUrl)
                .get()
                .addHeader("x-rapidapi-key", apiKey)
                .addHeader("x-rapidapi-host", "exercisedb.p.rapidapi.com")
                .build()

            // API 호출
            val response = client.newCall(request).execute()
            val responseData = response.body?.string()

            // 응답 출력
            println("API 응답: $responseData")

            // JSON 응답을 DTO 리스트로 변환
            if (!responseData.isNullOrEmpty()) {
                val exercises: List<ExerciseDto> = objectMapper.readValue(responseData)
                exercises.forEach { dto ->
                    // 데이터베이스에 저장하기
                    if (exerciseRepository.findByName(dto.name) == null) {
                        val exercise = Exercise(
                            name = dto.name,
                            description = dto.description ?: "No description available",
                            category = dto.category ?: "Uncategorized",
                            targetArea = dto.targetArea ?: "Unknown"
                        )
                        exerciseRepository.save(exercise)
                        println("Saved exercise: ${dto.name}")
                    }
                }
            }
        } catch (e: Exception) {
            println("API 호출 중 오류 발생: ${e.message}")
        }
    }
}

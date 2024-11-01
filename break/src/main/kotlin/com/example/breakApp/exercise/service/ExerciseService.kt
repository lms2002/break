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
    private val apiKey = "e81c4d92ccmshf84508c82b45c37p134fe6jsn795e29a4eca3"
    private val client = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()

    @PostConstruct
    @Transactional
    fun fetchAndSaveExercises() {
        // 모든 기존 데이터 삭제
        exerciseRepository.deleteAll()

        try {
            val request = Request.Builder()
                .url(apiUrl)
                .get()
                .addHeader("x-rapidapi-key", apiKey)
                .addHeader("x-rapidapi-host", "exercisedb.p.rapidapi.com")
                .build()

            val response = client.newCall(request).execute()
            val responseData = response.body?.string()

            if (!responseData.isNullOrEmpty()) {
                val exercises: List<ExerciseDto> = objectMapper.readValue(responseData)
                exercises.forEach { dto ->
                    val exercise = Exercise(
                        name = dto.name,
                        description = dto.instructions?.joinToString(" ") ?: "No description available",
                        category = dto.category,
                        targetArea = dto.targetArea
                    )
                    exerciseRepository.save(exercise)
                    println("Saved exercise: ${dto.name}, Description: ${exercise.description}")
                }
            }
        } catch (e: Exception) {
            println("API 호출 중 오류 발생: ${e.message}")
        }
    }
}

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
import org.slf4j.LoggerFactory

@Service
class ExerciseService(
    private val exerciseRepository: ExerciseRepository
) {
    private val apiUrl = "https://exercisedb.p.rapidapi.com/exercises" // 전체 데이터를 가져오기 위한 기본 URL
    private val apiKey = "e81c4d92ccmshf84508c82b45c37p134fe6jsn795e29a4eca3"  // API 키를 본인의 키로 교체
    private val client = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()
    private val logger = LoggerFactory.getLogger(ExerciseService::class.java)

    @PostConstruct
    @Transactional
    fun fetchAndSaveExercises() {
        // 기존 데이터를 삭제하여 중복 방지
        exerciseRepository.deleteAll()
        logger.info("기존 데이터가 삭제되었습니다.")

        try {
            // 전체 데이터를 가져오기 위한 API 요청 생성
            val request = Request.Builder()
                .url(apiUrl)  // 기본 URL을 그대로 사용하여 제한 없이 전체 데이터 요청
                .get()
                .addHeader("x-rapidapi-key", apiKey)
                .addHeader("x-rapidapi-host", "exercisedb.p.rapidapi.com")
                .build()

            val response = client.newCall(request).execute()
            val responseData = response.body?.string()

            if (!responseData.isNullOrEmpty()) {
                // JSON 응답을 파싱하여 ExerciseDto 리스트로 변환
                val exercises: List<ExerciseDto> = objectMapper.readValue(responseData)

                exercises.forEach { dto ->
                    // 데이터 그대로 저장
                    val exercise = Exercise(
                        name = dto.name,
                        description = dto.instructions?.joinToString(" ") ?: "No description available",
                        category = dto.category ?: "General",
                        targetArea = dto.targetArea  // 기본값 제거, null 값 허용
                    )
                    exerciseRepository.save(exercise)
                    logger.info("Saved exercise: ${exercise.name}, TargetArea: ${exercise.targetArea}")
                }
                logger.info("총 ${exercises.size}개의 운동 데이터가 저장되었습니다.")
            } else {
                logger.warn("API 응답이 비어 있습니다.")
            }
        } catch (e: Exception) {
            logger.error("API 호출 중 오류 발생: ${e.message}")
        }
    }
}
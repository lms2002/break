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
    private val apiUrl = "https://exercisedb.p.rapidapi.com/exercises?offset=0&limit=1350"
    private val apiKey = "e81c4d92ccmshf84508c82b45c37p134fe6jsn795e29a4eca3"
    private val client = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()
    private val logger = LoggerFactory.getLogger(ExerciseService::class.java)

    // 대분류 분류 맵
    private val bodyPartMap = mapOf(
        "가슴" to listOf("pectorals"),
        "등" to listOf("lats", "traps", "upper back"),
        "어깨" to listOf("delts", "serratus anterior"),
        "삼두" to listOf("triceps"),
        "이두" to listOf("biceps"),
        "하체" to listOf("abductors", "adductors", "calves", "glutes", "hamstrings", "quads", "spine"),
        "전신" to listOf("abs", "forearms"),
        "유산소" to listOf("cardiovascular system")
    )

    // targetArea를 대분류로 변환하는 함수
    private fun classifyExercise(targetArea: String): String {
        return bodyPartMap.entries.firstOrNull { (_, areas) -> targetArea in areas }?.key ?: "기타"
    }

    @PostConstruct
    @Transactional
    fun fetchAndSaveExercises() {
        exerciseRepository.deleteAll()
        logger.info("기존 데이터가 삭제되었습니다.")

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
                    val mainCategory = classifyExercise(dto.targetArea)  // 대분류 적용
                    val exercise = Exercise(
                        name = dto.name,
                        description = dto.instructions?.joinToString(" ") ?: "No description available",
                        category = mainCategory,  // 대분류로 저장
                        targetArea = dto.targetArea
                    )
                    exerciseRepository.save(exercise)
                    logger.info("Saved exercise: ${exercise.name}, MainCategory: $mainCategory, TargetArea: ${exercise.targetArea}")
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

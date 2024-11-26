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
    // 외부 API URL 및 API 키 설정
    private val apiUrl = "https://exercisedb.p.rapidapi.com/exercises?offset=0&limit=1350"
    private val apiKey = "e81c4d92ccmshf84508c82b45c37p134fe6jsn795e29a4eca3"

    // HTTP 요청을 위한 OkHttpClient와 JSON 파싱을 위한 ObjectMapper 설정
    private val client = OkHttpClient()
    private val objectMapper = jacksonObjectMapper()
    private val logger = LoggerFactory.getLogger(ExerciseService::class.java)

    // 대분류 맵: 타겟 부위에 따라 운동의 대분류를 매핑하는 Map
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
        // targetArea가 포함된 대분류를 찾아 반환하고, 없으면 "기타"로 설정
        return bodyPartMap.entries.firstOrNull { (_, areas) -> targetArea in areas }?.key ?: "기타"
    }

    // 데이터베이스에 운동 데이터를 가져와 저장하는 함수
    @PostConstruct
    @Transactional
    fun fetchAndSaveExercises() {
        // 기존 데이터 삭제 후 새로운 데이터로 업데이트
        exerciseRepository.deleteAll()
        logger.info("기존 데이터가 삭제되었습니다.")

        try {
            // API 요청 생성
            val request = Request.Builder()
                .url(apiUrl)
                .get()
                .addHeader("x-rapidapi-key", apiKey)
                .addHeader("x-rapidapi-host", "exercisedb.p.rapidapi.com")
                .build()

            // API 응답 처리
            val response = client.newCall(request).execute()
            val responseData = response.body?.string()

            if (!responseData.isNullOrEmpty()) {
                // JSON 데이터를 List<ExerciseDto>로 변환
                val exercises: List<ExerciseDto> = objectMapper.readValue(responseData)

                exercises.forEach { dto ->
                    val mainCategory = classifyExercise(dto.target)  // 대분류 적용
                    val exercise = Exercise(
                        name = dto.name,
                        instructions = dto.instructions?.joinToString(" ") ?: "No description available",
                        target = mainCategory,  // 대분류로 저장
                        gifUrl = dto.gifUrl, // GIF URL 저장
                        bodyPart = dto.bodyPart,
                        equipment = dto.equipment
                    )
                    // 변환한 Exercise 엔티티를 데이터베이스에 저장
                    exerciseRepository.save(exercise)
                    logger.info("Saved exercise: \${exercise.name}, MainCategory: \$mainCategory, TargetArea: \${exercise.targetArea}, GIF URL: \${exercise.gifUrl}")
                }
                logger.info("총 \${exercises.size}개의 운동 데이터가 저장되었습니다.")
            } else {
                logger.warn("API 응답이 비어 있습니다.")
            }
        } catch (e: Exception) {
            // 오류 발생 시 로그 출력
            logger.error("API 호출 중 오류 발생: \${e.message}",e)
        }
    }
}

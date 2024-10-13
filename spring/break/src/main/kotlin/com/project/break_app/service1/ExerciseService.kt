package com.project.break_app.service1

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ExerciseService {
    fun getExercises(): String {
        val url = "https://wger.de/api/v2/exercise"
        val restTemplate = RestTemplate()
        val response = restTemplate.getForEntity(url, String::class.java)
        return response.body ?: ""
    }
}
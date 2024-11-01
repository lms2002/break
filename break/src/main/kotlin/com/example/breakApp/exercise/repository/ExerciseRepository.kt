package com.example.breakApp.exercise.repository

import com.example.breakApp.exercise.entity.Exercise
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExerciseRepository : JpaRepository<Exercise, Long> {
    // 운동 이름으로 조회하는 메서드
    fun findByName(name: String): Exercise?
}

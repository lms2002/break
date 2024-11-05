package com.example.breakApp.exercise.repository

import com.example.breakApp.exercise.entity.Exercise
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExerciseRepository : JpaRepository<Exercise, Long> {
    // 이름으로 운동 찾기 (중복 방지를 위한 용도)
    fun findByName(name: String): Exercise?

    // 카테고리별로 운동 목록 가져오기
    fun findByCategory(category: String): List<Exercise>

    // 타겟 부위별로 운동 목록 가져오기
    fun findByTargetArea(targetArea: String): List<Exercise>

    // 이름이 포함된 운동 목록 검색 (부분 일치 검색)
    fun findByNameContainingIgnoreCase(name: String): List<Exercise>
}

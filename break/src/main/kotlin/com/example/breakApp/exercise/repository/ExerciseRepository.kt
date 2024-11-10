package com.example.breakApp.exercise.repository

import com.example.breakApp.exercise.entity.Exercise
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ExerciseRepository : JpaRepository<Exercise, Long> {
    // @Repository: 이 인터페이스가 JPA 리포지토리로서 Spring에 의해 관리됨을 나타냅니다.
    // JpaRepository<Exercise, Long>: Exercise 엔티티에 대한 CRUD 및 JPA 기능을 자동으로 제공합니다. Exercise 엔티티의 기본 키 타입은 Long입니다.

    // 이름으로 운동 찾기 (중복 방지를 위한 용도)
    fun findByName(name: String): Exercise?
    // findByName: 이름이 정확히 일치하는 운동 데이터를 반환합니다. 이름을 기반으로 한 중복 방지나 조회에 유용합니다.
    // 반환 타입은 Exercise?로, 이름이 일치하는 운동이 없으면 null을 반환합니다.

    // 카테고리별로 운동 목록 가져오기
    fun findByCategory(category: String): List<Exercise>
    // findByCategory: 주어진 카테고리에 속하는 모든 운동 데이터를 리스트 형태로 반환합니다.

    // 타겟 부위별로 운동 목록 가져오기
    fun findByTargetArea(targetArea: String): List<Exercise>
    // findByTargetArea: 특정 타겟 부위에 해당하는 운동 데이터를 리스트로 반환합니다.

    // 이름이 포함된 운동 목록 검색 (부분 일치 검색)
    fun findByNameContainingIgnoreCase(name: String): List<Exercise>
    // findByNameContainingIgnoreCase: 부분 일치 검색을 통해 특정 문자열이 포함된 운동 데이터를 조회합니다.
    // 대소문자를 구분하지 않고 검색하여 유연하게 이름으로 운동을 찾을 수 있습니다.
}

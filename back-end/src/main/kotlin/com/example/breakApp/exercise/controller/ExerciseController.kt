package com.example.breakApp.exercise.controller

// 컨트롤러의 패키지 위치를 지정합니다. 여기서는 com.example.breakApp.exercise.controller 패키지에 속합니다.

import com.example.breakApp.exercise.entity.Exercise
import com.example.breakApp.exercise.repository.ExerciseRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// Spring Boot에서 사용되는 기본적인 패키지와 클래스를 import 합니다.
// Exercise 엔티티와 ExerciseRepository 인터페이스를 포함하여 HTTP 상태 코드와 응답 형식(ResponseEntity)도 import 합니다.

@RestController
@RequestMapping("/api/exercises")
class ExerciseController(
    private val exerciseRepository: ExerciseRepository
) {

    // @RestController: 이 클래스가 REST API의 컨트롤러임을 나타냅니다.
    // @RequestMapping("/api/exercises"): 이 컨트롤러의 기본 URL 경로를 설정합니다. /api/exercises로 시작하는 모든 요청이 이 컨트롤러로 매핑됩니다.
    // ExerciseController 클래스는 ExerciseRepository 인터페이스의 인스턴스를 생성자 주입 방식으로 사용합니다.

    @GetMapping
    fun getAllExercises(): List<Exercise> {
        // @GetMapping: HTTP GET 요청을 처리하며, /api/exercises 엔드포인트에 매핑됩니다.
        // getAllExercises 메서드는 모든 운동(Exercise) 데이터를 리스트 형태로 반환합니다.
        return exerciseRepository.findAll()
    }

    @GetMapping("/{name}")
    fun getExerciseByName(@PathVariable name: String): ResponseEntity<Exercise> {
        // @GetMapping("/{name}"): URL에 /{name}이라는 변수를 포함하여 매핑되며, 특정 이름을 기반으로 운동 데이터를 조회합니다.
        // @PathVariable: URL 경로의 변수 {name} 값을 메서드의 매개변수 name에 바인딩합니다.
        // findByName 메서드를 통해 특정 이름의 운동 데이터를 조회하고, 있으면 해당 데이터를 반환하고, 없으면 NOT_FOUND 상태 코드를 반환합니다.
        val exercise = exerciseRepository.findByName(name)
        return if (exercise != null) {
            ResponseEntity.ok(exercise) // 조회 성공 시 HTTP 200 상태 코드와 함께 데이터 반환
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).build() // 조회 실패 시 HTTP 404 상태 코드 반환
        }
    }

    @GetMapping("/category/{category}")
    fun getExercisesByCategory(@PathVariable bodyPart: String): List<Exercise> {
        // @GetMapping("/category/{category}"): URL에 /category/{category}를 포함하여 매핑되며, 특정 카테고리의 운동 데이터를 조회합니다.
        // @PathVariable: URL 경로의 변수 {category} 값을 메서드의 매개변수 category에 바인딩합니다.
        // findByCategory 메서드를 통해 특정 카테고리에 속하는 운동 데이터를 리스트로 반환합니다.
        return exerciseRepository.findBybodyPart(bodyPart)
    }

    @GetMapping("/target/{targetArea}")
    fun getExercisesByTargetArea(@PathVariable target: String): List<Exercise> {
        // @GetMapping("/target/{targetArea}"): URL에 /target/{targetArea}를 포함하여 매핑되며, 특정 타겟 부위의 운동 데이터를 조회합니다.
        // @PathVariable: URL 경로의 변수 {targetArea} 값을 메서드의 매개변수 targetArea에 바인딩합니다.
        // findByTargetArea 메서드를 통해 특정 타겟 부위에 해당하는 운동 데이터를 리스트로 반환합니다.
        return exerciseRepository.findBytarget(target)
    }
}

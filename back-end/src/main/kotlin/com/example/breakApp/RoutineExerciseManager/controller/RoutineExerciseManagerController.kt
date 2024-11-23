package com.example.breakApp.RoutineExerciseManager.controller

import com.example.breakApp.RoutineExerciseManager.dto.RoutineExerciseManagerDto
import com.example.breakApp.RoutineExerciseManager.service.RoutineExerciseManagerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/routine-exercise")
class RoutineExerciseManagerController(
    private val routineExerciseManagerService: RoutineExerciseManagerService
) {

    // 루틴에 운동 추가
    @PostMapping
    fun addExerciseToRoutine(@RequestBody routineExerciseManagerDto: RoutineExerciseManagerDto): ResponseEntity<RoutineExerciseManagerDto> {
        val createdRelation = routineExerciseManagerService.addExerciseToRoutine(routineExerciseManagerDto)
        return ResponseEntity.ok(createdRelation)
    }

    // 루틴의 모든 운동 조회
    @GetMapping("/routine/{routineId}")
    fun getExercisesByRoutine(@PathVariable routineId: Long): ResponseEntity<List<RoutineExerciseManagerDto>> {
        val exercises = routineExerciseManagerService.getExercisesByRoutine(routineId)
        return ResponseEntity.ok(exercises)
    }

    // 루틴에서 특정 운동 삭제
    @DeleteMapping("/routine/{routineId}/exercise/{exerciseId}")
    fun removeExerciseFromRoutine(
        @PathVariable routineId: Long,
        @PathVariable exerciseId: Long
    ): ResponseEntity<Unit> {
        routineExerciseManagerService.removeExerciseFromRoutine(routineId, exerciseId)
        return ResponseEntity.noContent().build()
    }
}
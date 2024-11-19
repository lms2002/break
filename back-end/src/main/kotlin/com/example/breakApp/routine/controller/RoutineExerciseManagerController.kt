package com.example.breakApp.routine.controller

import com.example.breakApp.routine.dto.RoutineExerciseManagerDto
import com.example.breakApp.routine.service.RoutineExerciseManagerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/routines/exercises")
class RoutineExerciseManagerController(
    private val service: RoutineExerciseManagerService
) {

    /**
     * 특정 루틴에 포함된 모든 운동 데이터를 조회합니다.
     * @param routineId 루틴 ID
     * @return 해당 루틴의 RoutineExerciseManager 리스트
     */
    @GetMapping("/{routineId}")
    fun getAllByRoutineId(@PathVariable routineId: Long): ResponseEntity<List<RoutineExerciseManagerDto>> {
        val exercises = service.getAllByRoutineId(routineId)
        return ResponseEntity.ok(exercises)
    }

    /**
     * 새로운 RoutineExerciseManager를 생성합니다.
     * @param managerDto 생성 요청 DTO
     * @return 생성된 RoutineExerciseManager DTO
     */
    @PostMapping
    fun create(@RequestBody managerDto: RoutineExerciseManagerDto): ResponseEntity<RoutineExerciseManagerDto> {
        val created = service.create(managerDto)
        return ResponseEntity.ok(created)
    }

    /**
     * 기존 RoutineExerciseManager를 수정합니다.
     * @param id 수정할 RoutineExerciseManager의 ID
     * @param managerDto 수정 요청 DTO
     * @return 수정된 RoutineExerciseManager DTO
     */
    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody managerDto: RoutineExerciseManagerDto
    ): ResponseEntity<RoutineExerciseManagerDto> {
        val updated = service.update(id, managerDto)
        return ResponseEntity.ok(updated)
    }

    /**
     * 특정 RoutineExerciseManager를 삭제합니다.
     * @param id 삭제할 RoutineExerciseManager의 ID
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}

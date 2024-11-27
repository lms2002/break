package com.example.breakApp.workoutlog.repository

import com.example.breakApp.member.entity.Member
import com.example.breakApp.workoutlog.entity.WorkoutLog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime


@Repository
interface WorkoutLogRepository : JpaRepository<WorkoutLog, Long> {
    fun findByMemberUserId(userId: Long): List<WorkoutLog>
    // 사용자의 첫 번째 운동 시작 날짜 조회
    @Query("SELECT MIN(w.startTime) FROM WorkoutLog w WHERE w.member = :member")
    fun findFirstWorkoutDate(member: Member): LocalDateTime?
    // 주어진 사용자(userId)가 주어진 날짜(weekStart) 이후에 완료한 운동 로그의 개수를 반환하는 쿼리
    @Query("SELECT COUNT(w) FROM WorkoutLog w WHERE w.member.userId = :userId AND w.endTime >= :weekStart")
    fun countCompletedWorkoutsForUser(@Param("userId") userId: Long, @Param("weekStart") weekStart: LocalDateTime): Int
}
package com.example.breakApp.jetpack.subscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.api.model.CompletedWorkoutDto
import com.example.breakApp.api.model.RoutineDto
import com.example.breakApp.jetpack.tools.Calendar
import kotlinx.coroutines.launch

@Composable
fun HistoryExercise(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) } // 다이얼로그 표시 여부
    var workoutLogs by remember { mutableStateOf<List<CompletedWorkoutDto>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) } // 에러 메시지
    var routines by remember { mutableStateOf<List<RoutineDto>>(emptyList()) } // 루틴 목록
    var exercises by remember { mutableStateOf<Map<Long, String>>(emptyMap()) } // 운동 ID -> 이름 매핑
    val coroutineScope = rememberCoroutineScope()

    // 루틴 목록 가져오기
    LaunchedEffect(Unit) {
        try {
            val response = RetrofitInstance.api.getRoutineList()
            if (response.isSuccessful) {
                routines = response.body() ?: emptyList()
            } else {
                errorMessage = "Error fetching routines: ${response.errorBody()?.string()}"
            }
        } catch (e: Exception) {
            errorMessage = "Error fetching routines: ${e.localizedMessage}"
        }

        // 운동 목록 가져오기
        try {
            val exerciseResponse = RetrofitInstance.api.getAllExercises() // 모든 운동 API 호출
            if (exerciseResponse.isSuccessful) {
                exercises = exerciseResponse.body()?.associateBy({ it.exerciseId }, { it.name }) ?: emptyMap()
            } else {
                errorMessage = "Error fetching exercises: ${exerciseResponse.errorBody()?.string()}"
            }
        } catch (e: Exception) {
            errorMessage = "Error fetching exercises: ${e.localizedMessage}"
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "운동 기록", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))

        // Calendar 컴포넌트 호출
        Calendar(onDateSelected = { year, month, day ->
            val selectedDate = "$year-${String.format("%02d", month + 1)}-${String.format("%02d", day)}"
            coroutineScope.launch {
                try {
                    val response = RetrofitInstance.api.getCompletedWorkouts() // 완료된 운동 API 호출
                    if (response.isSuccessful) {
                        workoutLogs = response.body()?.filter { workout ->
                            workout.completedExercises.any { exercise ->
                                exercise.sets.any { set ->
                                    set.setNumber > 0 // 날짜 필터링 대체
                                }
                            }
                        } ?: emptyList()

                        showDialog = workoutLogs.isNotEmpty() // 데이터가 있으면 다이얼로그 표시
                    } else {
                        errorMessage = "오류: ${response.errorBody()?.string()}"
                        workoutLogs = emptyList()
                    }
                } catch (e: Exception) {
                    errorMessage = "예외 발생: ${e.localizedMessage}"
                    workoutLogs = emptyList()
                }
            }
        })
    }

    // 완료된 운동 기록 다이얼로그
    if (showDialog) {
        CompletedWorkoutsDialog(
            workouts = workoutLogs,
            routines = routines,
            exercises = exercises,
            onDismiss = { showDialog = false }
        )
    }

    // 에러 메시지 출력
    errorMessage?.let { message ->
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun CompletedWorkoutsDialog(
    workouts: List<CompletedWorkoutDto>,
    routines: List<RoutineDto>, // 루틴 이름 조회를 위한 리스트
    exercises: Map<Long, String>, // 운동 이름 매핑
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("완료된 운동 기록") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                workouts.forEach { workout ->
                    // 루틴 이름 찾기
                    val routineName = routines.find { it.routineId == workout.routineId }?.name ?: "알 수 없음"
                    Text(
                        text = "루틴: $routineName",
                        style = MaterialTheme.typography.titleMedium
                    )
                    workout.completedExercises.forEach { exercise ->
                        val exerciseName = exercises[exercise.exerciseId] ?: "알 수 없는 운동"
                        Text(
                            text = "  운동: $exerciseName",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        exercise.sets.forEach { set ->
                            Text(
                                text = "    세트 ${set.setNumber}: ${set.repetitions}회 * ${set.weight}kg",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onDismiss() }) {
                Text("확인")
            }
        }
    )
}

package com.example.breakApp.jetpack.subscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.api.model.Exercise
import com.example.breakApp.api.model.ExerciseSetDto
import com.example.breakApp.jetpack.tools.Calendar
import kotlinx.coroutines.launch

@Composable
fun HistoryExercise(navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var sets by remember { mutableStateOf<List<ExerciseSetDto>>(emptyList()) } // 세트 데이터만 가져옴
    var exercises by remember { mutableStateOf<List<Exercise>>(emptyList()) } // 운동 목록
    var sortedSets by remember { mutableStateOf<List<ExerciseSetDto>>(emptyList()) } // 필터링 및 정렬된 세트 데이터
    val coroutineScope = rememberCoroutineScope()

    var selectedDate by remember { mutableStateOf("") } // 캘린더에서 선택된 날짜

    // 데이터 로드
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val routineResponse = RetrofitInstance.api.getRoutineList()
                if (routineResponse.isSuccessful) {
                    val routines = routineResponse.body() ?: emptyList()

                    val allSets = mutableListOf<ExerciseSetDto>()
                    for (routine in routines) {
                        val routineId = routine.routineId // RoutineDto에서 routineId를 가져옴

                        if (routineId != null && routineId > 0) { // routineId가 null이 아니고 유효한 값일 때만 처리
                            val setResponse = RetrofitInstance.api.getSetsByRoutine(routineId)
                            if (setResponse.isSuccessful) {
                                allSets.addAll(setResponse.body() ?: emptyList())
                            } else {
                                errorMessage = "루틴 ID $routineId 의 세트를 가져오는 중 오류가 발생했습니다."
                            }
                        } else {
                            errorMessage = "유효하지 않은 루틴 ID $routineId"
                        }
                    }
                    sets = allSets
                } else {
                    errorMessage = "루틴 데이터를 가져오는 중 오류가 발생했습니다."
                }

                // 운동 목록 가져오기
                val exerciseResponse = RetrofitInstance.api.getAllExercises() // Exercise API 호출
                if (exerciseResponse.isSuccessful) {
                    exercises = exerciseResponse.body() ?: emptyList()
                } else {
                    errorMessage = "운동 목록을 가져오는 중 오류가 발생했습니다."
                }
            } catch (e: Exception) {
                errorMessage = "데이터를 불러오는 중 오류가 발생했습니다: ${e.localizedMessage}"
                e.printStackTrace()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "운동 기록", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))

        // 캘린더 컴포넌트
        Calendar(onDateSelected = { year, month, day ->
            selectedDate = "$year-${String.format("%02d", month + 1)}-${String.format("%02d", day)}"
            coroutineScope.launch {
                // 선택된 날짜에 해당하는 세트 필터링 및 정렬
                sortedSets = sets.filter {
                    it.createdAt.startsWith(selectedDate) // 날짜 필터링
                }.sortedBy {
                    it.createdAt // 생성 시간 기준으로 정렬
                }
                showDialog = true // 다이얼로그 표시
            }
        })

        // 에러 메시지 출력
        errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(16.dp))
        }
    }

    // 정렬된 데이터를 다이얼로그로 출력
    if (showDialog) {
        CompletedSetsDialog(
            sets = sortedSets,
            exercises = exercises, // 운동 목록을 전달
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun CompletedSetsDialog(
    sets: List<ExerciseSetDto>,
    exercises: List<Exercise>, // 운동 목록 추가
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("운동 기록") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                sets.forEach { set ->
                    // ExerciseSetDto에서 운동 ID를 찾아 운동 이름을 가져옵니다.
                    val exerciseName = exercises.find { it.exerciseId == set.exerciseId }?.name ?: "알 수 없는 운동"
                    Text(
                        text = "운동: $exerciseName", // 운동 이름 추가
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "세트: ${set.setNumber} | ${set.repetitions}회 * ${set.weight}kg ",
                        style = MaterialTheme.typography.bodySmall
                    )
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

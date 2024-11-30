package com.example.breakApp.jetpack.subscreen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.api.model.Exercise
import com.example.breakApp.api.model.ExerciseSetDto
import com.example.breakApp.api.model.WorkoutLogDto
import com.example.breakApp.jetpack.tools.Calendar
import kotlinx.coroutines.launch

@Composable
fun HistoryExercise(navController: NavController) {
    var workoutLogs by remember { mutableStateOf<List<WorkoutLogDto>>(emptyList()) } // 모든 WorkoutLogDto
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
    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                // WorkoutLog 데이터를 가져오기 위한 API 호출
                val workoutLogResponse = RetrofitInstance.api.getAllWorkoutLogs()
                if (workoutLogResponse.isSuccessful) {
                    workoutLogs = workoutLogResponse.body() ?: emptyList()
                    if (workoutLogs.isEmpty()) {
                        errorMessage = "운동 기록이 없습니다."
                    } else {
                        Log.d("WorkoutLogs", "Data loaded: $workoutLogs")
                    }
                } else {
                    errorMessage = "Workout logs API 호출 실패: ${workoutLogResponse.code()} - ${workoutLogResponse.message()}"
                    Log.e("WorkoutLogs", "API Error: ${workoutLogResponse.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                errorMessage = "Workout logs 데이터를 가져오는 중 오류 발생: ${e.localizedMessage}"
                Log.e("WorkoutLogs", "Exception: ${e.localizedMessage}")
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
            workoutLogs = workoutLogs,
            sets = sortedSets,
            exercises = exercises, // 운동 목록을 전달
            selectedDate = selectedDate,
            onDismiss = { showDialog = false }
        )
    }
}

@Composable
fun CompletedSetsDialog(
    sets: List<ExerciseSetDto>,
    exercises: List<Exercise>, // 운동 목록 추가
    workoutLogs: List<WorkoutLogDto>,
    selectedDate: String, // 선택된 날짜
    onDismiss: () -> Unit
) {
    val firstRoutineName = remember(selectedDate, workoutLogs) {
        workoutLogs
            .filter { log -> log.startTime.startsWith(selectedDate) } // 선택된 날짜로 필터링
            .firstOrNull()?.routine?.name ?: "" // 첫 번째 루틴 이름 가져오기
    }

    // 선택된 날짜 포맷 변환
    val formattedDate = remember(selectedDate) {
        if (selectedDate.isNotEmpty()) {
            try {
                val parts = selectedDate.split("-")
                "${parts[1].toInt()}월 ${parts[2].toInt()}일" // MM월 DD일 포맷
            } catch (e: Exception) {
                "알 수 없는 날짜"
            }
        } else {
            "알 수 없는 날짜"
        }
    }

    fun calculateTotalDuration(workoutLogs: List<WorkoutLogDto>, selectedDate: String): String {
        var totalDuration = 0L // 총 운동 시간을 초 단위로 계산

        val formatter = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", java.util.Locale.getDefault())

        // 선택된 날짜의 로그만 필터링
        val filteredLogs = workoutLogs.filter { log ->
            log.startTime.startsWith(selectedDate) // startTime이 선택된 날짜로 시작하는지 확인
        }

        filteredLogs.forEach { log ->
            if (!log.startTime.isNullOrEmpty() && !log.endTime.isNullOrEmpty()) {
                try {
                    // startTime과 endTime을 Date로 변환
                    val start = formatter.parse(log.startTime)
                    val end = formatter.parse(log.endTime)

                    // 시작 시간과 종료 시간의 차이를 초 단위로 계산
                    val duration = (end.time - start.time) / 1000 // 밀리초를 초 단위로 변환
                    totalDuration += duration
                } catch (e: Exception) {
                    e.printStackTrace() // 오류 발생 시 로그 출력
                }
            }
        }
        // 초 단위 결과를 분/초 포맷으로 변환
        return if (totalDuration > 0) {
            val minutes = totalDuration / 60
            val seconds = totalDuration % 60
            if (minutes > 0) "$minutes"+"분" +"$seconds"+"초" else "$seconds"+"초"
        } else {
            ""
        }
    }

    val totalDuration = calculateTotalDuration(workoutLogs, selectedDate)

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(formattedDate) }, // 선택된 날짜를 제목으로 표시
        text = {
            if (totalDuration.isEmpty() && sets.isEmpty()) {
                // 운동 기록이 없는 경우
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f),
                    contentAlignment = Alignment.Center // 내용 중앙 정렬
                ) {
                    Text(
                        text = "운동을 한 기록이 없습니다.",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // 운동 기록이 있는 경우
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), // 전체 리스트에 패딩 추가
                    verticalArrangement = Arrangement.spacedBy(2.dp) // 각 항목 간 간격 설정
                ) {
                    if (firstRoutineName.isNotEmpty()) {
                        item {
                            // 루틴 이름 표시
                            Text(
                                text = "$firstRoutineName",
                                style = MaterialTheme.typography.headlineLarge,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 24.dp),
                                textAlign = TextAlign.Center // 텍스트를 중앙 정렬
                            )
                        }
                    }
                    sets.forEach { set ->
                        item {
                            // ExerciseSetDto에서 운동 ID를 찾아 운동 이름을 가져옵니다.
                            val exerciseName = exercises.find { it.exerciseId == set.exerciseId }?.name ?: "알 수 없는 운동"

                            // 운동 이름
                            Text(
                                text = "운동: $exerciseName", // 운동 이름 추가
                                style = MaterialTheme.typography.bodyLarge
                            )

                            // 세트 정보
                            Text(
                                text = "세트: ${set.setNumber} | ${set.weight}kg * ${set.repetitions}회",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    if (totalDuration.isNotEmpty()) {
                        item {
                            Text(
                                text = "총 운동 시간: $totalDuration",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(top = 16.dp)
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
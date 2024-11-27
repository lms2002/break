package com.example.breakApp.jetpack.subscreen

import com.example.breakApp.jetpack.tools.ExerciseInputSection
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.breakApp.R
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.api.model.Exercise
import com.example.breakApp.api.model.ExerciseSetDto
import com.example.breakApp.api.model.RoutineDto
import com.example.breakApp.api.model.StartWorkoutRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineManagement(navController: NavController, routineId: Long, routineName: String) {
    var currentRoutineName by remember { mutableStateOf(routineName) }
    var newRoutineName by remember { mutableStateOf(routineName) } // 새로운 이름 입력
    var isEditing by remember { mutableStateOf(false) } // 편집 상태 관리
    var exercises by remember { mutableStateOf<List<Exercise>>(emptyList()) }
    var exerciseInputSections by remember {
        mutableStateOf<Map<Long, List<Triple<Boolean, MutableState<Pair<Float, Int>>, Long?>>>>(emptyMap())
    }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var userId by remember { mutableStateOf<Long?>(null) } // 사용자 ID 상태
    var workoutLogId by remember { mutableStateOf<Long?>(null) } // 시작한 운동의 로그 ID
    var startTime by remember { mutableStateOf<String?>(null) } // 운동 시작 시간
    var isWorkoutInProgress by remember { mutableStateOf(false) } // 운동 중인지 여부
    var endTime by remember { mutableStateOf<String?>(null) }

    // 사용자 ID 가져오기
    LaunchedEffect(Unit) {
        isLoading = true // 로딩 시작
        try {
            val response = RetrofitInstance.api.getMyInfo()
            if (response.isSuccessful) {
                userId = response.body()?.data?.userId
            } else {
                errorMessage = "Error fetching user info: ${response.errorBody()?.string()}"
            }
        } catch (e: Exception) {
            errorMessage = "An error occurred: ${e.localizedMessage}"
        } finally {
            isLoading = false // API 호출 완료 후 로딩 상태 종료
        }
    }

// API 호출: 루틴에 포함된 운동 데이터 가져오기
    LaunchedEffect(routineId) {
        isLoading = true // 로딩 시작
        try {
            val response = RetrofitInstance.api.getExercisesByRoutineId(routineId)
            if (response.isSuccessful) {
                exercises = response.body() ?: emptyList()
            } else {
                errorMessage = "Error: ${response.errorBody()?.string()}"
            }
        } catch (e: Exception) {
            errorMessage = "An error occurred: ${e.localizedMessage}"
        } finally {
            isLoading = false // API 호출 완료 후 로딩 상태 종료
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (isEditing) {
                            // 편집 상태에서는 입력 필드와 확인 버튼 표시
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                TextField(
                                    value = newRoutineName,
                                    onValueChange = { newRoutineName = it },
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(Color.Transparent) // 배경색을 투명하게 설정
                                        .padding(8.dp), // 여백 추가
                                    textStyle = TextStyle(
                                        color = MaterialTheme.colorScheme.onBackground, // 텍스트 색상 설정
                                        fontSize = 16.sp // 텍스트 크기 조정
                                    ),
                                    colors = TextFieldDefaults.textFieldColors(
                                        containerColor = Color.Transparent, // 배경 투명
                                        cursorColor = MaterialTheme.colorScheme.onBackground // 커서 색상
                                    )
                                )

                                IconButton(onClick = {
                                    // 이름 저장 로직
                                    if (userId != null) {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            try {
                                                val updatedRoutine = RoutineDto(
                                                    routineId = routineId,
                                                    userId = userId!!, // 가져온 사용자 ID 사용
                                                    name = newRoutineName
                                                )
                                                val response = RetrofitInstance.api.updateRoutine(routineId, updatedRoutine)
                                                if (response.isSuccessful) {
                                                    currentRoutineName = newRoutineName
                                                    isEditing = false // 편집 상태 종료
                                                } else {
                                                    errorMessage = "Error updating routine: ${response.errorBody()?.string()}"
                                                }
                                            } catch (e: Exception) {
                                                errorMessage = "Error: ${e.localizedMessage}"
                                            }
                                        }
                                    } else {
                                        errorMessage = "User ID not found"
                                    }
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_check),
                                        contentDescription = "Save"
                                    )
                                }

                            }
                        } else {
                            // 기본 상태에서는 제목과 편집 버튼 표시
                            Text(
                                text = "$currentRoutineName",
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.titleLarge
                            )
                            IconButton(onClick = { isEditing = true }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_setting), // 편집 아이콘
                                    contentDescription = "Edit Routine Name"
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 운동 시작 버튼
                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val response = RetrofitInstance.api.startWorkout(
                                    StartWorkoutRequest(routineId = routineId)
                                )
                                if (response.isSuccessful) {
                                    val workoutLog = response.body()
                                    workoutLogId = workoutLog?.logId
                                    startTime = workoutLog?.startTime.toString() // LocalDateTime -> String
                                    isWorkoutInProgress = true
                                } else {
                                    withContext(Dispatchers.Main) {
                                        errorMessage = "Failed to start workout: ${response.errorBody()?.string()}"
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    errorMessage = "Error starting workout: ${e.localizedMessage}"
                                }
                            }
                        }
                    },
                    modifier = Modifier.weight(1f), // 버튼 크기를 균등하게 분배
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000)),
                    enabled = !isWorkoutInProgress // 운동 중일 때 시작 버튼 비활성화
                ) {
                    Text("운동 시작", color = Color.White)
                }

                Spacer(modifier = Modifier.width(16.dp)) // 버튼 간 간격 추가

                // 운동 종료 버튼
                Button(
                    onClick = {
                        workoutLogId?.let { logId ->
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val response = RetrofitInstance.api.endWorkout(logId)
                                    if (response.isSuccessful) {
                                        val completedWorkout = response.body()
                                        endTime = completedWorkout?.endTime.toString() // LocalDateTime -> String
                                        isWorkoutInProgress = false
                                    } else {
                                        withContext(Dispatchers.Main) {
                                            errorMessage = "Failed to end workout: ${response.errorBody()?.string()}"
                                        }
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        errorMessage = "Error ending workout: ${e.localizedMessage}"
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier.weight(1f), // 버튼 크기를 균등하게 분배
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    enabled = isWorkoutInProgress // 운동이 진행 중일 때만 종료 버튼 활성화
                ) {
                    Text("운동 종료", color = Color.White)
                }
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (errorMessage != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                exercises.forEach { exercise ->
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = exercise.name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = {
                                // 세트 추가
                                exerciseInputSections = exerciseInputSections.toMutableMap().apply {
                                    val currentSets = getOrDefault(exercise.exerciseId, emptyList())
                                    this[exercise.exerciseId] = currentSets + Triple(
                                        false, // 저장되지 않은 세트
                                        mutableStateOf(Pair(0f, 0)), // 기본 weight와 repetitions 값
                                        null // setId는 아직 null
                                    )
                                }
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_add),
                                    contentDescription = "Add Set"
                                )
                            }
                            IconButton(onClick = {
                                // 세트 삭제
                                exerciseInputSections = exerciseInputSections.toMutableMap().apply {
                                    val currentSets = getOrDefault(
                                        exercise.exerciseId,
                                        emptyList()
                                    ).toMutableList()
                                    if (currentSets.isNotEmpty()) {
                                        currentSets.removeAt(currentSets.size - 1) // 마지막 세트를 삭제
                                        this[exercise.exerciseId] = currentSets
                                    }
                                }
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_minus),
                                    contentDescription = "Remove Set"
                                )
                            }
                        }

                        // 세트별 입력 필드 및 체크 버튼
                        exerciseInputSections[exercise.exerciseId]?.forEachIndexed { index, (isSaved, inputState, setId) ->
                            ExerciseInputSection(
                                label = "세트 ${index + 1}",
                                weight = inputState.value.first,
                                repetitions = inputState.value.second,
                                isSaved = isSaved,
                                onWeightChange = { newWeight ->
                                    inputState.value = inputState.value.copy(first = newWeight)
                                },
                                onRepetitionsChange = { newRepetitions ->
                                    inputState.value =
                                        inputState.value.copy(second = newRepetitions)
                                },
                                onSaveClicked = {
                                    // 서버로 세트 저장
                                    CoroutineScope(Dispatchers.IO).launch {
                                        try {
                                            val response = RetrofitInstance.api.createExerciseSet(
                                                ExerciseSetDto(
                                                    routineId = routineId,
                                                    exerciseId = exercise.exerciseId,
                                                    setNumber = index + 1,
                                                    weight = inputState.value.first,
                                                    repetitions = inputState.value.second,
                                                    isCompleted = true
                                                )
                                            )
                                            if (response.isSuccessful) {
                                                exerciseInputSections =
                                                    exerciseInputSections.toMutableMap().apply {
                                                        val currentSets = getOrDefault(
                                                            exercise.exerciseId,
                                                            emptyList()
                                                        ).toMutableList()
                                                        currentSets[index] = Triple(
                                                            true,
                                                            inputState,
                                                            response.body()?.setId
                                                        )
                                                        this[exercise.exerciseId] = currentSets
                                                    }
                                            } else {
                                                withContext(Dispatchers.Main) {
                                                    errorMessage = "Failed to save set: ${
                                                        response.errorBody()?.string()
                                                    }"
                                                }
                                            }
                                        } catch (e: Exception) {
                                            withContext(Dispatchers.Main) {
                                                errorMessage =
                                                    "Error saving set: ${e.localizedMessage}"
                                            }
                                        }
                                    }
                                }
                            )

                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    // 루틴 삭제 API 호출
                                    val response = RetrofitInstance.api.deleteRoutine(routineId)
                                    if (response.isSuccessful) {
                                        // 성공적으로 삭제한 경우 MainScreen으로 이동
                                        withContext(Dispatchers.Main) {
                                            navController.navigate("mainscreen") {
                                                popUpTo("mainscreen") { inclusive = true }
                                            }
                                        }
                                    } else {
                                        // 삭제 실패 시 오류 처리
                                        withContext(Dispatchers.Main) {
                                            println(
                                                "Failed to delete routine: ${
                                                    response.errorBody()?.string()
                                                }"
                                            )
                                        }
                                    }
                                } catch (e: Exception) {
                                    // 네트워크 오류 처리
                                    withContext(Dispatchers.Main) {
                                        println("Error deleting routine: ${e.localizedMessage}")
                                    }
                                }
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("삭제", color = Color.White)
                    }

                }
            }
        }
    }
}


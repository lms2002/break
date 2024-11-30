package com.example.breakApp.jetpack.subscreen

import com.example.breakApp.jetpack.tools.ExerciseInputSection
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineManagement(navController: NavController, routineId: Long, routineName: String) {
    var currentRoutineName by remember { mutableStateOf(routineName) }
    var newRoutineName by remember { mutableStateOf(routineName) } // 새로운 이름 입력
    var isEditing by remember { mutableStateOf(false) } // 편집 상태 관리
    var exercises by remember { mutableStateOf<List<Exercise>>(emptyList()) }
    var exerciseInputSections by remember {
        mutableStateOf<Map<Long, List<Triple<Boolean, MutableState<Pair<Float, Int>>, Long?>>>>(
            emptyMap()
        )
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
                                                val response = RetrofitInstance.api.updateRoutine(
                                                    routineId,
                                                    updatedRoutine
                                                )
                                                if (response.isSuccessful) {
                                                    currentRoutineName = newRoutineName
                                                    isEditing = false // 편집 상태 종료
                                                } else {
                                                    errorMessage = "Error updating routine: ${
                                                        response.errorBody()?.string()
                                                    }"
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
                                    startTime =
                                        workoutLog?.startTime.toString() // LocalDateTime -> String
                                    isWorkoutInProgress = true
                                } else {
                                    withContext(Dispatchers.Main) {
                                        errorMessage = "Failed to start workout: ${
                                            response.errorBody()?.string()
                                        }"
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
                                        endTime =
                                            completedWorkout?.endTime.toString() // LocalDateTime -> String
                                        isWorkoutInProgress = false
                                    } else {
                                        withContext(Dispatchers.Main) {
                                            errorMessage = "Failed to end workout: ${
                                                response.errorBody()?.string()
                                            }"
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
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000)),
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
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(exercises) { exercise ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween // 이름과 삭제 버튼을 양쪽 끝으로 정렬
                            ) {
                                // 운동 이름
                                Text(
                                    text = exercise.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )

                                // 삭제 아이콘
                                IconButton(
                                    onClick = {
                                        // 삭제 로직: 운동을 삭제하는 함수 호출
                                        exerciseInputSections = exerciseInputSections.toMutableMap().apply {
                                            remove(exercise.exerciseId)
                                        }
                                        exercises = exercises.filter { it.exerciseId != exercise.exerciseId }
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_delete),
                                        contentDescription = "Delete Exercise",
                                        tint = Color.Red // 아이콘 색상
                                    )
                                }
                            }

                            // 세트 입력 섹션
                            // 세트 입력 섹션
                            exerciseInputSections[exercise.exerciseId]?.forEachIndexed { index, (isSaved, inputState, setId) ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally // 가로 정렬을 가운데로 설정
                                ) {
                                    ExerciseInputSection(
                                        label = "세트 ${index + 1}",
                                        weight = inputState.value.first,
                                        repetitions = inputState.value.second,
                                        isSaved = isSaved,
                                        onWeightChange = { newWeight ->
                                            inputState.value = inputState.value.copy(first = newWeight)
                                        },
                                        onRepetitionsChange = { newRepetitions ->
                                            inputState.value = inputState.value.copy(second = newRepetitions)
                                        },
                                        onSaveClicked = {
                                            val currentTime = SimpleDateFormat(
                                                "yyyy-MM-dd'T'HH:mm:ss",
                                                Locale.getDefault()
                                            ).format(java.util.Date())
                                            CoroutineScope(Dispatchers.IO).launch {
                                                try {
                                                    val response = RetrofitInstance.api.createExerciseSet(
                                                        ExerciseSetDto(
                                                            routineId = routineId,
                                                            exerciseId = exercise.exerciseId,
                                                            setNumber = index + 1,
                                                            weight = inputState.value.first,
                                                            repetitions = inputState.value.second,
                                                            isCompleted = true,
                                                            createdAt = currentTime
                                                        )
                                                    )
                                                    if (response.isSuccessful) {
                                                        exerciseInputSections = exerciseInputSections.toMutableMap().apply {
                                                            val currentSets = getOrDefault(exercise.exerciseId, emptyList()).toMutableList()
                                                            currentSets[index] = Triple(
                                                                true,
                                                                inputState,
                                                                response.body()?.setId
                                                            )
                                                            this[exercise.exerciseId] = currentSets
                                                        }
                                                    } else {
                                                        withContext(Dispatchers.Main) {
                                                            errorMessage = "Failed to save set: ${response.errorBody()?.string()}"
                                                        }
                                                    }
                                                } catch (e: Exception) {
                                                    withContext(Dispatchers.Main) {
                                                        errorMessage = "Error saving set: ${e.localizedMessage}"
                                                    }
                                                }
                                            }
                                        }
                                    )
                                }
                            }

                            // 세트 추가/삭제 버튼
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                horizontalArrangement = Arrangement.Center // 버튼을 가운데로 배치
                            ) {
                                IconButton(
                                    onClick = {
                                        // 세트 추가 로직
                                        exerciseInputSections = exerciseInputSections.toMutableMap().apply {
                                            val currentSets = getOrDefault(exercise.exerciseId, emptyList())
                                            this[exercise.exerciseId] = currentSets + Triple(
                                                false, // 저장되지 않은 세트
                                                mutableStateOf(Pair(0f, 0)), // 초기 weight와 repetitions 값
                                                null // setId는 null
                                            )
                                        }
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_add),
                                        contentDescription = "세트 추가",
                                        tint = Color.White
                                    )
                                }

                                Spacer(modifier = Modifier.width(72.dp)) // 두 버튼 사이의 간격 조정

                                IconButton(
                                    onClick = {
                                        // 세트 삭제 로직
                                        exerciseInputSections = exerciseInputSections.toMutableMap().apply {
                                            val currentSets = getOrDefault(exercise.exerciseId, emptyList()).toMutableList()
                                            if (currentSets.isNotEmpty()) {
                                                currentSets.removeAt(currentSets.size - 1)
                                                this[exercise.exerciseId] = currentSets
                                            }
                                        }
                                    }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_minus),
                                        contentDescription = "세트 삭제",
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }

                    // 삭제 버튼을 LazyColumn의 마지막에 추가
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        try {
                                            val exerciseResponse = RetrofitInstance.api.getExercisesByRoutineId(routineId)
                                            if (exerciseResponse.isSuccessful) {
                                                val exercises = exerciseResponse.body() ?: emptyList()
                                                exercises.forEach { exercise ->
                                                    val deleteExerciseResponse = RetrofitInstance.api.removeExerciseFromRoutine(
                                                        routineId = routineId,
                                                        exerciseId = exercise.exerciseId
                                                    )
                                                    if (!deleteExerciseResponse.isSuccessful) {
                                                        withContext(Dispatchers.Main) {
                                                            println("Failed to delete exercise: ${deleteExerciseResponse.errorBody()?.string()}")
                                                        }
                                                        return@forEach
                                                    }
                                                }
                                                val deleteRoutineResponse = RetrofitInstance.api.deleteRoutine(routineId)
                                                if (deleteRoutineResponse.isSuccessful) {
                                                    withContext(Dispatchers.Main) {
                                                        navController.navigate("mainscreen") {
                                                            popUpTo("mainscreen") { inclusive = true }
                                                        }
                                                    }
                                                } else {
                                                    withContext(Dispatchers.Main) {
                                                        println("Failed to delete routine: ${deleteRoutineResponse.errorBody()?.string()}")
                                                    }
                                                }
                                            } else {
                                                withContext(Dispatchers.Main) {
                                                    println("Failed to fetch exercises: ${exerciseResponse.errorBody()?.string()}")
                                                }
                                            }
                                        } catch (e: Exception) {
                                            withContext(Dispatchers.Main) {
                                                println("Error deleting routine: ${e.localizedMessage}")
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA0000)),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("삭제", color = Color.White)
                            }
                        }
                    }
                }

            }
        }
    }
}
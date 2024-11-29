package com.example.breakApp.jetpack.subscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import com.example.breakApp.R
import androidx.navigation.NavController
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.api.model.Exercise
import com.example.breakApp.api.model.RoutineDto
import com.example.breakApp.jetpack.tools.BottomNavigationBar
import com.example.breakApp.jetpack.tools.RoutineDialog
import com.example.breakApp.tools.PreferenceManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.breakApp.api.model.CreateRoutineExerciseRequest
import com.example.breakApp.api.model.ExerciseRequest
import kotlinx.coroutines.launch

@Composable
fun CustomRoutine(navController: NavController) {
    var selectedTarget by remember { mutableStateOf("가슴") } // 초기 카테고리
    var showRoutineDialog by remember { mutableStateOf(true) } // 루틴 선택 다이얼로그 표시 여부
    var selectedRoutine by remember { mutableStateOf<RoutineDto?>(null) } // 선택된 루틴
    var userId by remember { mutableStateOf<Long?>(null) }

    // 루틴 초기화 및 생성 확인
    LaunchedEffect(Unit) {
        val token = PreferenceManager.getAccessToken()
        if (token != null) {
            try {
                // 사용자 정보 가져오기
                val response = RetrofitInstance.api.getMyInfo()
                if (response.isSuccessful) {
                    userId = response.body()?.data?.userId
                    println("User ID fetched: $userId")
                } else {
                    println("Error fetching user info: ${response.errorBody()?.string()}")
                    return@LaunchedEffect
                }

                // 사용자 정보가 없으면 중단
                if (userId == null) {
                    println("User ID not found")
                    return@LaunchedEffect
                }

                // 루틴 목록 가져오기
                val routinesResponse = RetrofitInstance.api.getRoutineList()
                val routines = routinesResponse.body() ?: emptyList()

                // 루틴이 없으면 새 루틴 생성
                if (routines.isEmpty()) {
                    val defaultRoutine = RoutineDto(
                        userId = userId!!, // 가져온 userId 사용
                        name = "루틴 1"
                    )
                    val createResponse = RetrofitInstance.api.createRoutine(defaultRoutine)
                    if (createResponse.isSuccessful) {
                        selectedRoutine = createResponse.body() // 생성된 루틴 선택
                        println("Default routine created: ${selectedRoutine?.routineId}")
                    }
                } else {
                    selectedRoutine = routines.firstOrNull() // 첫 번째 루틴 선택
                    println("First routine selected: ${selectedRoutine?.routineId}")
                }
            } catch (e: Exception) {
                println("Exception during initialization: ${e.localizedMessage}")
            }
        }
    }

    // 루틴 다이얼로그 표시
    if (showRoutineDialog) {
        RoutineDialog(
            onDismiss = { showRoutineDialog = false }, // 다이얼로그 닫기
            onRoutineSelected = { routine ->
                selectedRoutine = routine
                showRoutineDialog = false // 다이얼로그 닫기
                println("Routine selected from dialog: ${routine.routineId}")
            }
        )
    }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, 0) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // 선택된 루틴 표시
            if (selectedRoutine != null) {
                Text(
                    text = "선택된 루틴: ${selectedRoutine?.name} (ID: ${selectedRoutine?.routineId})",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text(
                    text = "루틴을 선택하세요",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = Color.Gray
                )
            }

            // 검색 바
            SearchBar()

            // 카테고리 필터
            CategoryFilters(selectedTarget) { target ->
                selectedTarget = target // 선택된 카테고리 변경
            }

            Spacer(modifier = Modifier.height(16.dp)) // 카테고리와 운동 목록 사이의 간격

            // 선택된 카테고리와 루틴 ID를 기반으로 운동 목록 표시
            selectedRoutine?.let { routine ->
                println("Selected Routine: $routine")
                if (routine.routineId != null && routine.routineId > 0) {
                    ExerciseList(selectedTarget, routine.routineId) // selectedBodyPart -> selectedTarget
                    println("Routine ID sent to ExerciseList: ${routine.routineId}")
                } else {
                    println("Invalid routine ID: ${routine.routineId}")
                }
            }
        }
    }
}



@Composable
fun CategoryFilters(
    selectedTarget: String,
    onTargetSelected: (String) -> Unit // 함수명도 target에 맞게 변경
) {
    val targets = listOf("가슴", "등", "어깨", "유산소", "삼두", "이두", "하체", "전신") // "categories"를 "targets"로 변경

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            targets.take(4).forEach { target -> // targets의 첫 4개를 반복
                Text(
                    text = target,
                    color = if (selectedTarget == target) Color(0xFFFFA500) else Color.White, // 선택된 타겟 강조
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clickable { onTargetSelected(target) } // 클릭 시 선택된 타겟 전달
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            targets.drop(4).forEach { target -> // targets의 나머지 4개를 반복
                Text(
                    text = target,
                    color = if (selectedTarget == target) Color(0xFFFFA500) else Color.White, // 선택된 타겟 강조
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clickable { onTargetSelected(target) } // 클릭 시 선택된 타겟 전달
                )
            }
        }
    }
}


@Composable
fun ExerciseList(selectedTarget: String, selectedRoutineId: Long) {
    var exercises by remember { mutableStateOf<List<Exercise>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedExercise by remember { mutableStateOf<Long?>(null) } // 선택된 운동 ID를 저장

    // CoroutineScope 추가
    val coroutineScope = rememberCoroutineScope()

    // 서버에서 운동 목록 가져오기
    LaunchedEffect(selectedTarget) {
        try {
            isLoading = true
            errorMessage = null
            val response = RetrofitInstance.api.getExercisesByTargetArea(selectedTarget) // API 호출
            if (response.isSuccessful) {
                exercises = response.body() ?: emptyList() // List<Exercise>로 처리
                println("Exercises fetched: $exercises") // 로그 추가
                println("Selected Target: $selectedTarget")
            } else {
                errorMessage = "Error: ${response.errorBody()?.string()}"
            }
        } catch (e: Exception) {
            errorMessage = "An error occurred: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    // UI
    Column(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            )
        } else if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.padding(8.dp)) {
                items(exercises) { exercise ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { selectedExercise = if (selectedExercise == exercise.exerciseId) null else exercise.exerciseId },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.DarkGray)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "${exercise.name} - ${exercise.target}", // 출력 변경
                            style = TextStyle(fontSize = 16.sp, color = Color.White),
                            modifier = Modifier.weight(1f)
                        )

                        if (selectedExercise == exercise.exerciseId) {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    try {
                                        val request = CreateRoutineExerciseRequest(
                                            routineId = selectedRoutineId,
                                            exercises = listOf(ExerciseRequest(exerciseId = exercise.exerciseId))
                                        )
                                        println("Request Body Sent: $request")
                                        val response = RetrofitInstance.api.addExercisesToRoutine(request)
                                        if (response.isSuccessful) {
                                            println("Exercise added successfully")
                                            selectedExercise = null
                                        } else {
                                            errorMessage = "Error: ${response.errorBody()?.string()}"
                                        }
                                    } catch (e: Exception) {
                                        errorMessage = "An error occurred: ${e.localizedMessage}"
                                    }
                                }
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_add),
                                    contentDescription = "Add to Routine",
                                    tint = Color(0xFFBA0000)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SearchBar() {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        // 검색 바
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray, shape = CircleShape)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            BasicTextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp, color = Color.White),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

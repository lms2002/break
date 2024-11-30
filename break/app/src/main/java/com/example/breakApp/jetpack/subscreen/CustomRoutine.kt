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
fun ExerciseList(selectedTarget: String, selectedRoutineId: Long, searchQuery: String) {
    var exercises by remember { mutableStateOf<List<Exercise>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedExercise by remember { mutableStateOf<Long?>(null) }

    val coroutineScope = rememberCoroutineScope()

    // 서버에서 운동 목록 가져오기
    LaunchedEffect(selectedTarget) {
        try {
            isLoading = true
            errorMessage = null
            val response = RetrofitInstance.api.getExercisesByTargetArea(selectedTarget)
            if (response.isSuccessful) {
                exercises = response.body() ?: emptyList() // 서버에서 받은 데이터를 저장
            } else {
                errorMessage = "운동 데이터를 가져오는 데 실패했습니다."
            }
        } catch (e: Exception) {
            errorMessage = "오류 발생: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    // 검색어로 필터링된 데이터
    val filteredExercises = remember(searchQuery, exercises) {
        if (searchQuery.isBlank()) {
            exercises // 검색어가 없으면 전체 데이터
        } else {
            exercises.filter { exercise ->
                exercise.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }

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
                items(filteredExercises) { exercise ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                selectedExercise = if (selectedExercise == exercise.exerciseId) null else exercise.exerciseId
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.DarkGray)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "${exercise.name} - ${exercise.target}",
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
                                        val response = RetrofitInstance.api.addExercisesToRoutine(request)
                                        if (response.isSuccessful) {
                                            selectedExercise = null
                                        } else {
                                            errorMessage = "운동 추가 실패: ${response.errorBody()?.string()}"
                                        }
                                    } catch (e: Exception) {
                                        errorMessage = "오류 발생: ${e.localizedMessage}"
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
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .background(Color.DarkGray, shape = CircleShape)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        BasicTextField(
            value = query,
            onValueChange = { onQueryChanged(it) },
            singleLine = true,
            textStyle = TextStyle(fontSize = 16.sp, color = Color.White),
            modifier = Modifier.fillMaxWidth(),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (query.isEmpty()) {
                        Text(
                            text = "운동 검색...",
                            style = TextStyle(fontSize = 16.sp, color = Color.Gray)
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@Composable
fun CustomRoutine(navController: NavController) {
    var selectedTarget by remember { mutableStateOf("가슴") }
    var showRoutineDialog by remember { mutableStateOf(true) }
    var selectedRoutine by remember { mutableStateOf<RoutineDto?>(null) }
    var userId by remember { mutableStateOf<Long?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val token = PreferenceManager.getAccessToken()
        if (token != null) {
            try {
                val response = RetrofitInstance.api.getMyInfo()
                if (response.isSuccessful) {
                    userId = response.body()?.data?.userId
                }

                if (userId == null) return@LaunchedEffect

                val routinesResponse = RetrofitInstance.api.getRoutineList()
                val routines = routinesResponse.body() ?: emptyList()

                if (routines.isEmpty()) {
                    val defaultRoutine = RoutineDto(userId = userId!!, name = "루틴 1")
                    val createResponse = RetrofitInstance.api.createRoutine(defaultRoutine)
                    if (createResponse.isSuccessful) {
                        selectedRoutine = createResponse.body()
                    }
                } else {
                    selectedRoutine = routines.firstOrNull()
                }
            } catch (e: Exception) {
                println("Error: ${e.localizedMessage}")
            }
        }
    }

    if (showRoutineDialog) {
        RoutineDialog(
            onDismiss = { showRoutineDialog = false },
            onRoutineSelected = { routine ->
                selectedRoutine = routine
                showRoutineDialog = false
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
            if (selectedRoutine != null) {
                Text(
                    text = "선택된 루틴: ${selectedRoutine?.name} (ID: ${selectedRoutine?.routineId})",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            } else {
                Text(
                    text = "루틴을 선택하세요",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            SearchBar(query = searchQuery, onQueryChanged = { searchQuery = it })

            CategoryFilters(selectedTarget) { target ->
                selectedTarget = target
            }

            Spacer(modifier = Modifier.height(16.dp))

            selectedRoutine?.routineId?.let { routineId ->
                ExerciseList(
                    selectedTarget = selectedTarget,
                    selectedRoutineId = routineId,
                    searchQuery = searchQuery
                )
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

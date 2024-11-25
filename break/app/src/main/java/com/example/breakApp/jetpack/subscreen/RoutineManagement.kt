package com.example.breakApp.jetpack.subscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import com.example.breakApp.api.model.RoutineDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineManagement(navController: NavController, routineId: Long, routineName: String) {
    var currentRoutineName by remember { mutableStateOf(routineName) }
    var newRoutineName by remember { mutableStateOf(routineName) } // 새로운 이름 입력
    var isEditing by remember { mutableStateOf(false) } // 편집 상태 관리
    var exercises by remember { mutableStateOf<List<Exercise>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var userId by remember { mutableStateOf<Long?>(null) } // 사용자 ID 상태

    // 사용자 ID 가져오기
    LaunchedEffect(Unit) {
        try {
            val response = RetrofitInstance.api.getMyInfo()
            if (response.isSuccessful) {
                userId = response.body()?.data?.userId
            } else {
                errorMessage = "Error fetching user info: ${response.errorBody()?.string()}"
            }
        } catch (e: Exception) {
            errorMessage = "An error occurred: ${e.localizedMessage}"
        }
    }

    // API 호출: 루틴에 포함된 운동 데이터 가져오기
    LaunchedEffect(routineId) {
        try {
            isLoading = true
            val response = RetrofitInstance.api.getExercisesByRoutineId(routineId)
            if (response.isSuccessful) {
                exercises = response.body() ?: emptyList()
            } else {
                errorMessage = "Error: ${response.errorBody()?.string()}"
            }
        } catch (e: Exception) {
            errorMessage = "An error occurred: ${e.localizedMessage}"
        } finally {
            isLoading = false
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
            if (isLoading) {
                // 로딩 상태
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (errorMessage != null) {
                // 에러 메시지 표시
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                // 운동 데이터 표시
                exercises.forEach { exercise ->
                    ExerciseInputSection(exercise.name)
                }

                // 버튼을 화면 너비에 꽉 차게 배치
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { /* 저장 로직 */ },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("저장", color = Color.White)
                    }
                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("취소", color = Color.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseInputSection(exerciseName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = exerciseName,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InputField(label = "반복 횟수")
            InputField(label = "세트 수")
            InputField(label = "중량 (kg)")
        }
    }
}

@Composable
fun InputField(label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
        BasicTextField(
            value = "",
            onValueChange = { /* Value change logic */ },
            modifier = Modifier
                .width(80.dp)
                .height(40.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(8.dp),
            textStyle = TextStyle(fontSize = 14.sp, color = Color.Black)
        )
    }
}

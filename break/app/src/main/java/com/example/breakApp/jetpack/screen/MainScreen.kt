package com.example.breakApp.jetpack.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.breakApp.MainActivity
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.api.model.RoutineDto
import com.example.breakApp.jetpack.tools.BottomNavigationBar
import com.example.breakApp.jetpack.tools.RoutineDialog
import com.example.breakApp.tools.PreferenceManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    var routines by remember { mutableStateOf<List<RoutineDto>>(emptyList()) } // 루틴 목록 상태
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var userName by remember { mutableStateOf("로딩 중...") }
    val scope = rememberCoroutineScope()

    // 서버에서 루틴 목록 가져오기
    LaunchedEffect(Unit) {
        try {
            isLoading = true
            val response = RetrofitInstance.api.getRoutineList()
            if (response.isSuccessful) {
                routines = response.body() ?: emptyList()
            } else {
                errorMessage = "Error: ${response.errorBody()?.string()}"
            }
        } catch (e: Exception) {
            errorMessage = "An error occurred: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val token = PreferenceManager.getAccessToken()
                if (!token.isNullOrEmpty()) {
                    val response = RetrofitInstance.api.getMyInfo()
                    if (response.isSuccessful && response.body()?.data != null) {
                        userName = response.body()?.data?.userName ?: "알 수 없음"
                    } else {
                        userName = "정보를 불러올 수 없습니다."
                    }
                } else {
                    userName = "로그인이 필요합니다."
                }
            } catch (e: Exception) {
                userName = "오류 발생: ${e.message}"
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { com.example.breakApp.jetpack.tools.TopAppBar(navController) },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                selectedItemIndex = selectedItem // 선택된 아이템 인덱스 전달
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            item {
                Text(
                    text = "내일 할 일",
                    style = MaterialTheme.typography.titleSmall
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp) // 높이 설정
                        .background(Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)) // 네모난 모양
                        .clickable { /* 클릭 시 다이얼로그 표시 */ } // 클릭 시 다이얼로그 표시
                        .padding(16.dp)
                )
            }

            item {
                Text(
                    text = userName + "님의 루틴 목록",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (errorMessage != null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            color = Color.Red
                        )
                    }
                }
            } else {
                items(routines) { routine ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .padding(vertical = 8.dp)
                            .background(Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp))
                            .clickable {
                                navController.navigate("routineManagement/${routine.routineId}/${routine.name}")
                            }
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = routine.name, style = MaterialTheme.typography.bodyLarge)
                    }

                }
            }
        }
    }
}

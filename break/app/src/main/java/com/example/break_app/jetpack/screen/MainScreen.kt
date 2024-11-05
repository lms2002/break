package com.example.break_app.jetpack.screen

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
import com.example.break_app.jetpack.tools.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { com.example.break_app.jetpack.tools.TopAppBar(navController) },
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
                ) {
                    // 여기에 다이얼로그를 띄우는 로직 추가 가능
                }
            }

            item {
                Text(
                    text = "내 루틴",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            // 루틴 목록 (예시)
            val routines = listOf("루틴 1", "루틴 2", "루틴 3", "루틴 4", "루틴 5", "루틴 6")

            items(routines) { routine ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp) // 루틴 박스 높이 설정
                        .padding(vertical = 8.dp)
                        .background(Color.Gray.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp)) // 박스 모양
                        .clickable { /* 루틴 클릭 시 동작 추가 */ }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center // 내용 중앙 정렬
                ) {
                    // 여기에 사진 및 이름 표시 가능
                    Text(text = routine, style = MaterialTheme.typography.bodyLarge)
                    // 예: Image(painter = painterResource(id = R.drawable.example_image), contentDescription = "루틴 이미지")
                }
            }
        }
    }
}

/*
241101_내일 할 일, 루틴 부분 박스로 변경 완료

 */
package com.example.break_app.jetpack.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.break_app.R
import com.example.break_app.jetpack.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, selectedItemIndex: Int) {
    var selectedTab by remember { mutableStateOf(0) } // 선택된 탭 상태

    Scaffold(
        bottomBar = { BottomNavigationBar(navController, selectedItemIndex = 2) },
        topBar = {
            TopAppBar(
                title = { Text("기록") },
                actions = {
                    IconButton(onClick = { /* 추가 버튼 클릭 시 동작 */
                        navController.navigate("planSection") }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_add), // + 아이콘
                            contentDescription = "Add"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 탭 레이아웃
            TabRow(selectedTabIndex = selectedTab) {
                val tabs = listOf("운동", "사진", "신체")
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            // 필요한 동작 추가
                        },
                        text = { Text(title) }
                    )
                }
            }

            // 탭에 따른 내용 표시
            when (selectedTab) {
                0 -> ExerciseContent() // 운동 탭 내용
                1 -> PhotoContent() // 사진 탭 내용
                2 -> BodyContent() // 신체 탭 내용
            }
        }
    }
}


@Composable
fun ExerciseContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "운동 탭 내용", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun PhotoContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "사진 탭 내용", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun BodyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "신체 탭 내용", style = MaterialTheme.typography.bodyLarge)
    }
}

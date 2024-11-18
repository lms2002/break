package com.example.breakApp.jetpack.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.breakApp.R
import com.example.breakApp.jetpack.tools.BottomNavigationBar
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import com.example.breakApp.jetpack.subscreen.*

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
            TabRow(
                selectedTabIndex = selectedTab,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Color(0xFF8B0000) // 테마 색상
                    )
                }
            ) {
                val tabs = listOf("운동", "사진", "인바디")
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title) }
                    )
                }
            }

            // 탭에 따른 내용 표시
            when (selectedTab) {
                0 -> HistoryExercise(navController) // 운동 탭 내용
                1 -> HistoryPhoto() // 사진 탭 내용
                2 -> HistoryBody(navController) // 신체 탭 내용
            }
        }
    }
}



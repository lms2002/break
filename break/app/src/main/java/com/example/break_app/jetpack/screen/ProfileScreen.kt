package com.example.break_app.jetpack.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.break_app.R
import com.example.break_app.jetpack.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, selectedItemIndex: Int) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("내 정보") },
                actions = {
                    // 리스트 모양 아이콘
                    IconButton(onClick = { /* 리스트 클릭 시 동작 */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_list), // 리스트 아이콘
                            contentDescription = "List"
                        )
                    }
                    // 알림 아이콘
                    IconButton(onClick = { navController.navigate("notification") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_notifications), // 알림 아이콘
                            contentDescription = "Notifications"
                        )
                    }
                    // 설정 아이콘
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_setting), // 설정 아이콘
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(selectedItemIndex = 1, navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 사용자 프로필 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile), // 사용자 아이콘
                    contentDescription = "User",
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "오덕춘", style = MaterialTheme.typography.titleLarge) // 사용자가 로그인 했을 때 이름으로 변경
            }

            // 메뉴 항목
            val menuItems = listOf(
                "내 루틴",
                "모든 메모",
                "친구",
                "친구 초대"
            )

            menuItems.forEach { item ->
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* 클릭 시 동작 추가 */ }
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // 피드백 섹션
            Text(
                text = "피드백",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp)
            )

            val feedbackItems = listOf(
                "운동 추가 요청하기",
                "간단하게 리뷰 남기기",
                "건의사항 쪽지 보내기"
            )

            feedbackItems.forEach { feedback ->
                Text(
                    text = feedback,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* 클릭 시 동작 추가 */ }
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

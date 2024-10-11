package com.example.break_app.jetpack.subscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.break_app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanSection(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("새로운 운동 계획") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        // ic_back 아이콘 사용
                        Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = "Back")
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
            // 날짜 선택 버튼
            Button(
                onClick = { /* 날짜 선택 로직 추가 */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("요일 선택")
            }

            Text(
                text = "선택 된 날짜의 시간 선택",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            // 시간 선택 버튼 (예: 자유 운동)
            Button(
                onClick = { /* 시간 선택 로직 추가 */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("자유 운동")
            }

            // 추가 버튼
            Button(
                onClick = { /* 루틴 추가 로직 추가 */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("루틴 추가")
            }

            // 하단에 일정 가져오기 버튼
            Button(
                onClick = { /* 일정 가져오기 로직 추가 */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("일정 가져오기")
            }
        }
    }
}

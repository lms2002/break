package com.example.break_app.jetpack.subscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.break_app.R

import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanSection(navController: NavController) {
    var isTimerVisible by remember { mutableStateOf(false) }
    var isTimerRunning by remember { mutableStateOf(false) }
    var isTimerStarted by remember { mutableStateOf(false) }
    var seconds by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("새로운 운동 계획") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { /* 완료 버튼 로직 */ }) {
                        Text("완료", color = Color(0xFF8B0000))
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
            // 날짜 및 시간
            Text(
                text = "2024.9.27 (금) AM 14:20",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            // 날짜 선택 버튼
            Text(text = "날짜", color = Color.White, fontSize = 16.sp)
            Button(
                onClick = { /* 요일 선택 로직 추가 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("요일 선택", color = Color.Black)
            }

            // 선택된 날짜의 시간 선택
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.DarkGray, shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("선택 된 날짜의 시간 선택", color = Color.Black)
            }

            // 일정 가져오기 버튼
            Button(
                onClick = { /* 일정 가져오기 로직 추가 */ },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("일정 가져오기", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 운동 제목
            Text(text = "운동", color = Color.White, fontSize = 16.sp)

            // 자유 운동과 루틴 추가 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 자유 운동 버튼
                Button(
                    onClick = { isTimerVisible = true },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("자유 운동", color = Color.White)
                }

                // 루틴 추가 버튼
                Button(
                    onClick = { navController.navigate("customExercise") },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("루틴 추가", color = Color.Black)
                }
            }

            // 타이머 표시
            if (isTimerVisible) {
                Timer(
                    seconds = seconds,
                    isRunning = isTimerRunning,
                    isStarted = isTimerStarted,
                    onStart = {
                        isTimerRunning = true
                        isTimerStarted = true // 타이머 시작 상태로 전환
                    },
                    onPause = { isTimerRunning = false },
                    onReset = {
                        isTimerRunning = false
                        isTimerStarted = false // 타이머가 종료되면 다시 초기 상태로
                        seconds = 0
                        isTimerVisible = false
                    }
                )
            }

            // 타이머 동작
            LaunchedEffect(isTimerRunning) {
                while (isTimerRunning) {
                    delay(1000L) // 1초 대기
                    seconds += 1
                }
            }
        }
    }
}

@Composable
fun Timer(
    seconds: Int,
    isRunning: Boolean,
    isStarted: Boolean,
    onStart: () -> Unit,
    onReset: () -> Unit,
    onPause: () -> Unit
) {
    val minutes = seconds / 60
    val displaySeconds = seconds % 60

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        // 타이머 텍스트
        Text(
            text = String.format("%02d:%02d", minutes, displaySeconds),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 시작 및 종료 버튼
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            if (isRunning) {
                Button(onClick = { onPause() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000))) {
                    Text("일시 정지", color = Color.White)
                }
            } else {
                Button(onClick = { onStart() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000))) {
                    Text(if (isStarted) "재시작" else "시작", color = Color.White)
                }
            }
            Button(onClick = { onReset() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000))) {
                Text("종료", color = Color.White)
            }
        }
    }
}

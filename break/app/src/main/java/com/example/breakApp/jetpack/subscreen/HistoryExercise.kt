package com.example.breakApp.jetpack.subscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.breakApp.jetpack.tools.Calendar

@Composable
fun HistoryExercise(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "운동 기록", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))

        // Calendar 컴포넌트 호출
        Calendar(onDateSelected = { year, month, day ->
            // 날짜 선택 시 dailyMemo 화면으로 이동
            val selectedDate = "$year-${month + 1}-$day" // 월은 0부터 시작하므로 +1 필요
            navController.navigate("dailyMemo/$selectedDate")
        })
    }
}
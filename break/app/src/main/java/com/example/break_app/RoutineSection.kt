package com.example.break_app

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun RoutineSection() {
    // 루틴 섹션
    Text(text = "루틴", style = MaterialTheme.typography.titleMedium)

    // 루틴 목록
    val routines = listOf("루틴1", "루틴2", "루틴3", "루틴4", "루틴5")
    //이 부분을 데이터베이스로 수정하기

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        items(routines) { routine ->
            RoutineItem(routine)
        }
    }
}

@Composable
fun RoutineItem(routine: String) {
    // 루틴 항목
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray), // 테두리 추가
        contentAlignment = Alignment.Center
    ) {
        Text(text = routine)
    }
}
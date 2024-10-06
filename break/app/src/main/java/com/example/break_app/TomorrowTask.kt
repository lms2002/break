package com.example.break_app

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TomorrowTasks() {
    Text(text = "내일 할 일", style = MaterialTheme.typography.titleMedium)

    var task by remember { mutableStateOf("") }
    BasicTextField(
        value = task,
        onValueChange = { task = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .height(100.dp) // 높이 설정
            .border(width = 1.dp, color = Color.Gray) // 테두리 추가
            .padding(8.dp), // 안쪽 패딩
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black)
    )
}
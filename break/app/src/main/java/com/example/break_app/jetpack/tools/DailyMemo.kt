package com.example.break_app.jetpack.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyMemoScreen(
    navController: NavController,
    selectedDate: String // 선택된 날짜를 문자열 형식으로 전달
) {
    var memoText by remember { mutableStateOf("") } // 메모 텍스트 상태

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = selectedDate) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
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
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "여기에 메모를 입력할 수 있어요.",
                style = TextStyle(fontSize = 20.sp, color = Color.Gray),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // 메모 입력 필드
            BasicTextField(
                value = memoText,
                onValueChange = { memoText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.LightGray)
                    .padding(16.dp),
                textStyle = TextStyle(fontSize = 18.sp, color = Color.Black)
            )
        }
    }
}

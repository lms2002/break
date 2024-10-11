package com.example.break_app.jetpack.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.break_app.jetpack.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    var selectedItem by remember { mutableStateOf(0) }
    var taskInput by remember { mutableStateOf("") }
    var tasks by remember { mutableStateOf(listOf<String>()) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { com.example.break_app.jetpack.TopAppBar(navController) },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                selectedItemIndex = selectedItem // 선택된 아이템 인덱스 전달
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // 내일 할 일 섹션
            Text(
                text = "내일 할 일",
                style = MaterialTheme.typography.titleMedium
            )
            TextField(
                value = taskInput,
                onValueChange = { taskInput = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("할 일을 입력하세요") }
            )
            Button(
                onClick = {
                    if (taskInput.isNotEmpty()) {
                        tasks = tasks + taskInput
                        taskInput = ""
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("추가")
            }

            // 추가된 할 일 목록
            tasks.forEach { task ->
                Text(
                    text = task,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { /* 클릭 시 동작 추가 */ }
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // 내 루틴 섹션
            Text(
                text = "내 루틴",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp)
            )

            // 루틴 목록 (예시)
            val routines = listOf("루틴 1", "루틴 2", "루틴 3", "루틴 4")
            routines.forEach { routine ->
                Text(
                    text = routine,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable { /* 루틴 클릭 시 동작 추가 */ }
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

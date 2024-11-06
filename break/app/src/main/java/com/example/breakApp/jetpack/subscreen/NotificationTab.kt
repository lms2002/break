package com.example.breakApp.jetpack.subscreen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.breakApp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTab(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("알림") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(painter = painterResource(id = R.drawable.ic_back), contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* 삭제 기능 추가 */ }) {
                        Icon(painter = painterResource(id = R.drawable.ic_delete), contentDescription = "Delete")
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
            // 알림 항목
            repeat(4) { // 4개의 알림 항목 생성
                Text(
                    text = "알림",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp), // 높이를 설정하여 일관된 UI 유지
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

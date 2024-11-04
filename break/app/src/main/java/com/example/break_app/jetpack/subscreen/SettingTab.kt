package com.example.break_app.jetpack.subscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.break_app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingTab(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("설정") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
            // 알림 설정
            Text(
                text = "알림 설정",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* 알림 설정 클릭 시 동작 추가 */ }
                    .padding(16.dp)
            )

            // 개인정보 변경
            Text(
                text = "개인정보 변경",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* 개인정보 변경 클릭 시 동작 추가 */ }
                    .padding(16.dp)
            )

            // 고객센터
            Text(
                text = "고객센터",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* 고객센터 클릭 시 동작 추가 */ }
                    .padding(16.dp)
            )
        }
    }
}

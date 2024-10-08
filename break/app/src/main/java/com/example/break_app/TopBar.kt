package com.example.break_app

import androidx.compose.foundation.Image
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar() {
    androidx.compose.material3.TopAppBar(
        title = { Text("홈") }, // 상단 바 제목
        actions = {
            // 우측 아이콘 추가
            IconButton(onClick = { /* 추가 버튼 클릭 시 동작 */ }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_add), // + 아이콘
                    contentDescription = "Add"
                )
            }
            IconButton(onClick = { /* 알림 버튼 클릭 시 동작 */ }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_notifications), // 알림 아이콘
                    contentDescription = "Notifications"
                )
            }
            IconButton(onClick = { /* 설정 버튼 클릭 시 동작 */ }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_setting), // 설정 아이콘
                    contentDescription = "Settings"
                )
            }
        }
    )
}
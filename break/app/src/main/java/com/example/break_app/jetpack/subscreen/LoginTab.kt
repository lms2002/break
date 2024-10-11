package com.example.break_app.jetpack.subscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun LoginTab(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), // 배경 색상
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 아이디 입력 필드
            BasicTextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
                    .padding(16.dp),
                decorationBox = { innerTextField ->
                    if (username.isEmpty()) {
                        Text(text = "아이디", color = Color.LightGray)
                    }
                    innerTextField()
                }
            )

            // 비밀번호 입력 필드
            BasicTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
                    .padding(16.dp),
                decorationBox = { innerTextField ->
                    if (password.isEmpty()) {
                        Text(text = "비밀번호", color = Color.LightGray)
                    }
                    innerTextField()
                }
            )

            // 로그인 버튼
            Button(
                onClick = {
                    // 로그인 로직 추가
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(text = "로그인")
            }

            // 회원가입 버튼
            Text(
                text = "회원가입",
                color = Color.White,
                modifier = Modifier
                    .clickable {
                        navController.navigate("signUpTab") // 회원가입 화면으로 이동
                    }
                    .padding(vertical = 8.dp)
            )
        }
    }
}

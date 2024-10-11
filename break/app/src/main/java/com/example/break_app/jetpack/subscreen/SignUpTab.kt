package com.example.break_app.jetpack.subscreen

import androidx.compose.foundation.background
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
fun SignUpTab(navController: NavController) {
    // 입력 필드 상태 변수
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var targetWeight by remember { mutableStateOf("") }

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
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            // 아이디 입력 필드
            Text(text = "아이디", color = Color.White)
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
                        Text(text = "아이디 입력", color = Color.LightGray)
                    }
                    innerTextField()
                }
            )

            // 비밀번호 입력 필드
            Text(text = "비밀번호", color = Color.White)
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
                        Text(text = "비밀번호 입력", color = Color.LightGray)
                    }
                    innerTextField()
                }
            )

            // 비밀번호 확인 입력 필드
            Text(text = "비밀번호 확인", color = Color.White)
            BasicTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
                    .padding(16.dp),
                decorationBox = { innerTextField ->
                    if (confirmPassword.isEmpty()) {
                        Text(text = "비밀번호 확인", color = Color.LightGray)
                    }
                    innerTextField()
                }
            )

            // 이름 입력 필드
            Text(text = "이름", color = Color.White)
            BasicTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
                    .padding(16.dp),
                decorationBox = { innerTextField ->
                    if (name.isEmpty()) {
                        Text(text = "이름 입력", color = Color.LightGray)
                    }
                    innerTextField()
                }
            )

            // 생년월일 입력 필드
            Text(text = "생년월일", color = Color.White)
            BasicTextField(
                value = birthDate,
                onValueChange = { birthDate = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
                    .padding(16.dp),
                decorationBox = { innerTextField ->
                    if (birthDate.isEmpty()) {
                        Text(text = "YYYY-MM-DD", color = Color.LightGray)
                    }
                    innerTextField()
                }
            )

            // 목표 체중 입력 필드
            Text(text = "목표 체중", color = Color.White)
            BasicTextField(
                value = targetWeight,
                onValueChange = { targetWeight = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
                    .padding(16.dp),
                decorationBox = { innerTextField ->
                    if (targetWeight.isEmpty()) {
                        Text(text = "목표 체중 입력", color = Color.LightGray)
                    }
                    innerTextField()
                }
            )

            // 확인 버튼
            Button(
                onClick = {
                    // 회원가입 로직 추가
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "확인", color = Color.White)
            }
        }
    }
}

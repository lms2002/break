package com.example.breakApp.jetpack.subscreen

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

    // 공통 Modifier 정의
    val inputFieldModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
        .height(32.dp)
        .background(Color.Gray, shape = MaterialTheme.shapes.small)
        .padding(16.dp)

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
                modifier = inputFieldModifier // 공통 Modifier 사용
            )
            Text(
                text = "아이디는 영문과 숫자를 포함해야 합니다.",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.End) // 오른쪽 정렬
            )

            // 비밀번호 입력 필드
            Text(text = "비밀번호", color = Color.White)
            BasicTextField(
                value = password,
                onValueChange = { password = it },
                modifier = inputFieldModifier // 공통 Modifier 사용
            )
            Text(
                text = "비밀번호는 8자 이상이어야 합니다.",
                color = Color.LightGray,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.End) // 오른쪽 정렬
            )

            // 비밀번호 확인 입력 필드
            Text(text = "비밀번호 확인", color = Color.White)
            BasicTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = inputFieldModifier // 공통 Modifier 사용
            )

            // 이름 입력 필드
            Text(text = "이름", color = Color.White)
            BasicTextField(
                value = name,
                onValueChange = { name = it },
                modifier = inputFieldModifier // 공통 Modifier 사용
            )

            // 생년월일 입력 필드
            Text(text = "생년월일", color = Color.White)
            BasicTextField(
                value = birthDate,
                onValueChange = { birthDate = it },
                modifier = inputFieldModifier // 공통 Modifier 사용
            )

            // 목표 체중 입력 필드
            Text(text = "목표 체중", color = Color.White)
            BasicTextField(
                value = targetWeight,
                onValueChange = { targetWeight = it },
                modifier = inputFieldModifier // 공통 Modifier 사용
            )

            // 확인 버튼
            Button(
                onClick = {
                    /**
                     * 반드시
                     * 여기에
                     * 회원가입
                     * 코드를
                     * 만들 것.
                     */
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "가입하기", color = Color.Black)
            }
        }
    }
}

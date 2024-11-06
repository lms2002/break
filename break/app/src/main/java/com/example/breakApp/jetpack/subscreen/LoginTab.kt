package com.example.breakApp.jetpack.subscreen

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.breakApp.R

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
            // 애플리케이션 아이콘
            Image(
                painter = painterResource(id = R.drawable.ic_setting),
                contentDescription = "App Icon",
                modifier = Modifier
                    .size(80.dp) // 아이콘 크기 설정
                    .padding(bottom = 24.dp), // 아이콘과 입력 필드 사이 간격
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White) // 아이콘 색상 변경
            )

            // 아이디 입력 필드
            BasicTextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp) // 입력 칸 간격 조절
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
                    .padding(12.dp) // 입력 칸 내부 패딩 조절
            )

            // 비밀번호 입력 필드
            BasicTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp) // 입력 칸 간격 조절
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
                    .padding(12.dp) // 입력 칸 내부 패딩 조절
            )

            // 로그인 버튼
            Button(
                onClick = {
                    /**
                     * 여기에도
                     * 로그인
                     * 코드를
                     * 구현해
                     * 주세요
                     */
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(text = "로그인")
            }

            // 회원가입 버튼 (작은 크기로 로그인 버튼 오른쪽 아래에 배치)
            Text(
                text = "회원가입",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall, // 텍스트 크기 작게 조정
                modifier = Modifier
                    .align(Alignment.End) // 오른쪽 아래 정렬
                    .padding(top = 8.dp)
                    .clickable {
                        navController.navigate("signUpTab") // 회원가입 화면으로 이동
                    }
            )
        }
    }
}

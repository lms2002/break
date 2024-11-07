package com.example.breakApp.jetpack.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.breakApp.R

@Composable
fun LoginScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black), // 배경 색상
        contentAlignment = Alignment.Center
    ) {
        // 중앙의 로고 원
        Box(
            modifier = Modifier
                .size(120.dp) // 원의 크기
                .offset(y = -90.dp)
                .background(Color.Gray, shape = CircleShape), // 원의 배경 색상
            contentAlignment = Alignment.Center
        ) {
            // 아이콘 추가 (여기서는 설정 아이콘 사용)
            Image(
                painter = painterResource(id = R.drawable.ic_setting),
                contentDescription = "Logo",
                modifier = Modifier.size(60.dp) // 아이콘 크기
            )
        }

        // 로그인 버튼
        Button(
            onClick = { navController.navigate("loginTab") }, // 로그인 탭으로 이동
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(0.8f) // 버튼 너비 설정
                .align(Alignment.BottomCenter)
                .offset(y = -160.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red) // 버튼 색상
        ) {
            Text(text = "로그인", color = Color.White) // 버튼 텍스트
        }

        // 회원가입 버튼
        Button(
            onClick = { navController.navigate("signUpTab") }, // 회원가입 탭으로 이동
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(0.8f) // 버튼 너비 설정
                .align(Alignment.BottomCenter)
                .offset(y = -100.dp), // 로그인 버튼 아래로 이동
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray) // 버튼 색상
        ) {
            Text(text = "회원가입", color = Color.White) // 버튼 텍스트
        }
    }
}

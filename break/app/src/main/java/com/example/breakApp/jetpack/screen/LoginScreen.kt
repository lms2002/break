package com.example.breakApp.jetpack.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.breakApp.R
import com.example.breakApp.tools.PreferenceManager

@Composable
fun LoginScreen(navController: NavController) {
    // 명시적으로 토큰 초기화
    fun clearToken() {
        PreferenceManager.clearAccessToken()
        println("AccessToken이 초기화되었습니다.")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // 중앙의 로고 원
        Box(
            modifier = Modifier
                .size(120.dp)
                .offset(y = -90.dp)
                .background(Color.Gray, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_setting),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(60.dp)
                    .clickable { clearToken() } // 클릭 시 토큰 초기화
            )
        }

        // 로그인 버튼
        Button(
            onClick = {
                clearToken() // 로그인 전 기존 토큰 초기화
                navController.navigate("loginTab")
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(0.8f)
                .align(Alignment.BottomCenter)
                .offset(y = -160.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(text = "로그인", color = Color.White)
        }

        // 회원가입 버튼
        Button(
            onClick = {
                navController.navigate("signUpTab")
            },
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth(0.8f)
                .align(Alignment.BottomCenter)
                .offset(y = -100.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
        ) {
            Text(text = "회원가입", color = Color.White)
        }
    }
}

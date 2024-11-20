package com.example.breakApp.jetpack.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.tools.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(navController: NavController) {
    var skipValidation by remember { mutableStateOf(false) }

    // 앱 시작 시 토큰 유효성 검사
    LaunchedEffect(Unit) {
        if (!skipValidation) {
            checkTokenAndUserValidity(navController)
        }
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
                modifier = Modifier.size(60.dp)
            )
        }

        // 로그인 버튼
        Button(
            onClick = {
                skipValidation = true
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
                skipValidation = true
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

/**
 * 토큰 및 사용자 정보를 확인하고 적절히 화면 전환
 */
private fun checkTokenAndUserValidity(navController: NavController) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val token = PreferenceManager.getAccessToken()

            if (token.isNullOrEmpty()) {
                // 토큰이 없으면 로그인 화면 유지
                withContext(Dispatchers.Main) {
                    navController.navigate("loginScreen")
                }
                return@launch
            }

            // 토큰 유효성 검사
            val tokenResponse = RetrofitInstance.api.validateToken("Bearer $token")
            if (tokenResponse.isSuccessful && tokenResponse.body()?.data == true) {
                // 사용자 정보 확인
                val userResponse = RetrofitInstance.api.getMyInfo("Bearer $token")
                withContext(Dispatchers.Main) {
                    if (userResponse.isSuccessful && userResponse.body()?.data != null) {
                        // 사용자 정보가 존재하면 MainScreen으로 이동
                        navController.navigate("mainScreen") {
                            popUpTo("loginScreen") { inclusive = true }
                        }
                    } else {
                        // 사용자 정보가 없으면 토큰 삭제 후 LoginScreen 유지
                        PreferenceManager.clearAccessToken()
                        navController.navigate("loginScreen")
                    }
                }
            } else {
                // 토큰이 유효하지 않으면 토큰 삭제 후 LoginScreen 유지
                PreferenceManager.clearAccessToken()
                withContext(Dispatchers.Main) {
                    navController.navigate("loginScreen")
                }
            }
        } catch (e: Exception) {
            // 예외 발생 시 토큰 삭제 후 LoginScreen 유지
            PreferenceManager.clearAccessToken()
            withContext(Dispatchers.Main) {
                navController.navigate("loginScreen")
            }
        }
    }
}

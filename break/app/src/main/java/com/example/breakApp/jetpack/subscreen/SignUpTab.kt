package com.example.breakApp.jetpack.subscreen

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.breakApp.api.RetrofitInstance
import androidx.navigation.NavController
import com.example.breakApp.api.model.MemberDtoRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SignUpTab(navController: NavController) {
    // 입력 필드 상태 변수
    var loginId by remember { mutableStateOf("") } // 아이디
    var password by remember { mutableStateOf("") } // 비밀번호
    var confirmPassword by remember { mutableStateOf("") } // 비밀번호 확인
    var userName by remember { mutableStateOf("") } // 이름
    var email by remember { mutableStateOf("") } // 이메일
    var gender by remember { mutableStateOf("") } // 성별
    val scope = rememberCoroutineScope()

    // 공통 Modifier 정의
    val inputFieldModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
        .height(48.dp)
        .background(Color.Gray, shape = MaterialTheme.shapes.small)
        .padding(horizontal = 16.dp, vertical = 8.dp)

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
                value = loginId,
                onValueChange = { loginId = it },
                modifier = inputFieldModifier,
                textStyle = TextStyle(color = Color.White, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            )

            // 비밀번호 입력 필드
            Text(text = "비밀번호", color = Color.White)
            BasicTextField(
                value = password,
                onValueChange = { password = it },
                modifier = inputFieldModifier,
                visualTransformation = PasswordVisualTransformation(),
                textStyle = TextStyle(color = Color.White, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            )

            // 비밀번호 확인 입력 필드
            Text(text = "비밀번호 확인", color = Color.White)
            BasicTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = inputFieldModifier,
                visualTransformation = PasswordVisualTransformation(),
                textStyle = TextStyle(color = Color.White, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            )

            // 이메일 입력 필드
            Text(text = "이메일", color = Color.White)
            BasicTextField(
                value = email,
                onValueChange = { email = it },
                modifier = inputFieldModifier,
                textStyle = TextStyle(color = Color.White, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            )

            // 이름 입력 필드
            Text(text = "이름", color = Color.White)
            BasicTextField(
                value = userName,
                onValueChange = { userName = it },
                modifier = inputFieldModifier,
                textStyle = TextStyle(color = Color.White, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            )

            // 성별 입력 필드
            Text(text = "성별 (MALE/FEMALE)", color = Color.White)
            BasicTextField(
                value = gender,
                onValueChange = { gender = it },
                modifier = inputFieldModifier,
                textStyle = TextStyle(color = Color.White, fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            )

            // 가입하기 버튼
            Button(
                onClick = {
                    if (loginId.isBlank() || password.isBlank() || confirmPassword.isBlank() || userName.isBlank() || email.isBlank() || gender.isBlank()) {
                        Log.d("SignUpTab", "모든 필드를 입력해주세요.")
                    } else if (password != confirmPassword) {
                        Log.d("SignUpTab", "비밀번호가 일치하지 않습니다.")
                    } else {
                        scope.launch {
                            try {
                                // 서버에 회원가입 요청
                                val response = RetrofitInstance.api.signUp(
                                    MemberDtoRequest(
                                        loginId = loginId,
                                        password = password,
                                        userName = userName,
                                        email = email,
                                        gender = gender
                                    )
                                )

                                if (response.isSuccessful) {
                                    // 성공 시 메인 화면으로 이동
                                    Log.d("SignUpTab", "회원가입 성공: ${response.body()}")
                                    navController.navigate("mainScreen") {
                                        popUpTo("signUpTab") { inclusive = true }
                                    }
                                } else {
                                    // 실패한 경우 로그 출력
                                    Log.d("SignUpTab", "회원가입 실패: ${response.errorBody()?.string()}")
                                }
                            } catch (e: Exception) {
                                // 예외 처리 (네트워크 오류 등)
                                Log.d("SignUpTab", "오류 발생: ${e.message}")
                            }
                        }
                    }
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

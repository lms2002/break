package com.example.breakApp.jetpack.subscreen

import android.util.Log
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.api.model.MemberDtoRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun SignUpTab(navController: NavController) {
    // 입력 필드 상태 변수
    var loginId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var verificationCode by remember { mutableStateOf("") }

    // UI 상태 변수
    var statusMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    val inputFieldModifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)
        .height(48.dp)
        .background(Color.Gray, shape = MaterialTheme.shapes.small)
        .padding(horizontal = 16.dp, vertical = 8.dp)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            // 상태 메시지 표시
            if (statusMessage.isNotBlank()) {
                Text(
                    text = statusMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Text(text = "아이디", color = Color.White)
            BasicTextField(
                value = loginId,
                onValueChange = { loginId = it },
                modifier = inputFieldModifier,
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp)
            )

            Text(text = "비밀번호", color = Color.White)
            BasicTextField(
                value = password,
                onValueChange = { password = it },
                modifier = inputFieldModifier,
                visualTransformation = PasswordVisualTransformation(),
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp)
            )

            Text(text = "비밀번호 확인", color = Color.White)
            BasicTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = inputFieldModifier,
                visualTransformation = PasswordVisualTransformation(),
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp)
            )

            Text(text = "이메일", color = Color.White)
            BasicTextField(
                value = email,
                onValueChange = { email = it },
                modifier = inputFieldModifier,
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp)
            )

            Text(text = "이메일 인증", color = Color.White)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                BasicTextField(
                    value = verificationCode,
                    onValueChange = { verificationCode = it },
                    modifier = Modifier
                        .weight(7f)
                        .height(48.dp)
                        .background(Color.Gray, shape = MaterialTheme.shapes.small)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    textStyle = TextStyle(color = Color.White, fontSize = 16.sp)
                )

                Box(
                    modifier = Modifier
                        .weight(3f)
                        .padding(start = 8.dp)
                        .height(48.dp)
                        .background(Color(0xFFBA0000), shape = MaterialTheme.shapes.small)
                        .clickable {
                            scope.launch {
                                try {
                                    val response = RetrofitInstance.api.requestEmailVerification(email)
                                    withContext(Dispatchers.Main) {
                                        if (response.isSuccessful) {
                                            val message = response.body()
                                            statusMessage = "이메일 전송 성공: $message"
                                            Log.d("SignUpTab", statusMessage)
                                        } else {
                                            statusMessage = "이메일 전송 실패: ${response.errorBody()?.string()}"
                                            Log.d("SignUpTab", statusMessage)
                                        }
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        statusMessage = "오류 발생: ${e.message}"
                                        Log.e("SignUpTab", statusMessage)
                                    }
                                }
                            }
                        }
                ) {
                    Text(
                        text = "전송",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }

            Text(text = "이름", color = Color.White)
            BasicTextField(
                value = userName,
                onValueChange = { userName = it },
                modifier = inputFieldModifier,
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp)
            )

            Text(text = "성별 (MALE/FEMALE)", color = Color.White)
            BasicTextField(
                value = gender,
                onValueChange = { gender = it },
                modifier = inputFieldModifier,
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp)
            )

            Button(
                onClick = {
                    when {
                        email.isBlank() || verificationCode.isBlank() -> {
                            statusMessage = "이메일과 인증 코드를 입력해주세요."
                        }
                        password != confirmPassword -> {
                            statusMessage = "비밀번호가 일치하지 않습니다."
                        }
                        else -> {
                            scope.launch {
                                try {
                                    // Step 1: 이메일 인증번호 확인
                                    val verifyResponse = RetrofitInstance.api.verifyEmail(
                                        email = email,
                                        token = verificationCode
                                    )

                                    withContext(Dispatchers.Main) {
                                        if (verifyResponse.isSuccessful) {
                                            Log.d("SignUpTab", "이메일 인증 성공")

                                            // Step 2: 이메일 인증 성공 후 회원가입 진행
                                            val memberDtoRequest = MemberDtoRequest(
                                                loginId = loginId,
                                                password = password,
                                                userName = userName,
                                                email = email,
                                                gender = gender
                                            )
                                            val signUpResponse = RetrofitInstance.api.signUp(memberDtoRequest)

                                            if (signUpResponse.isSuccessful) {
                                                statusMessage = "회원가입 성공"
                                                Log.d("SignUpTab", statusMessage)
                                                navController.navigate("loginTab")
                                            } else {
                                                statusMessage = "회원가입 실패: ${signUpResponse.errorBody()?.string()}"
                                                Log.d("SignUpTab", statusMessage)
                                            }
                                        } else {
                                            statusMessage = "인증번호가 올바르지 않습니다."
                                            Log.d("SignUpTab", "이메일 인증 실패: ${verifyResponse.errorBody()?.string()}")
                                        }
                                    }
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        statusMessage = "오류 발생: ${e.message}"
                                        Log.e("SignUpTab", statusMessage)
                                    }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = "가입하기", color = Color.White)
            }
        }
    }
}

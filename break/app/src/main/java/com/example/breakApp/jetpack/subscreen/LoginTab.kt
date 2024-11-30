package com.example.breakApp.jetpack.subscreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.breakApp.R
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.api.model.FindIdRequest
import com.example.breakApp.api.model.LoginDto
import com.example.breakApp.jetpack.tools.DialogType
import com.example.breakApp.jetpack.tools.FindDialog
import com.example.breakApp.tools.PreferenceManager
import kotlinx.coroutines.launch

@Composable
fun LoginTab(navController: NavController) {
    var loginId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf(DialogType.NONE) }
    var statusMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(130.dp)
                .offset(y = -200.dp)
                .background(Color.Gray, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_b),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(16.dp, top = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (statusMessage.isNotBlank()) {
                Text(
                    text = statusMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )
            }

            // 아이디 입력 필드
            BasicTextField(
                value = loginId,
                onValueChange = { loginId = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .height(36.dp)
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (loginId.isEmpty()) {
                            Text(
                                text = "ID", // 플레이스홀더 텍스트
                                color = Color.LightGray, // 플레이스홀더 색상
                                fontSize = 16.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )

            // 비밀번호 입력 필드
            BasicTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp) // 동일한 패딩
                    .height(36.dp) // 동일한 높이
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
                    .padding(horizontal = 10.dp, vertical = 6.dp), // 동일한 내부 패딩
                visualTransformation = PasswordVisualTransformation(),
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp), // 동일한 텍스트 크기
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (password.isEmpty()) {
                            Text(
                                text = "PW", // 플레이스홀더 텍스트
                                color = Color.LightGray,
                                fontSize = 16.sp // 동일한 플레이스홀더 크기
                            )
                        }
                        innerTextField()
                    }
                }
            )
            // 로그인 버튼
            Button(
                onClick = {
                    val token = PreferenceManager.getAccessToken()
                    Log.d("Debug", "AccessToken: $token")
                    when {
                        loginId.isBlank() || password.isBlank() -> {
                            statusMessage = "ID와 비밀번호를 입력해주세요."
                        }
                        loginId.length < 3 || password.length < 6 -> {
                            statusMessage = "ID는 3자 이상, 비밀번호는 6자 이상이어야 합니다."
                        }
                        else -> {
                            scope.launch {
                                try {
                                    val response =
                                        RetrofitInstance.api.login(LoginDto(loginId, password))
                                    if (response.isSuccessful) {
                                        val tokenInfo = response.body()?.data
                                        if (tokenInfo != null) {
                                            // AccessToken 저장 (PreferenceManager 사용)
                                            PreferenceManager.saveAccessToken(tokenInfo.accessToken)
                                            statusMessage = "로그인 성공"
                                            navController.navigate("mainScreen") {
                                                popUpTo("loginTab") { inclusive = true }
                                            }
                                        } else {
                                            statusMessage = "로그인 실패: 서버에서 유효한 토큰을 받지 못했습니다."
                                        }
                                    } else {
                                        statusMessage = "로그인 실패: ${response.code()} - ${response.errorBody()?.string()}"
                                    }
                                } catch (e: Exception) {
                                    statusMessage = "로그인 중 오류가 발생했습니다. 네트워크를 확인해주세요."
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA0000))
            ) {
                Text(text = "로그인", color = Color.White)
            }

            // 하단 버튼: ID찾기, PW찾기, 회원가입
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        dialogType = DialogType.ID_FIND
                        showDialog = true
                    },
                    modifier = Modifier
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("ID찾기", color = Color.Black, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        dialogType = DialogType.PW_FIND
                        showDialog = true
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("PW찾기", color = Color.Black, fontSize = 12.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = { navController.navigate("signUpTab") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("회원가입", color = Color.Black, fontSize = 12.sp)
                }
            }
        }

        if (showDialog) {
            when (dialogType) {
                DialogType.ID_FIND -> {
                    FindDialog(
                        dialogType = dialogType,
                        onDismissRequest = { showDialog = false },
                        onSubmit = { emailInput ->
                            scope.launch {
                                try {
                                    val response = RetrofitInstance.api.findIdByEmail(FindIdRequest(emailInput))
                                    if (response.isSuccessful) {
                                        println("아이디 찾기 성공: ${response.body()}")
                                    } else {
                                        println("아이디 찾기 실패: ${response.code()}")
                                    }
                                } catch (e: Exception) {
                                    println("오류 발생: ${e.message}")
                                }
                            }
                        }
                    )
                }
                DialogType.PW_FIND -> {
                    FindDialog(
                        dialogType = dialogType,
                        onDismissRequest = { showDialog = false },
                        onSubmit = { emailInput ->
                            scope.launch {
                                try {
                                    val response = RetrofitInstance.api.findIdByEmail(FindIdRequest(emailInput))
                                    if (response.isSuccessful) {
                                        println("아이디 찾기 성공: ${response.body()}")
                                    } else {
                                        println("아이디 찾기 실패: ${response.code()}")
                                    }
                                } catch (e: Exception) {
                                    println("오류 발생: ${e.message}")
                                }
                            }
                        }
                    )
                }
                else -> { showDialog = false }
            }
        }
    }
}

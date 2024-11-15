package com.example.breakApp.jetpack.subscreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.api.model.LoginDto
import com.example.breakApp.jetpack.tools.DialogType
import com.example.breakApp.jetpack.tools.FindDialog
import kotlinx.coroutines.launch

@Composable
fun LoginTab(navController: NavController) {
    var loginId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf(DialogType.NONE) }
    val scope = rememberCoroutineScope()

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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 아이디 입력 필드
            BasicTextField(
                value = loginId,
                onValueChange = { loginId = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
                    .padding(12.dp)
            )

            // 비밀번호 입력 필드
            BasicTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .background(Color.Gray, shape = MaterialTheme.shapes.small)
                    .padding(12.dp)
            )

            // 로그인 버튼
            Button(
                onClick = {
                    if (loginId.isBlank() || password.isBlank()) {
                        Toast.makeText(
                            navController.context,
                            "ID와 비밀번호를 입력해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (loginId == "admin" && password == "1234") {
                        // 관리자 계정 성공 처리
                        navController.navigate("mainScreen") {
                            popUpTo("loginTab") { inclusive = true }
                        }
                        Toast.makeText(
                            navController.context,
                            "관리자 로그인 성공",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Retrofit을 통한 로그인 처리
                        scope.launch {
                            try {
                                val response = RetrofitInstance.api.login(LoginDto(loginId, password))
                                if (response.isSuccessful) {
                                    val tokenInfo = response.body()?.data
                                    if (tokenInfo != null) {
                                        // 로그인 성공 -> MainScreen으로 이동
                                        navController.navigate("mainScreen") {
                                            popUpTo("loginTab") { inclusive = true }
                                        }
                                    } else {
                                        Toast.makeText(
                                            navController.context,
                                            "로그인 시도에 실패했습니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        navController.context,
                                        "로그인 실패: ${response.code()}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    navController.context,
                                    "오류 발생: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
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
                    modifier = Modifier.weight(1f),
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

        // 다이얼로그 표시
        if (showDialog) {
            FindDialog(
                dialogType = dialogType,
                onDismissRequest = { showDialog = false }
            )
        }
    }
}

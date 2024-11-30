package com.example.breakApp.jetpack.subscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.api.model.MemberDtoResponse
import com.example.breakApp.api.model.UpdateDtoRequest
import com.example.breakApp.tools.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun EditProfile(navController: NavController) {
    var userInfo by remember { mutableStateOf<MemberDtoResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // 수정 가능한 상태 변수
    var userName by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // 읽기 전용 상태 변수
    var email by remember { mutableStateOf("") }
    var createdAt by remember { mutableStateOf("") }
    var updatedAt by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    // 비밀번호 확인 여부
    val isPasswordValid = newPassword.isNotBlank() && newPassword == confirmPassword

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                isLoading = true
                val token = PreferenceManager.getAccessToken()
                if (token != null) {
                    val response = RetrofitInstance.api.getMyInfo()
                    if (response.isSuccessful) {
                        userInfo = response.body()?.data
                        userName = userInfo?.userName ?: ""
                        email = userInfo?.email ?: ""
                        createdAt = userInfo?.createdAt ?: ""
                        updatedAt = userInfo?.updatedAt ?: ""
                        gender = userInfo?.gender ?: ""
                    } else {
                        errorMessage = "정보를 불러오는 데 실패했습니다."
                    }
                } else {
                    errorMessage = "Access token is missing."
                }
            } catch (e: Exception) {
                errorMessage = "An error occurred: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.primary)
        } else if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.align(Alignment.Center),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        } else if (userInfo != null) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .align(Alignment.TopCenter),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "회원 정보 수정",
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp, bottom = 8.dp),
                    textAlign = TextAlign.Center
                )
                // 사용자 정보를 카드로 표시
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.elevatedCardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF242424))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        UserInputBox(label = "이름", value = userName) { userName = it }
                        UserInputBox(label = "이메일", value = email, enabled = false) {}
                        UserInputBox(label = "새 비밀번호", value = newPassword, isPassword = true) { newPassword = it }
                        if (!isPasswordValid && newPassword.isNotBlank() && confirmPassword.isNotBlank()) {
                            Text(
                                text = "새 비밀번호가 일치하지 않습니다.",
                                color = Color.White,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        UserInputBox(label = "새 비밀번호 확인", value = confirmPassword, isPassword = true) { confirmPassword = it }
                        UserInputBox(label = "성별", value = gender, enabled = false) {}
                        UserInputBox(label = "계정 생성일", value = createdAt, enabled = false) {}



                        // 저장 버튼
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    try {
                                        isLoading = true
                                        val token = PreferenceManager.getAccessToken()
                                        if (token != null) {
                                            // 비밀번호 필드를 비워둔 경우 null로 설정
                                            val updateRequest = UpdateDtoRequest(
                                                userName = userName, // 이름은 항상 업데이트
                                                password = if (newPassword.isNotBlank()) newPassword else null // 비밀번호가 비어있으면 null
                                            )
                                            val response = RetrofitInstance.api.updateMyInfo(updateRequest)
                                            if (response.isSuccessful) {
                                                withContext(Dispatchers.Main) {
                                                    navController.popBackStack() // 성공 시 이전 화면으로 이동
                                                }
                                            } else {
                                                errorMessage = "정보 수정에 실패했습니다."
                                            }
                                        } else {
                                            errorMessage = "Access token is missing."
                                        }
                                    } catch (e: Exception) {
                                        errorMessage = "An error occurred: ${e.localizedMessage}"
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                                .padding(top = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA0000))
                        ) {
                            Text(text = "저장", color = Color.White, fontSize = 16.sp)
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun UserInputBox(
    label: String,
    value: String,
    enabled: Boolean = true,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = if (enabled) Color(0xFF444444) else Color(0xFF2C2C2C),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                singleLine = true,
                textStyle = TextStyle(
                    color = if (enabled) Color.White else Color.Gray,
                    fontSize = 16.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None // 비밀번호 처리
            )
        }
    }
}


package com.example.breakApp.jetpack.subscreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.api.model.UpdateDtoRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingTab(navController: NavController) {
    var showNameDialog by remember { mutableStateOf(false) } // 이름 변경 다이얼로그 표시 여부
    var showPasswordDialog by remember { mutableStateOf(false) } // 비밀번호 변경 다이얼로그 표시 여부
    var errorMessage by remember { mutableStateOf<String?>(null) } // 에러 메시지 상태
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("설정") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 알림 설정
            Text(
                text = "알림 설정",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* 알림 설정 클릭 시 동작 추가 */ }
                    .padding(16.dp)
            )

            // 이름 변경
            Text(
                text = "이름 변경",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showNameDialog = true } // 이름 변경 다이얼로그 표시
                    .padding(16.dp)
            )

            // 비밀번호 변경
            Text(
                text = "비밀번호 변경",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showPasswordDialog = true } // 비밀번호 변경 다이얼로그 표시
                    .padding(16.dp)
            )
        }

        // 이름 변경 다이얼로그
        if (showNameDialog) {
            UpdateNameDialog(
                onDismiss = {
                    showNameDialog = false // 명시적으로 닫힐 때만 상태 변경
                },
                onSuccess = { name ->
                    coroutineScope.launch {
                        try {
                            val response = RetrofitInstance.api.updateMyInfo(
                                UpdateDtoRequest(userName = name, password = null)
                            )
                            if (response.isSuccessful) {
                                println("이름 변경 성공")
                                errorMessage = null
                            } else {
                                errorMessage = "Error: ${response.errorBody()?.string()}"
                            }
                        } catch (e: Exception) {
                            errorMessage = "Exception: ${e.localizedMessage}"
                        }
                    }
                }
            )
        }

        // 비밀번호 변경 다이얼로그
        if (showPasswordDialog) {
            UpdatePasswordDialog(
                onDismiss = {
                    showPasswordDialog = false // 명시적으로 닫힐 때만 상태 변경
                },
                onSuccess = { password ->
                    coroutineScope.launch {
                        try {
                            val response = RetrofitInstance.api.updateMyInfo(
                                UpdateDtoRequest(userName = null, password = password)
                            )
                            if (response.isSuccessful) {
                                println("비밀번호 변경 성공")
                                errorMessage = null
                            } else {
                                errorMessage = "Error: ${response.errorBody()?.string()}"
                            }
                        } catch (e: Exception) {
                            errorMessage = "Exception: ${e.localizedMessage}"
                        }
                    }
                }
            )
        }

        // 에러 메시지 표시
        errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Composable
fun UpdateNameDialog(
    onDismiss: () -> Unit,
    onSuccess: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var currentStep by remember { mutableStateOf("INPUT") } // 단계 상태: "INPUT" 또는 "SUCCESS"

    AlertDialog(
        onDismissRequest = { /* 사용자가 명시적으로 닫지 않도록 무시 */ },
        title = {
            Text(
                when (currentStep) {
                    "INPUT" -> "이름 변경"
                    "SUCCESS" -> "변경 완료"
                    else -> ""
                }
            )
        },
        text = {
            when (currentStep) {
                "INPUT" -> {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("새 이름") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                "SUCCESS" -> {
                    Text("이름이 성공적으로 변경되었습니다.", modifier = Modifier.fillMaxWidth())
                }
            }
        },
        confirmButton = {
            when (currentStep) {
                "INPUT" -> {
                    Button(
                        onClick = {
                            onSuccess(name) // 입력값 전달
                            currentStep = "SUCCESS" // 성공 단계로 전환
                        }
                    ) {
                        Text("변경")
                    }
                }
                "SUCCESS" -> {
                    Button(
                        onClick = { onDismiss() } // 확인 버튼 클릭 시 다이얼로그 닫기
                    ) {
                        Text("확인")
                    }
                }
            }
        },
        dismissButton = {
            if (currentStep == "INPUT") { // 입력 단계에서만 취소 버튼 활성화
                Button(onClick = { onDismiss() }) {
                    Text("취소")
                }
            }
        }
    )
}


@Composable
fun UpdatePasswordDialog(
    onDismiss: () -> Unit,
    onSuccess: (String) -> Unit
) {
    var password by remember { mutableStateOf("") }
    var currentStep by remember { mutableStateOf("INPUT") } // 단계 상태: "INPUT" 또는 "SUCCESS"

    AlertDialog(
        onDismissRequest = { /* 사용자가 명시적으로 닫지 않도록 무시 */ },
        title = {
            Text(
                when (currentStep) {
                    "INPUT" -> "비밀번호 변경"
                    "SUCCESS" -> "변경 완료"
                    else -> ""
                }
            )
        },
        text = {
            when (currentStep) {
                "INPUT" -> {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("새 비밀번호") },
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
                "SUCCESS" -> {
                    Text("비밀번호가 성공적으로 변경되었습니다.", modifier = Modifier.fillMaxWidth())
                }
            }
        },
        confirmButton = {
            when (currentStep) {
                "INPUT" -> {
                    Button(
                        onClick = {
                            onSuccess(password) // 입력값 전달
                            currentStep = "SUCCESS" // 성공 단계로 전환
                        }
                    ) {
                        Text("변경")
                    }
                }
                "SUCCESS" -> {
                    Button(
                        onClick = { onDismiss() } // 확인 버튼 클릭 시 다이얼로그 닫기
                    ) {
                        Text("확인")
                    }
                }
            }
        },
        dismissButton = {
            if (currentStep == "INPUT") { // 입력 단계에서만 취소 버튼 활성화
                Button(onClick = { onDismiss() }) {
                    Text("취소")
                }
            }
        }
    )
}

package com.example.breakApp.jetpack.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.core.util.PatternsCompat
import kotlinx.coroutines.launch


@Composable
fun FindDialog(
    dialogType: DialogType,
    onDismissRequest: () -> Unit,
    onSubmit: (String, () -> Unit) -> Unit // 완료 콜백 추가
) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var submissionMessage by remember { mutableStateOf("") } // 전송 결과 메시지 추가
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    AlertDialog(
        onDismissRequest = {
            if (!isSubmitting) {
                onDismissRequest()
                keyboardController?.hide() // 키보드 닫기
            }
        },
        title = {
            Text(
                text = when (dialogType) {
                    DialogType.ID_FIND -> "아이디 찾기"
                    DialogType.PW_FIND -> "비밀번호 찾기"
                    else -> ""
                }
            )
        },
        text = {
            Column {
                Text(text = "이메일을 입력하세요.", fontSize = 14.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                BasicTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                        .padding(12.dp)
                        .focusRequester(focusRequester),
                    textStyle = LocalTextStyle.current.copy(color = Color.Black)
                )
                if (errorMessage.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = errorMessage, fontSize = 12.sp, color = Color.Red)
                }
                if (isSubmitting) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = submissionMessage.ifBlank { "전송 중..." },
                        fontSize = 12.sp,
                        color = if (submissionMessage.isNotBlank()) Color.Green else Color.Blue
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (email.text.isBlank()) {
                        errorMessage = "이메일을 입력하세요."
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches()) {
                        errorMessage = "올바른 이메일 형식을 입력하세요."
                    } else {
                        errorMessage = ""
                        isSubmitting = true
                        submissionMessage = "" // 초기화
                        onSubmit(email.text) {
                            isSubmitting = false
                            submissionMessage = "전송 완료!"
                        }
                    }
                },
                enabled = !isSubmitting // 전송 중일 때 버튼 비활성화
            ) {
                Text("전송")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    if (!isSubmitting) {
                        onDismissRequest()
                        keyboardController?.hide() // 키보드 닫기
                    }
                }
            ) {
                Text("취소")
            }
        },
        containerColor = Color.White
    )

    // 이메일 입력 시 포커스 요청
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

package com.example.breakApp.jetpack.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

@Composable
fun FindDialog(
    dialogType: DialogType,
    onDismissRequest: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var errorMessage by remember { mutableStateOf("") }
    var submissionMessage by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) } // 전송 완료 상태
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    AlertDialog(
        onDismissRequest = {
            if (!isSubmitted) {
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
                },
                color = Color.White // 제목 글자 색상 흰색
            )
        },
        text = {
            Column {
                if (!isSubmitted) {
                    Text(
                        text = "이메일을 입력하세요.",
                        fontSize = 14.sp,
                        color = Color.White // 설명 텍스트 색상 흰색
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    BasicTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black, shape = MaterialTheme.shapes.small) // 입력창 배경색 검은색
                            .border(1.dp, Color.White, shape = MaterialTheme.shapes.small) // 테두리 흰색
                            .padding(12.dp)
                            .focusRequester(focusRequester),
                        textStyle = LocalTextStyle.current.copy(color = Color.White) // 입력 텍스트 색상 흰색
                    )
                    if (errorMessage.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = errorMessage,
                            fontSize = 12.sp,
                            color = Color.Red // 오류 메시지 색상 빨간색
                        )
                    }
                } else {
                    Text(
                        text = submissionMessage,
                        fontSize = 14.sp,
                        color = Color.White // 전송 완료 메시지 색상 흰색
                    )
                }
            }
        },
        confirmButton = {
            if (isSubmitted) {
                // 전송 완료 후 확인 버튼
                Button(
                    onClick = {
                        onDismissRequest() // 창 닫기
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text("확인", color = Color.White) // 버튼 텍스트 색상 흰색
                }
            } else {
                // 전송 버튼
                Button(
                    onClick = {
                        if (email.text.isBlank()) {
                            errorMessage = "이메일을 입력하세요."
                        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.text).matches()) {
                            errorMessage = "올바른 이메일 형식을 입력하세요."
                        } else {
                            errorMessage = ""
                            submissionMessage = "전송 완료!"
                            isSubmitted = true // 전송 완료 상태로 전환
                            onSubmit(email.text)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text("전송", color = Color.White) // 버튼 텍스트 색상 흰색
                }
            }
        },
        dismissButton = {
            if (!isSubmitted) {
                // 취소 버튼은 전송 완료 전까지만 표시
                Button(
                    onClick = {
                        onDismissRequest()
                        keyboardController?.hide() // 키보드 닫기
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
                ) {
                    Text("취소", color = Color.White) // 버튼 텍스트 색상 흰색
                }
            }
        },
        containerColor = Color.DarkGray // 전체 창 배경색 짙은 회색
    )

    // 이메일 입력 시 포커스 요청
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

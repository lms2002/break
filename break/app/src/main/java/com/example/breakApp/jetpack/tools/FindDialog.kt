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

@Composable
fun FindDialog(dialogType: DialogType, onDismissRequest: () -> Unit) {
    var email by remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        onDismissRequest = { onDismissRequest() },
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
                        .padding(12.dp),
                    textStyle = LocalTextStyle.current.copy(color = Color.Black)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // 전송 로직 (후에 구현 예정)
                    onDismissRequest()
                }
            ) {
                Text("전송")
            }
        },
        dismissButton = {
            Button(onClick = { onDismissRequest() }) {
                Text("취소")
            }
        },
        containerColor = Color.White
    )
}


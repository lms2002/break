package com.example.breakApp.jetpack.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.breakApp.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ExerciseInputSection(
    label: String,
    weight: Float,
    repetitions: Int,
    isSaved: Boolean,
    onWeightChange: (Float) -> Unit,
    onRepetitionsChange: (Int) -> Unit,
    onSaveClicked: () -> Unit // 저장 로직
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 세트 번호
            Text(
                text = label,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 18.dp)
            )

            // Weight 입력
            InputField(
                label = "무게 (kg)",
                value = weight.toString(),
                onValueChange = { onWeightChange(it.toFloatOrNull() ?: 20.0f) }
            )

            // Repetitions 입력
            InputField(
                label = "횟수",
                value = repetitions.toString(),
                onValueChange = { onRepetitionsChange(it.toIntOrNull() ?: 10) },
                modifier = Modifier.padding(start = 12  .dp)
            )

            // 저장 버튼
            IconButton(
                onClick = {
                    if (!isSaved) {
                        onSaveClicked() // 저장 로직 실행
                    }
                },
                enabled = !isSaved // 저장된 세트는 버튼 비활성화
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isSaved) R.drawable.ic_check else R.drawable.ic_square
                    ),
                    contentDescription = if (isSaved) "Saved" else "Save Set",
                    tint = if (isSaved) Color.White else Color.DarkGray// 저장 완료 시 색상 변경
                )
            }
        }
    }
}

@Composable
fun InputField(label: String, value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp))
        Box(
            modifier = Modifier
                .width(80.dp)
                .height(40.dp)
                .background(Color.LightGray, shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center // 텍스트를 박스의 오른쪽 아래로 정렬
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 8.dp),
                textStyle = TextStyle(
                    fontSize = 17.sp,
                    color = Color.Black,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center // 텍스트를 중앙 정렬
                ),
                singleLine = true // 텍스트가 한 줄로 유지되도록 설정

            )
        }
    }
}

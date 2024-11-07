package com.example.breakApp.jetpack.subscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HistoryBody() {
    var weight by remember { mutableStateOf("") }
    var bodyFat by remember { mutableStateOf("") }
    var skeletalMuscle by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // 중앙 정렬
    ) {
        Text(
            text = "전체기간",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 사용자 입력 섹션
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround, // 중앙에서 입력 필드와 버튼 정렬
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserInputBox(label = "몸무게", value = weight, unit = "kg", onValueChange = { weight = it })
            UserInputBox(label = "체지방", value = bodyFat, unit = "kg", onValueChange = { bodyFat = it })
            UserInputBox(label = "골격근량", value = skeletalMuscle, unit = "kg", onValueChange = { skeletalMuscle = it })
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 저장 버튼
        Button(
            onClick = { /* 데이터 저장 로직 */ },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "저장", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 그래프 제목
        Text(
            text = "몸무게(kg)",
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // 그래프 표시 영역
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "그래프", fontSize = 24.sp, color = Color.Black)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInputBox(label: String, value: String, unit: String, onValueChange: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .width(100.dp)
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(4.dp)
        ) {
            TextField(
                value = value,
                onValueChange = {
                    if (it.length <= 5) onValueChange(it)  // 최대 5자리로 제한 (예: 000.0)
                },
                singleLine = true,
                textStyle = TextStyle(color = Color.Black), // 텍스트 색상 설정
                modifier = Modifier.width(70.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Text(
                text = unit,
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}


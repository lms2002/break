package com.example.breakApp.jetpack.subscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun HistoryBody(navController: NavController) {
    // 상태 관리 변수 초기화
    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var skeletalMuscle by remember { mutableStateOf("") }
    var bodyFat by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf("") }
    // 날짜 상태 관리
    var selectedDate by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally // 중앙 정렬
    ) {
        // 날짜 선택 필드
        TextField(
            value = selectedDate,
            onValueChange = { selectedDate = it }, // 입력 값 변경 시 날짜로 저장
            label = { Text("날짜 선택") },
            placeholder = { Text("YYYY-MM-DD 형식") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable { /* 텍스트 클릭 시 날짜 입력 로직 추가 */ }
        )

        // 사용자 입력 섹션 (세로로 배치, 가운데 정렬)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()), // 스크롤 가능하게 추가
            verticalArrangement = Arrangement.spacedBy(-8.0.dp), // 각 입력 항목 간 간격 설정
            horizontalAlignment = Alignment.CenterHorizontally // 입력 칸들을 가운데로 정렬
        ) {
            UserInputBox(label = "나이", value = age, unit = "세", onValueChange = { age = it })
            UserInputBox(label = "키", value = height, unit = "cm", onValueChange = { height = it })
            UserInputBox(label = "몸무게", value = weight, unit = "kg", onValueChange = { weight = it })
            UserInputBox(label = "골격근량", value = skeletalMuscle, unit = "kg", onValueChange = { skeletalMuscle = it })
            UserInputBox(label = "체지방", value = bodyFat, unit = "kg", onValueChange = { bodyFat = it })
            UserInputBox(label = "BMI", value = bmi, unit = "", onValueChange = { bmi = it })

            Button(
                onClick = { /* 데이터 저장 로직 */ },
                modifier = Modifier
                    .fillMaxWidth(0.4f) // 버튼을 화면의 너비로 채우기
                    .height(84.dp) // 적당한 높이 설정
                    .padding(vertical = 16.dp) // 버튼과 다른 입력칸 간의 간격 추가
                    .padding(bottom = 2.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red) // 버튼 배경 색상 추가
            ) {
                Text(text = "저장", color = Color.White, fontSize = 18.sp) // 버튼 텍스트 크기
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 저장 버튼


    }
}

@Composable
fun UserInputBox(label: String, value: String, unit: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp), // 입력 박스를 양쪽에 간격을 주어 배치
        horizontalAlignment = Alignment.CenterHorizontally // 중앙 정렬
    ) {
        // 레이블
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier
                .padding(bottom = 4.dp) // 레이블과 입력 칸 사이 간격
        )

        // 입력 칸
        BasicTextField(
            value = value,
            onValueChange = { if (it.length <= 6) onValueChange(it) }, // 6자리 제한
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .height(50.dp) // 높이 증가
                .width(200.dp) // 폭을 줄여서 6자리 정도 입력 가능
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        )

        // 단위
        Text(
            text = unit,
            color = Color.Black,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

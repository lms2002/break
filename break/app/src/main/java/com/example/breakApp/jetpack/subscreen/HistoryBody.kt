package com.example.breakApp.jetpack.subscreen

import android.util.Log
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.breakApp.jetpack.tools.Calendar

@Composable
fun HistoryBody(navController: NavController) {
    // 선택된 날짜와 캘린더 표시 상태 관리
    var selectedDate by remember { mutableStateOf("") }
    var showCalendar by remember { mutableStateOf(false) }

    var age by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var skeletalMuscle by remember { mutableStateOf("") }
    var bodyFat by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // 스크롤 가능하도록 수정
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 날짜 선택 TextField
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable {
                    showCalendar = true // 캘린더 표시
                }
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Text(
                text = if (selectedDate.isNotEmpty()) selectedDate else "날짜 선택 (YYYY-MM-DD)",
                style = MaterialTheme.typography.bodyLarge,
                color = if (selectedDate.isNotEmpty()) Color.Black else Color.Gray
            )
        }

// 캘린더 표시
        if (showCalendar) {
            Dialog(
                onDismissRequest = { showCalendar = false } // 캘린더 외부 클릭 시 닫기
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)) // 반투명 배경
                ) {
                    Surface(
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.Center)
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.background,
                        tonalElevation = 8.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .width(300.dp)
                        ) {
                            Text(
                                text = "날짜 선택",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            // 캘린더 컴포넌트
                            Calendar(onDateSelected = { year, month, day ->
                                selectedDate = "$year-${(month + 1).toString().padStart(2, '0')}-${day.toString().padStart(2, '0')}"
                                showCalendar = false // 선택 후 닫기
                            })
                        }
                    }
                }
            }
        }





        Spacer(modifier = Modifier.height(16.dp))

        // 입력 필드 섹션
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.elevatedCardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                UserInputBox(label = "나이", value = age, unit = "세") { age = it }
                UserInputBox(label = "키", value = height, unit = "cm") { height = it }
                UserInputBox(label = "몸무게", value = weight, unit = "kg") { weight = it }
                UserInputBox(label = "골격근량", value = skeletalMuscle, unit = "kg") { skeletalMuscle = it }
                UserInputBox(label = "체지방", value = bodyFat, unit = "%") { bodyFat = it }
                UserInputBox(label = "BMI", value = bmi, unit = "") { bmi = it }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 저장 버튼
        Button(
            onClick = {
                // 저장 로직
                navController.navigate("saveConfirmation") // 저장 후 확인 화면으로 이동
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text(text = "저장", color = Color.White, fontSize = 18.sp)
        }
    }
}

@Composable
fun UserInputBox(label: String, value: String, unit: String, onValueChange: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        BasicTextField(
            value = value,
            onValueChange = { if (it.length <= 6) onValueChange(it) },
            singleLine = true,
            textStyle = TextStyle(color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                .padding(8.dp)
        )

        if (unit.isNotEmpty()) {
            Text(
                text = unit,
                color = Color.Gray,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }
}

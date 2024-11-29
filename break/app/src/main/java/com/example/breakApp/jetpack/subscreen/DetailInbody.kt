
package com.example.breakApp.jetpack.subscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.api.model.InBodyResponse
import com.example.breakApp.tools.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun DetailInBody(navController: NavController, selectedDate: String) {
    var inBodyData by remember { mutableStateOf<InBodyResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isDeleting by remember { mutableStateOf(false) }

    LaunchedEffect(selectedDate) {
        try {
            isLoading = true
            val token = PreferenceManager.getAccessToken()
            if (token != null) {
                val response = RetrofitInstance.api.getInBodyList()
                if (response.isSuccessful) {
                    val data = response.body()?.data ?: emptyList()
                    inBodyData = data.find { it.measurementDate == selectedDate }
                    if (inBodyData == null) {
                        errorMessage = "선택된 날짜에 해당하는 데이터가 없습니다."
                    }
                } else {
                    errorMessage = "Error: ${response.errorBody()?.string()}"
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // 전체 배경 검정색
            .padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (inBodyData != null) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center) // 정보 섹션을 화면 가운데 정렬
                    .fillMaxWidth(0.8f), // 폭을 약간 줄임
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.elevatedCardElevation(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.DarkGray) // 짙은 회색 배경
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.Start // 텍스트는 왼쪽 정렬
                    ) {
                        Text(text = "나이: ${inBodyData?.age ?: "?"} 세", color = Color.White)
                        Text(text = "키: ${inBodyData?.height ?: "?"} cm", color = Color.White)
                        Text(text = "몸무게: ${inBodyData?.weight ?: "?"} kg", color = Color.White)
                        Text(text = "체지방률: ${inBodyData?.bodyFatPercentage ?: "?"} %", color = Color.White)
                        Text(text = "골격근량: ${inBodyData?.muscleMass ?: "?"} kg", color = Color.White)
                        Text(text = "BMI: ${inBodyData?.bmi ?: "?"}", color = Color.White)
                    }
                }
            }
        }

        // 하단 버튼 배치
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // 뒤로 가기 버튼
            Button(
                onClick = {
                    navController.popBackStack() // 이전 화면으로 돌아가기
                },
                modifier = Modifier
                    .weight(1f) // 버튼 폭을 균등 분배
                    .padding(horizontal = 8.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(text = "뒤로 가기", color = Color.Black, fontSize = 16.sp) // 텍스트는 검은색
            }

            // 삭제 버튼
            Button(
                onClick = {
                    if (inBodyData != null) {
                        isDeleting = true
                        deleteInBody(navController, inBodyData!!.inBodyId) // 삭제 로직 호출
                    }
                },
                modifier = Modifier
                    .weight(1f) // 버튼 폭을 균등 분배
                    .padding(horizontal = 8.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp), // 둥근 모서리
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBA0000)) // 어두운 빨간색
            ) {
                Text(text = "삭제", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

fun deleteInBody(navController: NavController, inBodyId: Long) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.deleteInBody(inBodyId)
            if (response.isSuccessful) {
                withContext(Dispatchers.Main) {
                    navController.popBackStack() // 성공적으로 삭제한 후 이전 화면으로 돌아가기
                }
            } else {
                withContext(Dispatchers.Main) {
                    // 실패 시 에러 메시지 표시
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                    println("Failed to delete InBody: $errorMessage")
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                println("An error occurred while deleting InBody: ${e.localizedMessage}")
            }
        }
    }
}

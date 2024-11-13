package com.example.breakApp.jetpack.subscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryPhoto() {
    var currentDate by remember { mutableStateOf(Date()) } // 현재 날짜 상태
    val dateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
    val photos = getPhotosForDate(currentDate) // 현재 날짜에 해당하는 사진 리스트

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 날짜와 좌우 이동 버튼
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Previous",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        currentDate = getPreviousDay(currentDate)
                    } // 이전 날짜로 이동
            )
            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = dateFormat.format(currentDate),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                color = Color.White
            )

            Spacer(modifier = Modifier.width(16.dp))

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Next Day",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        currentDate = getNextDay(currentDate)
                    } // 다음 날짜로 이동
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 사진 그리드
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(photos) { photo ->
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .background(Color.Gray, shape = RoundedCornerShape(8.dp))
                        .clickable {
                            // 사진 클릭 시 수행할 동작 추가 가능 (예: 사진 상세보기)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = photo, color = Color.Black)
                }
            }
        }
    }
}

// 날짜를 하루 전으로 변경하는 함수
fun getPreviousDay(date: Date): Date {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.DAY_OF_YEAR, -1)
    return calendar.time
}

// 날짜를 하루 후로 변경하는 함수
fun getNextDay(date: Date): Date {
    val calendar = Calendar.getInstance()
    calendar.time = date
    calendar.add(Calendar.DAY_OF_YEAR, 1)
    return calendar.time
}

// 특정 날짜의 사진 목록을 불러오는 함수 (예시용)
fun getPhotosForDate(date: Date): List<String> {
    // 실제 데이터베이스 연동 필요
    return listOf("사진1", "사진2", "사진3", "사진4")
}

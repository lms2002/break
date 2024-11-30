package com.example.breakApp.jetpack.tools

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DateFormatSymbols
import java.util.*

@Composable
fun Calendar(
    onDateSelected: (year: Int, month: Int, day: Int) -> Unit = { _, _, _ -> } // 날짜 선택 시 동작
) {
    var selectedYear by remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableStateOf(Calendar.getInstance().get(Calendar.MONTH)) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 연도 선택
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            IconButton(onClick = { selectedYear -= 1 }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous Year"
                )
            }
            Text(text = "$selectedYear", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { selectedYear += 1 }) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next Year"
                )
            }
        }

        // 월 선택
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            IconButton(onClick = {
                selectedMonth = (selectedMonth - 1 + 12) % 12
                if (selectedMonth == 11) selectedYear -= 1
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Previous Month"
                )
            }
            Text(
                text = DateFormatSymbols().months[selectedMonth],
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = {
                selectedMonth = (selectedMonth + 1) % 12
                if (selectedMonth == 0) selectedYear += 1
            }) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Next Month"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 날짜 그리드
        DateGrid(selectedYear, selectedMonth, onDateSelected)
    }
}

@Composable
fun DateGrid(year: Int, month: Int, onDateSelected: (year: Int, month: Int, day: Int) -> Unit) {
    val daysInMonth = getDaysInMonth(year, month)
    val calendar = Calendar.getInstance()
    calendar.set(year, month, 1)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 // 첫 날의 요일 인덱스 (0부터 시작)
    val weekDays = listOf("일", "월", "화", "수", "목", "금", "토")

    // 오늘 날짜 확인
    val today = Calendar.getInstance()
    val isTodayMonth = today.get(Calendar.YEAR) == year && today.get(Calendar.MONTH) == month
    val todayDay = if (isTodayMonth) today.get(Calendar.DAY_OF_MONTH) else -1

    // 요일 헤더
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        weekDays.forEach { day ->
            Text(text = day, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // 날짜 그리기
    var dayCounter = 1 // 날짜는 1일부터 시작

    // 각 주를 순회
    for (week in 0 until 6) { // 최대 6줄 (주) 필요
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // 각 주의 요일을 순회
            for (dayIndex in 0 until 7) {
                val day = if (week == 0 && dayIndex < firstDayOfWeek) {
                    null
                } else if (dayCounter <= daysInMonth) {
                    dayCounter++ // 날짜를 증가시키고 표시
                } else {
                    null
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(28.dp) // 날짜 크기 줄이기
                        .padding(2.dp) // 간격 좁히기
                        .clickable { day?.let { onDateSelected(year, month, it) } }
                ) {
                    day?.let {
                        Text(
                            text = it.toString(),
                            fontSize = 16.sp,
                            color = if (it == todayDay) Color(0xFF8B0000) else Color.White,
                            fontWeight = if (it == todayDay) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
        if (dayCounter > daysInMonth) break // 모든 날짜가 표시되면 종료
    }
}



// 연도와 월에 따라 해당 월의 일 수 계산
fun getDaysInMonth(year: Int, month: Int): Int {
    return when (month) {
        1 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28 // 윤년 계산
        3, 5, 8, 10 -> 30
        else -> 31
    }
}

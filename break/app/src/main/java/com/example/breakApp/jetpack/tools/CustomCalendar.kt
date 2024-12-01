package com.example.breakApp.jetpack.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.DateFormatSymbols
import java.util.Calendar

@Composable
fun CustomCalendar(
    workoutDates: Set<String>, // 운동 기록이 있는 날짜
    onDateSelected: (year: Int, month: Int, day: Int) -> Unit = { _, _, _ -> }
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
        DateGrid(selectedYear, selectedMonth, workoutDates, onDateSelected)
    }
}

@Composable
fun DateGrid(
    year: Int,
    month: Int,
    workoutDates: Set<String>, // 운동 기록 날짜
    onDateSelected: (year: Int, month: Int, day: Int) -> Unit
) {
    val daysInMonth = getDaysInMonth(year, month)
    val calendar = Calendar.getInstance()
    calendar.set(year, month, 1)
    val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1 // 첫 날의 요일 인덱스
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
            Text(text = day, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // 날짜 그리기
    var dayCounter = 1 // 날짜는 1일부터 시작

    for (week in 0 until 6) { // 최대 6줄 (주) 필요
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            for (dayIndex in 0 until 7) {
                val day = if (week == 0 && dayIndex < firstDayOfWeek) {
                    null
                } else if (dayCounter <= daysInMonth) {
                    dayCounter++ // 날짜 증가
                } else {
                    null
                }

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(2.dp)
                        .background(
                            color = when {
                                day == todayDay -> Color(0xFFBA0000) // 오늘 날짜
                                day != null && workoutDates.contains(
                                    "%04d-%02d-%02d".format(year, month + 1, day)
                                ) -> Color.DarkGray // 운동 기록 날짜
                                else -> Color.Transparent // 기본 배경
                            },
                            shape = CircleShape
                        )
                        .clickable { day?.let { onDateSelected(year, month, it) } }
                ) {
                    day?.let {
                        Text(
                            text = it.toString(),
                            fontSize = 16.sp,
                            color = if (day == todayDay) Color.White else Color.LightGray,
                            fontWeight = if (day == todayDay) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                }
            }
        }
        if (dayCounter > daysInMonth) break // 모든 날짜가 표시되면 종료
    }
}
package com.example.break_app.jetpack.subscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import com.example.break_app.R
import androidx.navigation.NavController
import com.example.break_app.jetpack.tools.BottomNavigationBar

@Composable
fun CustomRoutine(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController, 0) },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            SearchBar(navController) // 검색 바
            CategoryFilters() // 카테고리 필터
            Spacer(modifier = Modifier.height(16.dp)) // 카테고리와 운동 목록 사이의 간격
            ExerciseList() // 운동 목록 표시
            CustomRoutineAddButton(navController) // 커스텀 운동 추가 버튼
        }
    }
}

@Composable
fun SearchBar(navController: NavController) {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        // 이전 화면으로 돌아가는 버튼
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.width(8.dp)) // 아이콘과 검색 바 사이 간격

        // 검색 바
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.DarkGray, shape = CircleShape)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            BasicTextField(
                value = searchQuery.value,
                onValueChange = { searchQuery.value = it },
                singleLine = true,
                textStyle = TextStyle(fontSize = 16.sp, color = Color.White),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun CategoryFilters() {
    val categories = listOf("전체", "가슴", "등", "어깨", "삼두", "이두", "하체")
    var selectedCategory by remember { mutableStateOf("전체") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            categories.take(4).forEach { category ->
                Text(
                    text = category,
                    color = if (selectedCategory == category) Color(0xFFFFA500) else Color.White,
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clickable { selectedCategory = category }
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            categories.drop(4).forEach { category ->
                Text(
                    text = category,
                    color = if (selectedCategory == category) Color(0xFFFFA500) else Color.White,
                    style = TextStyle(fontSize = 16.sp),
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clickable { selectedCategory = category }
                )
            }
        }
    }
}


@Composable
fun CustomRoutineAddButton(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_add),
            contentDescription = "Add Custom Exercise",
            tint = Color(0xFF8B0000),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "커스텀 운동 추가",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun ExerciseList() {
    val exercises = listOf(
        "시티드 로우 - 등",
        "인클라인 벤치 프레스 - 가슴",
        "와이드 그립 랫 풀 다운 - 등, 어깨",
        "바벨 플랫 벤치 프레스 - 가슴, 삼두",
        "덤벨 바이셉 컬 - 이두, 전완",
        "이지바 바이셉 컬 - 이두, 전완",
        // 여기에 더 많은 운동 항목 추가 가능
    )

    var selectedExercise by remember { mutableStateOf<String?>(null) }

    LazyColumn(modifier = Modifier.padding(8.dp)) {
        items(exercises) { exercise ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { selectedExercise = if (selectedExercise == exercise) null else exercise }, // 클릭 시 선택된 운동 변경
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.DarkGray)
                ) {
                    // 이미지 대신 회색 박스
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = exercise,
                    style = TextStyle(fontSize = 16.sp, color = Color.White),
                    modifier = Modifier.weight(1f)
                )

                // '+' 버튼은 선택된 운동일 때만 표시
                if (selectedExercise == exercise) {
                    IconButton(onClick = { /* 루틴에 추가하는 로직 구현 */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_add),
                            contentDescription = "Add to Routine",
                            tint = Color(0xFF8B0000)
                        )
                    }
                }
            }
        }
    }
}


/**
 * 11/4 필터 선택 시 주황색으로 색상 변경 효과 적용, 이전 버튼 구현
 */
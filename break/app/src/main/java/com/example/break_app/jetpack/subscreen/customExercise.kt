package com.example.break_app.jetpack.subscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.TextFieldValue
import com.example.break_app.R
import androidx.navigation.NavController
import com.example.break_app.jetpack.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomExerciseScreen(navController: NavController) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController, 0) }, // 하단 내비게이션 바 추가
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // 하단 내비게이션 바와 겹치지 않도록 패딩 추가
                .padding(16.dp)
        ) {
            SearchBar() //검색 바
            CategoryFilters() // 카테고리 필터
            Spacer(modifier = Modifier.height(16.dp)) // 카테고리와 운동 목록 사이의 간격
            ExerciseList() // 운동 목록 표시
        }
    }

}



@Composable
fun SearchBar() {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.Gray.copy(alpha = 0.1f), shape = CircleShape)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        BasicTextField(
            value = searchQuery.value,
            onValueChange = { searchQuery.value = it },
            singleLine = true,
            textStyle = TextStyle(fontSize = 16.sp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CategoryFilters() {
    // 카테고리 목록
    val categories = listOf(
        "전체", "가슴", "등", "삼두", "이두", "하체",
        "맨몸", "유산소", "스트레칭", "덤벨", "바벨"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // 첫 번째 줄
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            categories.take(6).forEach { category -> // 첫 6개 카테고리
                Text(
                    text = category,
                    style = TextStyle(color = Color.Black),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
        // 두 번째 줄
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            categories.drop(6).forEach { category -> // 나머지 카테고리
                Text(
                    text = category,
                    style = TextStyle(color = Color.Black),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}


@Composable
fun CustomExerciseAddButton(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                // 커스텀 운동 추가 화면으로 이동하는 동작 (현재는 navController를 활용해 추가 가능)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = "Add Custom Exercise"
            )
        }
        Text(text = "커스텀 운동 추가", color = Color.Red)
    }
}

@Composable
fun ExerciseList() {
    // 운동 리스트 예시 (수정하여 실제 데이터를 적용 가능)
    val exercises = listOf(
        "시티드 로우 - 등",
        "인클라인 벤치 프레스 - 가슴",
        "와이드 그립 랫 풀 다운 - 등, 어깨",
        "바벨 플랫 벤치 프레스 - 가슴, 삼두",
        "덤벨 바이셉 컬 - 이두, 전완",
        "이지바 바이셉 컬 - 이두, 전완"
    )

    Column(modifier = Modifier.padding(16.dp)) {
        exercises.forEach { exercise ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color.Gray)
                ) {
                    // 이미지 대신 회색 박스
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = exercise,
                    style = TextStyle(fontSize = 16.sp)
                )
            }
        }
    }
}

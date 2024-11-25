package com.example.breakApp.jetpack.subscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.input.TextFieldValue
import com.example.breakApp.R
import androidx.navigation.NavController
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.api.model.Exercise
import com.example.breakApp.jetpack.tools.BottomNavigationBar

@Composable
fun CustomRoutine(navController: NavController) {
    var selectedCategory by remember { mutableStateOf("가슴") } // 초기 카테고리

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
            SearchBar() // 검색 바
            CategoryFilters(selectedCategory) { category ->
                selectedCategory = category // 선택된 카테고리 변경
            }
            Spacer(modifier = Modifier.height(16.dp)) // 카테고리와 운동 목록 사이의 간격
            ExerciseList(selectedCategory) // 선택된 카테고리를 기반으로 운동 목록 표시
        }
    }
}

@Composable
fun CategoryFilters(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("가슴", "등", "어깨", "유산소", "삼두", "이두", "하체", "전신")

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
                        .clickable { onCategorySelected(category) } // 클릭 시 선택된 카테고리 전달
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
                        .clickable { onCategorySelected(category) } // 클릭 시 선택된 카테고리 전달
                )
            }
        }
    }
}

@Composable
fun ExerciseList(selectedCategory: String) {
    var exercises by remember { mutableStateOf<List<Exercise>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var selectedExercise by remember { mutableStateOf<String?>(null) }

    // 서버에서 운동 목록 가져오기
    LaunchedEffect(selectedCategory) { // 카테고리가 변경될 때마다 호출
        try {
            isLoading = true
            errorMessage = null
            val response = RetrofitInstance.api.getExercisesByCategory(selectedCategory)
            if (response.isSuccessful) {
                exercises = response.body() ?: emptyList()
            } else {
                errorMessage = "Error: ${response.errorBody()?.string()}"
            }
        } catch (e: Exception) {
            errorMessage = "An error occurred: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    // UI
    Column(modifier = Modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            )
        } else if (errorMessage != null) {
            Text(
                text = errorMessage ?: "",
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 16.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.padding(8.dp)) {
                items(exercises) { exercise ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { selectedExercise = if (selectedExercise == exercise.name) null else exercise.name },
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
                            text = "${exercise.name} - ${exercise.category}",
                            style = TextStyle(fontSize = 16.sp, color = Color.White),
                            modifier = Modifier.weight(1f)
                        )

                        if (selectedExercise == exercise.name) {
                            IconButton(onClick = {
                                /**
                                 * + 버튼 누르고 루틴 선택해서 추가하도록 하면 될 듯
                                 */
                            }) {
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
    }
}

@Composable
fun SearchBar() {
    val searchQuery = remember { mutableStateOf(TextFieldValue("")) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
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

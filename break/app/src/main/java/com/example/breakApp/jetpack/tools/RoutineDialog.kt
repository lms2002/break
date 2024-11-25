package com.example.breakApp.jetpack.tools

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.api.model.RoutineDto
import com.example.breakApp.tools.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RoutineDialog(
    onDismiss: () -> Unit,
    onRoutineSelected: (RoutineDto) -> Unit
) {
    var routines by remember { mutableStateOf<List<RoutineDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var userId by remember { mutableStateOf<Long?>(null) } // 사용자 ID 상태

    // 사용자 ID 가져오기
    LaunchedEffect(Unit) {
        val token = PreferenceManager.getAccessToken()
        if (token != null) {
            try {
                val userInfoResponse = RetrofitInstance.api.getMyInfo()
                if (userInfoResponse.isSuccessful) {
                    userId = userInfoResponse.body()?.data?.userId // 사용자 ID 설정
                    println("Routines fetched: $routines")
                } else {
                    errorMessage = "Error fetching user info: ${userInfoResponse.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                errorMessage = "Error fetching user info: ${e.localizedMessage}"
            }
        }
    }

    // 루틴 목록 가져오기
    LaunchedEffect(userId) {
        if (userId != null) {
            val token = PreferenceManager.getAccessToken()
            if (token != null) {
                try {
                    isLoading = true
                    val response = RetrofitInstance.api.getRoutineList()
                    if (response.isSuccessful) {
                        routines = response.body() ?: emptyList()
                    } else {
                        errorMessage = "Error fetching routines: ${response.errorBody()?.string()}"
                    }
                } catch (e: Exception) {
                    errorMessage = "An error occurred: ${e.localizedMessage}"
                } finally {
                    isLoading = false
                }
            }
        }
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "루틴 선택", style = MaterialTheme.typography.titleLarge)

                // 상태에 따라 UI 렌더링
                when {
                    isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    errorMessage != null -> Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    else -> LazyColumn {
                        items(routines) { routine ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                                    .clickable { onRoutineSelected(routine) },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = routine.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 루틴 추가 버튼
                Button(
                    onClick = {
                        val token = PreferenceManager.getAccessToken()
                        if (token != null && userId != null) {
                            val newRoutine = RoutineDto(
                                userId = userId!!, // 실제 사용자 ID 사용
                                name = "새 루틴 ${routines.size + 1}"
                            )
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val response = RetrofitInstance.api.createRoutine(newRoutine)
                                    if (response.isSuccessful) {
                                        routines = routines + response.body()!! // 새 루틴 추가
                                    } else {
                                        errorMessage = "Error creating routine: ${response.errorBody()?.string()}"
                                    }
                                } catch (e: Exception) {
                                    errorMessage = "An error occurred: ${e.localizedMessage}"
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("추가")
                }

                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("닫기")
                }
            }
        }
    }
}

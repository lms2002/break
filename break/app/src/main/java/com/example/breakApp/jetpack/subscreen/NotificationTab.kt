package com.example.breakApp.jetpack.subscreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.breakApp.R
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.api.model.NotificationDto
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationTab(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    var notifications by remember { mutableStateOf<List<NotificationDto>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var expandedNotificationId by remember { mutableStateOf<Long?>(null) } // 현재 확장된 알림 ID

    // 알림 목록 가져오기
    LaunchedEffect(Unit) {
        try {
            val response = RetrofitInstance.api.getNotifications()
            if (response.isSuccessful) {
                notifications = response.body() ?: emptyList()
            } else {
                errorMessage = "알림을 가져오는 중 오류가 발생했습니다."
            }
        } catch (e: Exception) {
            errorMessage = "네트워크 오류: ${e.localizedMessage}"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("알림") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LazyColumn {
                items(notifications) { notification ->
                    NotificationItem(
                        notification = notification,
                        isExpanded = expandedNotificationId == notification.notificationId,
                        onClick = {
                            // 클릭 시 확장 상태 토글
                            expandedNotificationId =
                                if (expandedNotificationId == notification.notificationId) null else notification.notificationId
                        },
                        onDelete = {
                            coroutineScope.launch {
                                try {
                                    // 알림 삭제 API 호출
                                    val response = RetrofitInstance.api.deleteNotification(notification.notificationId)
                                    if (response.isSuccessful) {
                                        // 삭제 성공 시 목록에서 제거
                                        notifications = notifications.filter { it.notificationId != notification.notificationId }
                                    } else {
                                        errorMessage = "알림 삭제 중 오류 발생: ${response.errorBody()?.string()}"
                                    }
                                } catch (e: Exception) {
                                    errorMessage = "네트워크 오류: ${e.localizedMessage}"
                                }
                            }
                        }
                    )
                }
            }

            // 에러 메시지 출력
            errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: NotificationDto,
    isExpanded: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val backgroundColor = if (isExpanded) Color(0xff636363)else if (notification.isRead) Color(0xFF2B2B2B) else Color(0xFF2B2B2B)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(12.dp) // 모서리 둥글게 처리
            )
            .clickable { onClick() }
    ) {
        // 기본 알림 내용
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = notification.message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = notification.createdAt,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // "지우기" 버튼: 확장 상태일 때만 표시
            if (isExpanded) {
                TextButton(
                    onClick = onDelete,
                    modifier = Modifier.align(Alignment.CenterVertically)
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = "지우기",
                        color = Color.LightGray,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

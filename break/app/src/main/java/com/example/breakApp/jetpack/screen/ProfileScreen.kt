package com.example.breakApp.jetpack.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.breakApp.R
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.jetpack.tools.BottomNavigationBar
import com.example.breakApp.tools.PreferenceManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, selectedItemIndex: Int) {
    // 사용자 이름을 저장할 상태
    var userName by remember { mutableStateOf("로딩 중...") }
    val scope = rememberCoroutineScope()

    // 사용자 이름 로드
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val token = PreferenceManager.getAccessToken()
                if (!token.isNullOrEmpty()) {
                    val response = RetrofitInstance.api.getMyInfo("Bearer $token")
                    if (response.isSuccessful && response.body()?.data != null) {
                        userName = response.body()?.data?.userName ?: "알 수 없음"
                    } else {
                        userName = "정보를 불러올 수 없습니다."
                    }
                } else {
                    userName = "로그인이 필요합니다."
                }
            } catch (e: Exception) {
                userName = "오류 발생: ${e.message}"
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("내 정보") },
                actions = {
                    IconButton(onClick = { /* 리스트 클릭 시 동작 */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_list),
                            contentDescription = "List"
                        )
                    }
                    IconButton(onClick = { navController.navigate("notification") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_notifications),
                            contentDescription = "Notifications"
                        )
                    }
                    IconButton(onClick = { navController.navigate("settings") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_setting),
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(selectedItemIndex = 1, navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 사용자 프로필 정보
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "User",
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = userName, style = MaterialTheme.typography.titleLarge)
            }

            // 메뉴 항목
            val menuItems = listOf("모든 메모", "친구", "친구 초대")

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp) // 항목 간 간격 줄임
            ) {
                menuItems.forEach { item ->
                    Text(
                        text = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                /**
                                 * 기능 넣기
                                 */
                            }
                            .padding(vertical = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            // 피드백 섹션과의 간격을 주기 위한 Spacer
            Spacer(modifier = Modifier.height(24.dp))

            // 피드백 섹션 - 강조된 텍스트로 구분
            Text(
                text = "피드백",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            val feedbackItems = listOf("운동 추가 요청하기", "간단하게 리뷰 남기기", "건의사항 쪽지 보내기")

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp) // 피드백 항목 간 간격 유지
            ) {
                feedbackItems.forEach { feedback ->
                    Text(
                        text = feedback,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { /* 클릭 시 동작 추가 */ }
                            .padding(vertical = 8.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // 화면 아래로 밀어내기
        }
    }
}

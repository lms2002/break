package com.example.break_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.break_app.ui.theme.Break_appTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Break_appTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopAppBar() }, // 상단 바
        bottomBar = { BottomNavigationBar() } // 하단 네비게이션 바
    ) { innerPadding ->
        // 화면 중앙에 기본 UI 및 데이터베이스 연결 상태 출력
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            TomorrowTasks() // 내일 할 일 섹션
            RoutineSection() // 루틴 섹션
            Spacer(modifier = Modifier.height(16.dp))
            DatabaseConnectionTest() // 데이터베이스 연결 테스트 섹션
        }
    }
}

// 데이터베이스 연결 테스트를 수행하는 함수
@Composable
fun DatabaseConnectionTest(modifier: Modifier = Modifier) {
    // 연결 상태를 저장하는 변수
    var connectionStatus by remember { mutableStateOf("Connecting to database...") }

    // LaunchedEffect로 비동기 데이터베이스 연결 실행
    LaunchedEffect(Unit) {
        val dbManager = DatabaseManager()
        try {
            dbManager.fetchUsers() // 데이터베이스 연동
            connectionStatus = "Database connection successful!"
        } catch (e: Exception) {
            connectionStatus = "Failed to connect to database: ${e.message}"
        }
    }

    // 연결 상태를 화면에 출력
    Text(
        text = connectionStatus,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Break_appTheme {
        MainScreen()
    }
}

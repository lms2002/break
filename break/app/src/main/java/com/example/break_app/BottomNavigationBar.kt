package com.example.break_app

import androidx.compose.foundation.Image
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource

@Composable
fun BottomNavigationBar() {
    // 하단 내비게이션의 각 항목 정보 설정
    val items = listOf("홈", "내 정보", "기록")
    var selectedItem by remember { mutableStateOf(0) }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    // 각 아이템에 맞는 아이콘 설정 (임의의 아이콘으로 설정)
                    val iconId = when (index) {
                        0 -> R.drawable.ic_home // 이미지 파일 리소스 아이디 사용
                        1 -> R.drawable.ic_profile
                        2 -> R.drawable.ic_history
                        else -> R.drawable.ic_home
                    }
                    Image(
                        painter = painterResource(id = iconId),
                        contentDescription = item
                    )
                },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = { selectedItem = index }
            )
        }
    }
}
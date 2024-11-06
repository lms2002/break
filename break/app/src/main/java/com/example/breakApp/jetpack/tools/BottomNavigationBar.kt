package com.example.breakApp.jetpack.tools

import androidx.compose.foundation.Image
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.breakApp.R

@Composable
fun BottomNavigationBar(navController: NavController, selectedItemIndex: Int) {
    // 하단 내비게이션의 각 항목 정보 설정
    val items = listOf("홈", "내 정보", "기록")

    NavigationBar(containerColor = Color.Black) {
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
                label = { Text(item, color = Color.White) },
                selected = selectedItemIndex == index,
                onClick = {
                    if (selectedItemIndex != index) {
                        when (index) {
                            0 -> navController.navigate("mainScreen")
                            1 -> navController.navigate("profileScreen")
                            2 -> navController.navigate("historyScreen")}
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White, // 선택된 아이콘 색상
                    unselectedIconColor = Color.White, // 선택되지 않은 아이콘 색상
                    selectedTextColor = Color.White, // 선택된 텍스트 색상
                    unselectedTextColor = Color.White, // 선택되지 않은 텍스트 색상
                    indicatorColor = Color(0xFF8B0000)
                )
            )
        }
    }
}

/*
241101_선택되어있는 탭 배경색 추가, 후에 아이콘 추가해서 선택되어있는 아이콘은 색상 변경된 동일 아이콘으로 변경 예정

 */
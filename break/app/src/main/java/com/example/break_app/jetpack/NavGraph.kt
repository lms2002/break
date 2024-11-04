package com.example.break_app.jetpack

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.break_app.jetpack.screen.*
import com.example.break_app.jetpack.subscreen.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "mainScreen") {
        composable("mainScreen") { MainScreen(navController) } // 메인 스크린
        composable("profileScreen") { ProfileScreen(navController, 1) }
        composable("historyScreen") { HistoryScreen(navController, 2) }
        composable("customExercise") { CustomExerciseScreen(navController) } // 커스텀 운동 화면
        composable("loginSignup") { LoginScreen(navController) }
        composable("planSection") { PlanSection(navController) } //일정 추가 화면
        composable("notification") { NotificationTab(navController) }
        composable("settings") { SettingTab(navController) }
        composable("loginTab") { LoginTab(navController) }
        composable("signupTab") { SignUpTab(navController) }
    }
}

/*
StartDestination 수정하면 앱 켰을 때 시작화면 변경 가능함
 */
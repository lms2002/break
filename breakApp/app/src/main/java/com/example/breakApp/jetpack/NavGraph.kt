package com.example.breakApp.jetpack

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.breakApp.jetpack.screen.*
import com.example.breakApp.jetpack.subscreen.*
import com.example.breakApp.jetpack.tools.DailyMemoScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "mainScreen",
        enterTransition = { fadeIn(initialAlpha = 1f) },
        exitTransition = { fadeOut(targetAlpha = 0f) },
        popEnterTransition = { fadeIn(initialAlpha = 1f) },
        popExitTransition = { fadeOut(targetAlpha = 0f) }
    ) {
        composable("mainScreen") { MainScreen(navController) } // 메인 스크린
        composable("profileScreen") { ProfileScreen(navController, 1) }
        composable("historyScreen") { HistoryScreen(navController, 2) }
        composable("customRoutine") { CustomRoutine(navController) } // 커스텀 운동 화면
        composable("loginScreen") { LoginScreen(navController) }
        composable("planSection") { PlanSection(navController) } //일정 추가 화면
        composable("notification") { NotificationTab(navController) }
        composable("settings") { SettingTab(navController) }
        composable("loginTab") { LoginTab(navController) }
        composable("signupTab") { SignUpTab(navController) }
        composable("dailyMemo/{selectedDate}") { backStackEntry ->
            val selectedDate = backStackEntry.arguments?.getString("selectedDate")
            DailyMemoScreen(navController = navController, selectedDate = selectedDate ?: "")
        }
        composable("routineManagement/{routineName}") { backStackEntry ->
            val routineName = backStackEntry.arguments?.getString("routineName") ?: "루틴 없음"
            RoutineManagement(navController = navController, routineName = routineName)
        }
    }
}

/*
StartDestination 수정하면 앱 켰을 때 시작화면 변경 가능함
 */
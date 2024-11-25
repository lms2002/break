package com.example.breakApp.jetpack

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.breakApp.jetpack.screen.*
import com.example.breakApp.jetpack.subscreen.*
import com.example.breakApp.jetpack.tools.DailyMemoScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "loginScreen",
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
        composable("detailInBody/{selectedDate}") { backStackEntry ->
            val selectedDate = backStackEntry.arguments?.getString("selectedDate") ?: ""
            DetailInBody(navController = navController, selectedDate = selectedDate)
        }
        composable("dailyMemo/{selectedDate}") { backStackEntry ->
            val selectedDate = backStackEntry.arguments?.getString("selectedDate")
            DailyMemoScreen(navController = navController, selectedDate = selectedDate ?: "")
        }
        composable(
            route = "routineManagement/{routineId}/{routineName}",
            arguments = listOf(
                navArgument("routineId") { type = NavType.LongType },
                navArgument("routineName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val routineId = backStackEntry.arguments?.getLong("routineId") ?: 0L
            val routineName = backStackEntry.arguments?.getString("routineName") ?: "Unknown"
            RoutineManagement(navController = navController, routineId = routineId, routineName = routineName)
        }
    }
}

/*
StartDestination 수정하면 앱 켰을 때 시작화면 변경 가능함
 */
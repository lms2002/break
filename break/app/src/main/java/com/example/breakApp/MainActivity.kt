package com.example.breakApp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.breakApp.api.RetrofitInstance
import com.example.breakApp.ui.theme.Break_appTheme
import com.example.breakApp.jetpack.screen.*
import com.example.breakApp.jetpack.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Break_appTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }

    public fun testBackendConnection() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.testConnection()
                if (response.isSuccessful && response.body() != null) {
                    val message = response.body()!!
                    Log.d("RetrofitTest", "서버 응답: $message")  // "OK"가 출력되면 연결 성공
                } else {
                    Log.e("RetrofitTest", "응답 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("RetrofitTest", "네트워크 오류: ${e.message}")
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    Break_appTheme {
        val navController = rememberNavController()
        MainScreen(navController)
    }
}

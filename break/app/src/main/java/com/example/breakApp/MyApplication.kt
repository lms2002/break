package com.example.breakApp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class MyApplication : Application() {

    companion object {
        private lateinit var sharedPreferences: SharedPreferences

        // SharedPreferences 초기화
        fun getSharedPreferences(): SharedPreferences {
            return sharedPreferences
        }

        // AccessToken 저장
        fun setAccessToken(token: String) {
            sharedPreferences.edit().putString("accessToken", token).apply()
        }

        // AccessToken 가져오기
        fun getAccessToken(): String? {
            return sharedPreferences.getString("accessToken", null)
        }

        // AccessToken 삭제
        fun clearAccessToken() {
            sharedPreferences.edit().remove("accessToken").apply()
        }
    }

    override fun onCreate() {
        super.onCreate()
        // Application context에서 SharedPreferences 초기화
        sharedPreferences = applicationContext.getSharedPreferences("auth", Context.MODE_PRIVATE)
    }
}

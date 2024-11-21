package com.example.breakApp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class MyApplication : Application() {

    companion object {
        private var sharedPreferences: SharedPreferences? = null

        // SharedPreferences를 안전하게 가져오는 메서드
        fun getSharedPreferences(): SharedPreferences {
            return sharedPreferences
                ?: throw IllegalStateException("SharedPreferences is not initialized")
        }

        // AccessToken 저장
        fun setAccessToken(token: String) {
            getSharedPreferences().edit().putString("accessToken", token).apply()
        }

        // AccessToken 가져오기
        fun getAccessToken(): String? {
            return getSharedPreferences().getString("accessToken", null)
        }

        // AccessToken 삭제
        fun clearAccessToken() {
            getSharedPreferences().edit().remove("accessToken").apply()
        }
    }

    override fun onCreate() {
        super.onCreate()
        // SharedPreferences 초기화
        sharedPreferences = applicationContext.getSharedPreferences("auth", Context.MODE_PRIVATE)
    }
}

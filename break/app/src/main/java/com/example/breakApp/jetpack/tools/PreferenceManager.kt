package com.example.breakApp.tools

import com.example.breakApp.MyApplication

object PreferenceManager {
    fun saveTokens(accessToken: String, refreshToken: String) {
        val editor = MyApplication.getSharedPreferences().edit()
        editor.putString("accessToken", accessToken)
        editor.putString("refreshToken", refreshToken)
        editor.apply()
    }

    fun getAccessToken(): String? {
        return MyApplication.getSharedPreferences().getString("accessToken", null)
    }

    fun getRefreshToken(): String? {
        return MyApplication.getSharedPreferences().getString("refreshToken", null)
    }

    fun clearTokens() {
        val editor = MyApplication.getSharedPreferences().edit()
        editor.remove("accessToken")
        editor.remove("refreshToken")
        editor.apply()
    }
}

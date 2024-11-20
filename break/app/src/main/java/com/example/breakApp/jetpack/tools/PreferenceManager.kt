package com.example.breakApp.tools

import com.example.breakApp.MyApplication

object PreferenceManager {
    fun saveAccessToken(accessToken: String) {
        val editor = MyApplication.getSharedPreferences().edit()
        editor.putString("accessToken", accessToken)
        editor.apply()
    }

    fun getAccessToken(): String? {
        return MyApplication.getSharedPreferences().getString("accessToken", null)
    }

    fun clearAccessToken() {
        val editor = MyApplication.getSharedPreferences().edit()
        editor.remove("accessToken")
        editor.apply()
    }
}

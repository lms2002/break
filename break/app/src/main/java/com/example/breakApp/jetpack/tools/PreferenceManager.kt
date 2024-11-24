package com.example.breakApp.tools

import android.util.Log
import com.example.breakApp.MyApplication

object PreferenceManager {
    fun saveAccessToken(accessToken: String) {
        Log.d("PreferenceManager", "Saving AccessToken: $accessToken")
        val editor = MyApplication.getSharedPreferences().edit()
        editor.putString("accessToken", accessToken).apply()
    }

    fun getAccessToken(): String? {
        val token = MyApplication.getSharedPreferences().getString("accessToken", null)
        Log.d("PreferenceManager", "Retrieved AccessToken: $token")
        return token
    }

    fun clearAccessToken() {
        val editor = MyApplication.getSharedPreferences().edit()
        editor.remove("accessToken")
        editor.apply()
    }
}

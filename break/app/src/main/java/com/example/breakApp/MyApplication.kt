package com.example.breakApp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class MyApplication : Application() {
    companion object {
        private lateinit var sharedPreferences: SharedPreferences

        fun getSharedPreferences(): SharedPreferences {
            return sharedPreferences
        }
    }

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = applicationContext.getSharedPreferences("auth", Context.MODE_PRIVATE)
    }
}

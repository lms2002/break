package com.example.breakApp.api

import android.util.Log
import com.example.breakApp.tools.PreferenceManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8089/api/"

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                val url = chain.request().url.toString()

                // 특정 요청에 Authorization 헤더 추가
                if (!url.contains("find-id") && !url.contains("reset-password")) {
                    val token = PreferenceManager.getAccessToken()
                    Log.d("RetrofitInterceptor", "AccessToken: $token")
                    if (!token.isNullOrEmpty()) {
                        requestBuilder.addHeader("Authorization", "Bearer $token")
                    }
                }

                val request = requestBuilder.build()
                Log.d("RetrofitInterceptor", "Request URL: ${request.url}")
                Log.d("RetrofitInterceptor", "Request Headers: ${request.headers}")

                val response = chain.proceed(request)
                Log.d("RetrofitInterceptor", "Response Code: ${response.code}")
                response
            }
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

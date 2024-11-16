package com.example.breakApp.api

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    //private const val BASE_URL = "http://172.16.105.62:8089/api/"  // 백엔드 API URL
    private const val BASE_URL = "http://10.0.2.2:8089/api/"
    // private const val BASE_URL = "http://172.16.106.117:8089/api/" // 서버 = 프론트

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

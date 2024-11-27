package com.example.breakApp.api

import android.util.Log
import com.example.breakApp.tools.PreferenceManager
import com.google.gson.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Date Adapter for JSON serialization and deserialization
// 기존 DateAdapter 또는 LocalDateTimeAdapter 대신 간단히 문자열을 처리
class SimpleStringAdapter : JsonDeserializer<String>, JsonSerializer<String> {
    override fun serialize(
        src: String,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src)
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): String {
        return json.asString
    }
}

// RetrofitInstance 수정
object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8089/api/"

    // Gson 설정에서 문자열로 변환 처리
    private val gson: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(String::class.java, SimpleStringAdapter()) // 문자열 어댑터 등록
            .create()
    }

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = PreferenceManager.getAccessToken()
                Log.d("RetrofitInstance", "Retrieved Token: $token")
                val requestBuilder = chain.request().newBuilder()
                if (!token.isNullOrEmpty()) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }
                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

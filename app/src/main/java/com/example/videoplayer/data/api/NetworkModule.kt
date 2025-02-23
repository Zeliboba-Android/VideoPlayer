package com.example.videoplayer.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {
    private const val BASE_URL = "https://api.vimeo.com/"
    private const val ACCESS_TOKEN = "cf8f7ec1441504fd8050d2a23a730150"
    val apiService: VideoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $ACCESS_TOKEN")
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            )
            .build()
            .create(VideoApiService::class.java)
    }
}
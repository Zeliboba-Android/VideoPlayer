package com.example.videoplayer.data.api


import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoApiService {
    @GET("categories/travel/videos")
    suspend fun getTravelVideos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 2
    ): Response<VimeoApiResponse>

}
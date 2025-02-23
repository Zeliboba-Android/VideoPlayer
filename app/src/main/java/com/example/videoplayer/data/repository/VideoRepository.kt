package com.example.videoplayer.data.repository

import android.content.Context
import android.util.Log
import com.example.videoplayer.data.api.VideoApiService
import com.example.videoplayer.data.db.VideoDao
import com.example.videoplayer.domain.model.Resource
import com.example.videoplayer.domain.model.Video
import com.example.videoplayer.presentation.utils.NetworkUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VideoRepository(
    private val apiService: VideoApiService,
    private val videoDao: VideoDao,
    private val context: Context
) {
    suspend fun getTravelVideos(): Flow<Resource<List<Video>>> = flow {
        try {
            emit(Resource.Loading())
            Log.d("API_REQUEST", "Fetching travel videos")

            if (!NetworkUtils.isNetworkAvailable(context)) {
                Log.e("NETWORK", "No internet connection")
                emit(Resource.Error("No internet connection"))
                return@flow
            }

            val response = apiService.getTravelVideos(
                page = 1,
                perPage = 10
            )

            Log.d("API_RESPONSE", "Response code: ${response.code()}")
            Log.d("API_RESPONSE", "Response body: ${response.body()}")

            if (response.isSuccessful) {
                val vimeoVideos = response.body()?.videos ?: emptyList()
                Log.d("API_DATA", "Videos received: ${vimeoVideos.size}")

                val videos = vimeoVideos.map { vimeoVideo ->
                    Video(
                        id = vimeoVideo.uri.substringAfterLast("/"),
                        title = vimeoVideo.name,
                        duration = "${vimeoVideo.duration} sec",
                        thumbnailUrl = vimeoVideo.getThumbnailUrl(),
                        videoUrl = vimeoVideo.link
                    )
                }

                videoDao.insertVideos(videos)
                emit(Resource.Success(videos))
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("API_ERROR", "Error ${response.code()}: $errorBody")
                emit(Resource.Error("API Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("REPOSITORY", "Error: ${e.localizedMessage}")
            emit(Resource.Error(e.localizedMessage ?: "Unknown error"))
        }
    }
}
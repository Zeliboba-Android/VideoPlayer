package com.example.videoplayer.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.videoplayer.data.repository.VideoRepository
import com.example.videoplayer.domain.model.Resource
import com.example.videoplayer.domain.model.Video
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class VideoListViewModel(
    private val repository: VideoRepository
) : ViewModel() {
    private val _videoState = MutableStateFlow<Resource<List<Video>>>(Resource.Loading())
    val videoState: StateFlow<Resource<List<Video>>> = _videoState

    init {
        loadTravelVideos()
    }

    fun loadTravelVideos() {
        viewModelScope.launch {
            Log.d("VIEWMODEL", "Loading travel videos")
            repository.getTravelVideos().collect { result ->
                _videoState.value = result
            }
        }
    }
}
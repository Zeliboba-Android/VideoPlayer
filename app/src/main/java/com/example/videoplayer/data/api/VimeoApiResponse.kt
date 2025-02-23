package com.example.videoplayer.data.api

import com.google.gson.annotations.SerializedName

data class VimeoApiResponse(
    @SerializedName("data") val videos: List<VimeoVideo>
)
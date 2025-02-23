package com.example.videoplayer.data.api

import com.example.videoplayer.domain.model.Pictures
import com.google.gson.annotations.SerializedName

data class VimeoVideo(
    @SerializedName("uri") val uri: String,
    @SerializedName("name") val name: String,
    @SerializedName("duration") val duration: Int,
    @SerializedName("pictures") val pictures: Pictures,
    @SerializedName("link") val link: String
) {
    fun getThumbnailUrl(): String {
        return pictures.sizes.lastOrNull()?.link ?: ""
    }
}
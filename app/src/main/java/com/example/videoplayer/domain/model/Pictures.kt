package com.example.videoplayer.domain.model

import com.google.gson.annotations.SerializedName

data class Pictures(
    @SerializedName("sizes") val sizes: List<Size>
)
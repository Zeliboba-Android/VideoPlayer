package com.example.videoplayer.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class Video(
    @PrimaryKey
    val id: String,
    val title: String,
    val duration: String,
    val thumbnailUrl: String,
    val videoUrl: String
)
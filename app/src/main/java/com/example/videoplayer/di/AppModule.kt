package com.example.videoplayer.di

import android.content.Context
import androidx.room.Room
import com.example.videoplayer.data.db.AppDatabase
import com.example.videoplayer.data.db.VideoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "video_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideVideoDao(database: AppDatabase): VideoDao = database.videoDao()
}
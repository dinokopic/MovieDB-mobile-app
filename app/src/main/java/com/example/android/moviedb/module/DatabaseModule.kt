package com.example.android.moviedb.module

import android.content.Context
import androidx.room.Room
import com.example.android.moviedb.R
import com.example.android.moviedb.database.MediaDao
import com.example.android.moviedb.database.MediaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideMediaDao(database: MediaDatabase): MediaDao {
        return database.mediaDao
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): MediaDatabase {
        return Room.databaseBuilder(
            appContext.applicationContext,
            MediaDatabase::class.java, appContext.getString(R.string.media)
        ).build()
    }

}
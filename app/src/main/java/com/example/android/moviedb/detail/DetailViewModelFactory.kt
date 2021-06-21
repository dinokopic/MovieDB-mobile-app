package com.example.android.moviedb.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.moviedb.Media

class DetailViewModelFactory(
    private val media: Media,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            return DetailViewModel(media, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
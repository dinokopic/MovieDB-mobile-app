package com.example.android.moviedb

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.moviedb.network.MediaType

class OverviewViewModelFactory(
    private val mediaType: MediaType,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OverviewViewModel::class.java)) {
            return OverviewViewModel(mediaType, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
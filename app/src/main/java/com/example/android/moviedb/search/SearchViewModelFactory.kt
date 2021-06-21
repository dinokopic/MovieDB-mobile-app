package com.example.android.moviedb.search

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.moviedb.network.MediaType

class SearchViewModelFactory(
    private val mediaType: MediaType,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(mediaType, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
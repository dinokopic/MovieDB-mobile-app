package com.example.android.moviedb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviedb.network.TMDBApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OverviewViewModel : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _movies = MutableLiveData<List<Movie>>()
    val movies: LiveData<List<Movie>>
        get() = _movies

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    init {
        getMovies()
    }

    private fun getMovies() {
        coroutineScope.launch {
            val getMoviesDeferred = TMDBApi.retrofitService.getMovies()
            try {
                val movieResults = getMoviesDeferred.await()
                _status.value = "Success"
                if (movieResults.results.isNotEmpty()) {
                    _movies.value = movieResults.results.subList(0, 10)
                }
            } catch (t: Throwable) {
                _status.value = "Failure: " + t.message
            }
        }
    }

}
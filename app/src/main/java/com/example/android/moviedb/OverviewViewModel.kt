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

    private val _media = MutableLiveData<List<Media>>()
    val media: LiveData<List<Media>>
        get() = _media

    private val _status = MutableLiveData<TMDBApiStatus>()
    val status: LiveData<TMDBApiStatus>
        get() = _status

    private val _navigateToSelectedMedia = MutableLiveData<Media>()
    val navigateToSelectedMedia: LiveData<Media>
        get() = _navigateToSelectedMedia

    init {
        getMovies()
    }

    private fun getMovies() {
        coroutineScope.launch {
            val getMoviesDeferred = TMDBApi.retrofitService.getMovies()
            try {
                val movieResults = getMoviesDeferred.await()
                _status.value = TMDBApiStatus.LOADING
                if (movieResults.results.isNotEmpty()) {
                    movieResults.results.mapIndexed { index, movie ->
                        movie.rank = index + 1
                    }
                    _media.value = movieResults.results.subList(0, 10)
                }
                _status.value = TMDBApiStatus.DONE
            } catch (t: Throwable) {
                _status.value = TMDBApiStatus.ERROR
            }
        }
    }

    fun displayMediaDetails(media: Media) {
        _navigateToSelectedMedia.value = media
    }

    fun displayMediaDetailsComplete() {
        _navigateToSelectedMedia.value = null
    }

}

enum class TMDBApiStatus { LOADING, ERROR, DONE }

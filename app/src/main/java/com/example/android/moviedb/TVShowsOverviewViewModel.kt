package com.example.android.moviedb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.moviedb.network.TMDBApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TVShowsOverviewViewModel : ViewModel() {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _tvShows = MutableLiveData<List<Media>>()
    val tvShows: LiveData<List<Media>>
        get() = _tvShows

    private val _navigateToSelectedTVShow = MutableLiveData<Media>()
    val navigateToSelectedTVShow: LiveData<Media>
        get() = _navigateToSelectedTVShow

    init {
        getTVShows()
    }

    private fun getTVShows() {
        coroutineScope.launch {
            val getTVShowsDeferred = TMDBApi.retrofitService.getTVShows()
            try {
                val tvShowResults = getTVShowsDeferred.await()
                if (tvShowResults.results.isNotEmpty()) {
                    tvShowResults.results.mapIndexed { index, movie ->
                        movie.rank = index + 1
                    }
                    _tvShows.value = tvShowResults.results.subList(0, 10)
                }
            } catch (t: Throwable) {

            }
        }
    }

    fun displayMediaDetails(media: Media) {
        _navigateToSelectedTVShow.value = media
    }

    fun displayMediaDetailsComplete() {
        _navigateToSelectedTVShow.value = null
    }

}
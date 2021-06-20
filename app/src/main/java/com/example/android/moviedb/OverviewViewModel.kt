package com.example.android.moviedb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.moviedb.network.MediaResults
import com.example.android.moviedb.network.MediaType
import com.example.android.moviedb.network.TMDBApi
import kotlinx.coroutines.*

class OverviewViewModel(mediaType: MediaType, app: Application) : AndroidViewModel(app) {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _media = MutableLiveData<List<Media>>()
    val media: LiveData<List<Media>>
        get() = _media

    private val _navigateToSelectedMedia = MutableLiveData<Media>()
    val navigateToSelectedMedia: LiveData<Media>
        get() = _navigateToSelectedMedia

    init {
        getMedia(mediaType)
    }

    private fun getMedia(mediaType: MediaType) {
        coroutineScope.launch {
            val getMediasDeferred = TMDBApi.retrofitService.getTopMedia(mediaType.type)
            fetchData(getMediasDeferred, _media)
        }
    }

    fun displayMediaDetails(media: Media) {
        _navigateToSelectedMedia.value = media
    }

    fun displayMediaDetailsComplete() {
        _navigateToSelectedMedia.value = null
    }

}

package com.example.android.moviedb.medialist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.moviedb.Media
import com.example.android.moviedb.MediaRepository
import com.example.android.moviedb.TMDBApiStatus
import com.example.android.moviedb.database.MediaDatabase
import com.example.android.moviedb.database.getDatabase
import com.example.android.moviedb.fetchData
import com.example.android.moviedb.network.MediaType
import com.example.android.moviedb.network.TMDBApi
import kotlinx.coroutines.*

class MediaListViewModel(private val mediaType: MediaType,
                         app: Application) : AndroidViewModel(app) {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<TMDBApiStatus>()
    val status: LiveData<TMDBApiStatus>
        get() = _status

    private val _navigateToSelectedMedia = MutableLiveData<Media>()
    val navigateToSelectedMedia: LiveData<Media>
        get() = _navigateToSelectedMedia

    private val database = getDatabase(app)

    private val mediaRepository = MediaRepository(database)

    init {
        coroutineScope.launch {
            try {
                _status.value = TMDBApiStatus.LOADING
                if (mediaType == MediaType.Movie) {
                    mediaRepository.refreshMovies()
                } else {
                    mediaRepository.refreshTVShows()
                }
                _status.value = TMDBApiStatus.DONE
            } catch (t: Throwable) {
                _status.value = TMDBApiStatus.ERROR
            }
        }
    }

    val media = when (mediaType) {
        MediaType.Movie -> mediaRepository.movies
        else -> mediaRepository.tvShows
    }

    fun displayMediaDetails(media: Media) {
        _navigateToSelectedMedia.value = media
    }

    fun displayMediaDetailsComplete() {
        _navigateToSelectedMedia.value = null
    }

}

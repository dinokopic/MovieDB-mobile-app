package com.example.android.moviedb.medialist

import androidx.lifecycle.*
import com.example.android.moviedb.Media
import com.example.android.moviedb.repository.MediaRepository
import com.example.android.moviedb.TMDBApiStatus
import com.example.android.moviedb.network.MediaType
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.*

class MediaListViewModel @AssistedInject constructor(private val repository: MediaRepository,
                                                     @Assisted private val mediaType: MediaType) : ViewModel() {

    class Factory(
        private val assistedFactory: MediaListViewModelAssistedFactory,
        private val mediaType: MediaType,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return assistedFactory.create(mediaType) as T
        }
    }

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _status = MutableLiveData<TMDBApiStatus>()
    val status: LiveData<TMDBApiStatus>
        get() = _status

    private val _navigateToSelectedMedia = MutableLiveData<Media>()
    val navigateToSelectedMedia: LiveData<Media>
        get() = _navigateToSelectedMedia

    init {
        coroutineScope.launch {
            try {
                _status.value = TMDBApiStatus.LOADING
                if (mediaType == MediaType.Movie) {
                    repository.refreshMovies()
                } else {
                    repository.refreshTVShows()
                }
                _status.value = TMDBApiStatus.DONE
            } catch (t: Throwable) {
                _status.value = TMDBApiStatus.ERROR
            }
        }
    }

    val media = when (mediaType) {
        MediaType.Movie -> repository.movies
        else -> repository.tvShows
    }

    fun displayMediaDetails(media: Media) {
        _navigateToSelectedMedia.value = media
    }

    fun displayMediaDetailsComplete() {
        _navigateToSelectedMedia.value = null
    }

}

@AssistedFactory
interface MediaListViewModelAssistedFactory {
    fun create(mediaType: MediaType): MediaListViewModel
}
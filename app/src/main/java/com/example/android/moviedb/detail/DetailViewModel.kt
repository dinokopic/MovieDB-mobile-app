package com.example.android.moviedb.detail

import androidx.lifecycle.*
import com.example.android.moviedb.Media
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DetailViewModel @AssistedInject constructor(@Assisted private val media: Media) : ViewModel() {

    class Factory(
        private val assistedFactory: DetailViewModelAssistedFactory,
        private val media: Media,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return assistedFactory.create(media) as T
        }
    }

    private val _selectedMedia = MutableLiveData<Media>()
    val selectedMedia: LiveData<Media>
        get() = _selectedMedia

    init {
        _selectedMedia.value = media
    }

}

@AssistedFactory
interface DetailViewModelAssistedFactory {
    fun create(media: Media): DetailViewModel
}
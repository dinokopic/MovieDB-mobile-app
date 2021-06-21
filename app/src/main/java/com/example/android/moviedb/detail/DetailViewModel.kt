package com.example.android.moviedb.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.moviedb.Media

class DetailViewModel(media: Media, app: Application) : AndroidViewModel(app) {

    private val _selectedMedia = MutableLiveData<Media>()
    val selectedMedia: LiveData<Media>
        get() = _selectedMedia

    init {
        _selectedMedia.value = media
    }

}
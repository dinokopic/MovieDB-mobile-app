package com.example.android.moviedb

import android.app.Application
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.moviedb.network.MediaType
import com.example.android.moviedb.network.TMDBApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel(private val mediaType: MediaType, app: Application): AndroidViewModel(app) {

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _topMediaListVisibility = MutableLiveData(View.VISIBLE)
    val topMediaListVisibility: LiveData<Int>
        get() = _topMediaListVisibility

    private val _searchListVisibility = MutableLiveData(View.GONE)
    val searchListVisibility: LiveData<Int>
        get() = _searchListVisibility

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String>
        get() = _searchQuery

    private val _media = MutableLiveData<List<Media>>()
    val media: LiveData<List<Media>>
        get() = _media

    private val _navigateToSelectedMedia = MutableLiveData<Media>()
    val navigateToSelectedMedia: LiveData<Media>
        get() = _navigateToSelectedMedia

    private fun getMediaFromQuery(query: String, mediaType: MediaType) {
        coroutineScope.launch {
            val getMediaDeferred = TMDBApi.retrofitService.getMediaFromQuery(mediaType.type, query)
            fetchData(getMediaDeferred, _media)
        }
    }

    fun search(query: String) {
        getMediaFromQuery(query, mediaType)
    }

    fun displayMediaDetails(media: Media) {
        _navigateToSelectedMedia.value = media
    }

    fun displayMediaDetailsComplete() {
        _navigateToSelectedMedia.value = null
        showSearchResults()
    }

    fun hideSearchResults() {
        _searchListVisibility.value = View.GONE
        _topMediaListVisibility.value = View.VISIBLE
        Log.i("SEARCH INVISIBLE", searchListVisibility.value.toString())
    }

    fun showSearchResults() {
        _searchListVisibility.value = View.VISIBLE
        _topMediaListVisibility.value = View.GONE
        Log.i("SEARCH VISIBLE", searchListVisibility.value.toString())
    }

    fun setQueryValue(query: String?) {
        _searchQuery.value = query
    }

    fun removeMedia() {
        _media.value = null
    }

}
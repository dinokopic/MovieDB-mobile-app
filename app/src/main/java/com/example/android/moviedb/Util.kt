package com.example.android.moviedb

import androidx.lifecycle.MutableLiveData
import com.example.android.moviedb.network.MediaResults
import kotlinx.coroutines.Deferred

enum class TMDBApiStatus {
    ERROR, LOADING, DONE
}

suspend fun fetchData(mediasDeferred: Deferred<MediaResults>, medias: MutableLiveData<List<Media>>) {
    val mediaResults = mediasDeferred.await()
    if (mediaResults.results.isNotEmpty()) {
        mediaResults.results.mapIndexed { index, media ->
            media.rank = index + 1
        }
        medias.value = mediaResults.results
    }
}

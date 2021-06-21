package com.example.android.moviedb

import android.util.Log
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
        Log.i("RESULTS", mediaResults.results.toString())
        medias.value = mediaResults.results
    }
}

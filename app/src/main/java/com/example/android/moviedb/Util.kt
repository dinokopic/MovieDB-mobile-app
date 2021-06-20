package com.example.android.moviedb

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.android.moviedb.network.MediaResults
import kotlinx.coroutines.Deferred

suspend fun fetchData(mediasDeferred: Deferred<MediaResults>, medias: MutableLiveData<List<Media>>) {
    try {
        val mediaResults = mediasDeferred.await()
        if (mediaResults.results.isNotEmpty()) {
            mediaResults.results.mapIndexed { index, media ->
                media.rank = index + 1
            }
            medias.value = mediaResults.results.subList(0, 10)
        }
    } catch (t: Throwable) {

    }
}

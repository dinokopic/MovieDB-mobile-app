package com.example.android.moviedb

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.moviedb.database.MediaDatabase
import com.example.android.moviedb.database.asMovieDomainModel
import com.example.android.moviedb.database.asTVShowDomainModel
import com.example.android.moviedb.network.MediaResults
import com.example.android.moviedb.network.TMDBApi
import com.example.android.moviedb.network.asMovieDatabaseModel
import com.example.android.moviedb.network.asTVShowDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaRepository(private val database: MediaDatabase) {

    val movies: LiveData<List<Media>> = Transformations.map(database.mediaDao.getMovies()) {
        it.asMovieDomainModel()
    }

    val tvShows: LiveData<List<Media>> = Transformations.map(database.mediaDao.getTVShows()) {
        it.asTVShowDomainModel()
    }

    suspend fun refreshMovies() {
        withContext(Dispatchers.IO) {
            val movieResults = TMDBApi.retrofitService.getTopMedia("movie").await()
            movieResults.results.mapIndexed { index, media ->
                media.rank = index + 1
            }
            val results = MediaResults(movieResults.results.subList(0, 10))
            database.mediaDao.insertAll(*results.asMovieDatabaseModel())
        }
    }

    suspend fun refreshTVShows() {
        withContext(Dispatchers.IO) {
            val tvShowResults = TMDBApi.retrofitService.getTopMedia("tv").await()
            tvShowResults.results.mapIndexed { index, media ->
                media.rank = index + 1
            }
            val results = MediaResults(tvShowResults.results.subList(0, 10))
            database.mediaDao.insertAll(*results.asTVShowDatabaseModel())
        }
    }
}
package com.example.android.moviedb.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.moviedb.Media
import com.example.android.moviedb.database.MediaDao
import com.example.android.moviedb.database.asMovieDomainModel
import com.example.android.moviedb.database.asTVShowDomainModel
import com.example.android.moviedb.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaRepository @Inject constructor(private val mediaDao: MediaDao,
                                          private val tmdbApiService: TMDBApiService) {

    val movies: LiveData<List<Media>> = Transformations.map(mediaDao.getMovies()) {
        it.asMovieDomainModel()
    }

    val tvShows: LiveData<List<Media>> = Transformations.map(mediaDao.getTVShows()) {
        it.asTVShowDomainModel()
    }

    suspend fun refreshMovies() {
        withContext(Dispatchers.IO) {
            val movieResults = tmdbApiService.getTopMedia("movie").await()
            movieResults.results.mapIndexed { index, media ->
                media.rank = index + 1
            }
            val results = MediaResults(movieResults.results.subList(0, 10))
            mediaDao.insertAll(*results.asMovieDatabaseModel())
        }
    }

    suspend fun refreshTVShows() {
        withContext(Dispatchers.IO) {
            val tvShowResults = tmdbApiService.getTopMedia("tv").await()
            tvShowResults.results.mapIndexed { index, media ->
                media.rank = index + 1
            }
            val results = MediaResults(tvShowResults.results.subList(0, 10))
            mediaDao.insertAll(*results.asTVShowDatabaseModel())
        }
    }
}
package com.example.android.moviedb.repository

import android.util.Log
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
        val toIndex = if (it.asMovieDomainModel().isEmpty())  0 else 10
        it.asMovieDomainModel().subList(0,toIndex)
    }

    val tvShows: LiveData<List<Media>> = Transformations.map(mediaDao.getTVShows()) {
        val toIndex = if (it.asTVShowDomainModel().isEmpty())  0 else 10
        it.asTVShowDomainModel().subList(0,toIndex)
    }

    suspend fun refreshMovies() {
        withContext(Dispatchers.IO) {
            val movieResults = tmdbApiService.getTopMedia("movie").await()
            movieResults.results.mapIndexed { index, media ->
                media.rank = index + 1
            }
            val results = MediaResults(movieResults.results.subList(0, 10))
            movies.value?.forEach {
                if (results.results.find { movie ->
                        movie.id == it.id
                    } == null) {
                    mediaDao.deleteAllMovies()
                    return@forEach
                }
            }
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
            tvShows.value?.forEach {
                if (results.results.find { tvShow ->
                        tvShow.id == it.id
                    } == null) {
                    mediaDao.deleteAllTVShows()
                    return@forEach
                }
            }
            mediaDao.insertAll(*results.asTVShowDatabaseModel())
        }
    }
}
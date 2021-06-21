package com.example.android.moviedb.network

import com.example.android.moviedb.Media
import com.example.android.moviedb.database.DatabaseMovie
import com.example.android.moviedb.database.DatabaseTVShow

data class MediaResults(
    val results: List<Media>
)

fun MediaResults.asMovieDatabaseModel(): Array<DatabaseMovie> {
    return results.map {
        DatabaseMovie(
            id = it.id,
            title = it.title,
            overview = it.overview,
            posterPath = it.posterPath,
            rank = it.rank
        )
    }.toTypedArray()
}

fun MediaResults.asTVShowDatabaseModel(): Array<DatabaseTVShow> {
    return results.map {
        DatabaseTVShow(
            id = it.id,
            name = it.name,
            overview = it.overview,
            posterPath = it.posterPath,
            rank = it.rank
        )
    }.toTypedArray()
}


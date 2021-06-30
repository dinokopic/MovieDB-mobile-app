package com.example.android.moviedb.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.moviedb.Media

@Entity
data class DatabaseMovie constructor(
    @PrimaryKey
    val id: String,
    val title: String?,
    val overview: String,
    val posterPath: String?,
    var rank: Int
)

@Entity
data class DatabaseTVShow constructor(
    @PrimaryKey
    val id: String,
    val name: String?,
    val overview: String,
    val posterPath: String?,
    var rank: Int
)

fun List<DatabaseMovie>.asMovieDomainModel(): List<Media> {
    return map {
        Media(
            id = it.id,
            title = it.title ?: "",
            name = "",
            overview = it.overview,
            posterPath = it.posterPath,
            rank = indexOf(it) + 1
            )
    }
}

fun List<DatabaseTVShow>.asTVShowDomainModel(): List<Media> {
    return map {
        Media(
            id = it.id,
            title = "",
            name = it.name ?: "",
            overview = it.overview,
            posterPath = it.posterPath,
            rank = indexOf(it) + 1
        )
    }
}

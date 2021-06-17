package com.example.android.moviedb

import com.squareup.moshi.Json

data class Movie(
    val id: String,
    val title: String,
    val overview: String,
    @Json(name = "poster_path") val posterPath: String
)
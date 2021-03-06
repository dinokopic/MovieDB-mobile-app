package com.example.android.moviedb

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

@Parcelize
data class Media @Inject constructor(
    val id: String,
    val title: String = "",
    val name: String = "",
    val overview: String,
    @Json(name = "poster_path") val posterPath: String?,
    var rank: Int = 0
): Parcelable {
    val titleName: String
        get() = if (title.isNotEmpty()) title else name
}
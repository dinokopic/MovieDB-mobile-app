package com.example.android.moviedb.network

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val API_KEY = "8f9035e5c04d9ace3690999b2f6c5cd6"

interface TMDBApiService {

    @GET("search/{type}?api_key=${API_KEY}")
    fun getMediaFromQuery(@Path("type") type: String, @Query("query") name: String): Deferred<MediaResults>

    @GET("{type}/top_rated?api_key=$API_KEY")
    fun getTopMedia(@Path("type") type: String): Deferred<MediaResults>

}

enum class MediaType(val type: String) {
    Movie("movie"),
    TVShow("tv")
}

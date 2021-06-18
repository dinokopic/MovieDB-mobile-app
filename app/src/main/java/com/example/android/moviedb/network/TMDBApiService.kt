package com.example.android.moviedb.network

import com.example.android.moviedb.Media
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://api.themoviedb.org/3/"
private const val API_KEY = "8f9035e5c04d9ace3690999b2f6c5cd6"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

data class MovieResults(
    val results: List<Media>
)

data class TVShowResults(
    val results: List<Media>
)

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface TMDBApiService {
    @GET("movie/top_rated?api_key=$API_KEY")
    fun getMovies(): Deferred<MovieResults>

    @GET("tv/top_rated?api_key=$API_KEY")
    fun getTVShows(): Deferred<TVShowResults>
}

object TMDBApi {
    val retrofitService: TMDBApiService by lazy {
        retrofit.create(TMDBApiService::class.java)
    }
}
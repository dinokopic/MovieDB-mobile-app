package com.example.android.moviedb.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/"
private const val API_KEY = "8f9035e5c04d9ace3690999b2f6c5cd6"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .client(client)
    .build()

interface TMDBApiService {
    @GET("search/{type}?api_key=${API_KEY}")
    fun getMediaFromQuery(@Path("type") type: String, @Query("query") name: String): Deferred<MediaResults>

    @GET("{type}/top_rated?api_key=$API_KEY")
    fun getTopMedia(@Path("type") type: String): Deferred<MediaResults>
}

object TMDBApi {
    val retrofitService: TMDBApiService by lazy {
        retrofit.create(TMDBApiService::class.java)
    }
}

enum class MediaType(val type: String) {
    Movie("movie"),
    TVShow("tv")
}

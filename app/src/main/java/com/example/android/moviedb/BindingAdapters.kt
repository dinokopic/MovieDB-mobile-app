package com.example.android.moviedb

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/original"

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Movie>?) {
    val adapter = recyclerView.adapter as MovieListAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, posterPath: String?) {
    posterPath?.let {
        val imgUrl = POSTER_BASE_URL + posterPath
        Log.i("MOJ TAG", imgUrl)
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }
}

@BindingAdapter("fitOverview")
fun bindOverview(textView: TextView, overview: String?) {
    overview?.let {
        var text = overview
        if (text.length > 200) {
            text = text.subSequence(0, 199).toString() + "..."
        }
        textView.text = text
    }
}
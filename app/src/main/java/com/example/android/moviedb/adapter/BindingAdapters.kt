package com.example.android.moviedb.adapter

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.moviedb.Media
import com.example.android.moviedb.R
import com.example.android.moviedb.TMDBApiStatus

private const val POSTER_BASE_URL = "https://image.tmdb.org/t/p/original"

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Media>?) {
    val adapter = recyclerView.adapter as MediaListAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, posterPath: String?) {
    posterPath?.let {
        val imgUrl = POSTER_BASE_URL + posterPath
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }
    if (posterPath.isNullOrEmpty()) {
        imgView.setImageResource(R.drawable.movie_icon)
    }
}

@BindingAdapter("overview")
fun bindNoOverview(textView: TextView, overview: String?) {
    overview?.let {
        var text = overview
        if (overview.isEmpty()) {
            val context = textView.context
            text = context.getString(R.string.no_overview)
        }
        textView.text = text
    }
}

@BindingAdapter("tmdbApiStatus", "listData")
fun bindStatus(textView: TextView, status: TMDBApiStatus?, data: List<Media>?) {
    val context = textView.context
    if (status == TMDBApiStatus.ERROR) {
        textView.text = context.getString(R.string.no_connection)
        textView.visibility = View.VISIBLE
    } else if (status == TMDBApiStatus.DONE && data.isNullOrEmpty()) {
        textView.text = context.getString(R.string.no_results)
        textView.visibility = View.VISIBLE
    } else {
        textView.visibility = View.GONE
    }
}

@BindingAdapter("tmdbApiStatus")
fun bindProgressStatus(progressBar: ProgressBar, status: TMDBApiStatus?) {
    when (status) {
        TMDBApiStatus.LOADING -> {
            progressBar.visibility = View.VISIBLE
        }
        else -> {
            progressBar.visibility = View.GONE
        }
    }
}

@BindingAdapter("maxLinesScalable")
fun bindMaxLines(textView: TextView, boolean: Boolean) {
    if (boolean) {
        when (textView.context.resources.configuration.fontScale) {
            0.85f -> textView.maxLines = 4
            1.15f, 1.3f -> textView.maxLines = 2
            else -> textView.maxLines = 3
        }
    }
}

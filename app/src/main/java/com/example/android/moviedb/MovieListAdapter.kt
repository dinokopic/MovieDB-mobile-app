package com.example.android.moviedb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.moviedb.databinding.ListViewItemBinding

class MovieListAdapter : ListAdapter<Movie, MovieListAdapter.MovieViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieListAdapter.MovieViewHolder {
        return MovieViewHolder(ListViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MovieListAdapter.MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    class MovieViewHolder(private var binding: ListViewItemBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(movie: Movie) {
                binding.movie = movie
                binding.executePendingBindings()
            }

    }

}
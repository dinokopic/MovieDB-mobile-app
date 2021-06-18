package com.example.android.moviedb

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.moviedb.databinding.ListViewItemBinding

class MediaListAdapter(private val onClickListener: OnClickListener)
    : ListAdapter<Media, MediaListAdapter.MediaViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MediaListAdapter.MediaViewHolder {
        return MediaViewHolder(ListViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MediaListAdapter.MediaViewHolder, position: Int) {
        val media = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.clickListener(media)
        }
        holder.bind(media)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Media>() {
        override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean {
            return oldItem == newItem
        }
    }

    class MediaViewHolder(private var binding: ListViewItemBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(media: Media) {
                binding.media = media
                binding.executePendingBindings()
            }

    }

    class OnClickListener(val clickListener: (media: Media) -> Unit) {
        fun onClick(media: Media) = clickListener(media)
    }

}
package com.example.videoplayer.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.videoplayer.R
import com.example.videoplayer.domain.model.Video
import com.example.videoplayer.databinding.ItemVideoBinding

class VideoAdapter(
    private val onItemClick: (Video) -> Unit
) : ListAdapter<Video, VideoAdapter.VideoViewHolder>(VideoDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VideoViewHolder(private val binding: ItemVideoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(video: Video) {
            binding.title.text = video.title
            binding.duration.text = video.duration

            Glide.with(binding.root)
                .load(video.thumbnailUrl)
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(binding.thumbnail)

            binding.root.setOnClickListener {
                onItemClick(video)
            }
        }
    }

    companion object {
        private val VideoDiffCallback = object : DiffUtil.ItemCallback<Video>() {
            override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
                return oldItem == newItem
            }
        }
    }
}
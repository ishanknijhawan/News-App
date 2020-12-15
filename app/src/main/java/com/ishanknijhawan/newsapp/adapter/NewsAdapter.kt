package com.ishanknijhawan.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ishanknijhawan.newsapp.R
import com.ishanknijhawan.newsapp.data.News
import com.ishanknijhawan.newsapp.databinding.NewsItemBinding

class NewsAdapter(private val listener: OnNewsItemClickListener) :
    PagingDataAdapter<News, NewsAdapter.ViewHolder>(PHOTO_COMPARITOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class ViewHolder(private val binding: NewsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }

            binding.btnShare.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onClickShare(item)
                    }
                }
            }
        }

        fun bind(news: News) {
            binding.apply {
                Glide.with(itemView).load(news.urlToImage)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(ivNewsImage)

                tvNewsHeadline.text = news.title
            }
        }
    }

    interface OnNewsItemClickListener {
        fun onItemClick(news: News)
        fun onClickShare(news: News)
        fun dismiss()
    }

    companion object {
        private val PHOTO_COMPARITOR = object : DiffUtil.ItemCallback<News>() {
            override fun areItemsTheSame(oldItem: News, newItem: News) = false
            override fun areContentsTheSame(oldItem: News, newItem: News) = oldItem == newItem

        }
    }
}

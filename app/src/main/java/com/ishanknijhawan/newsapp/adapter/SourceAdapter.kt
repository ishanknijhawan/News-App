package com.ishanknijhawan.newsapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.ishanknijhawan.newsapp.R
import com.ishanknijhawan.newsapp.data.News
import com.ishanknijhawan.newsapp.databinding.NewsItemBinding
import com.ishanknijhawan.newsapp.databinding.SourceItemBinding
import javax.xml.transform.Source

class SourceAdapter(
    private val items: List<com.ishanknijhawan.newsapp.data.Source>,
    private val listener: OnSourceItemClickListener
) : RecyclerView.Adapter<SourceAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceAdapter.ViewHolder {
        val binding = SourceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)

    }

    inner class ViewHolder(private val binding: SourceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = items[position]
                    listener.onItemClick(item)
                }
            }
        }

        fun bind(source: com.ishanknijhawan.newsapp.data.Source) {
            binding.apply {
                cvDetails.elevation = 10F
                tvSourceTitle.text = source.name
                tvSourceDescription.text = source.description
                tvSourceCategory.text = source.category
            }
        }
    }

    interface OnSourceItemClickListener {
        fun onItemClick(source: com.ishanknijhawan.newsapp.data.Source)
    }
}
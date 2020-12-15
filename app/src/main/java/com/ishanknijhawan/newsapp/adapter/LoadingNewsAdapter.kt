package com.ishanknijhawan.newsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ishanknijhawan.newsapp.databinding.ItemFooterBinding

class LoadingNewsAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<LoadingNewsAdapter.LoadingPageViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingPageViewHolder {
        val binding = ItemFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingPageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadingPageViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadingPageViewHolder(private val binding: ItemFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                tvError.isVisible = loadState !is LoadState.Loading
                btnRetry.isVisible = loadState !is LoadState.Loading
            }
        }
    }
}
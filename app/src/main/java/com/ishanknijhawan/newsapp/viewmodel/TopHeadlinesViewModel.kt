package com.ishanknijhawan.newsapp.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.ishanknijhawan.newsapp.enum.FromEnum
import com.ishanknijhawan.newsapp.repository.NewsRepository

class TopHeadlinesViewModel @ViewModelInject constructor(repository: NewsRepository) :
    ViewModel() {
    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    val news = currentQuery.switchMap { queryString ->
        repository.getNewsPages(queryString, FromEnum.TOP_HEADLINES, null, null, null)
            .cachedIn(viewModelScope)
    }

    fun searchNewsArticles(query: String) {
        currentQuery.value = query
    }

    companion object {
        private val DEFAULT_QUERY: String? = null
    }
}
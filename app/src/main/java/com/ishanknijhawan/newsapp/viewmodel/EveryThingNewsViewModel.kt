package com.ishanknijhawan.newsapp.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.ishanknijhawan.newsapp.custom_switchmap.MultipleSwitchMapTrigger
import com.ishanknijhawan.newsapp.data.News
import com.ishanknijhawan.newsapp.enum.FromEnum
import com.ishanknijhawan.newsapp.repository.NewsRepository

class EveryThingNewsViewModel @ViewModelInject constructor(repository: NewsRepository) :
    ViewModel() {

    private val currentQuery = MutableLiveData(DEFAULT_QUERY)
    private val currentSort = MutableLiveData(DEFAULT_SORT)
    private val currentFromDate = MutableLiveData(DEFAULT_FROM_DATE)
    private val currentToDate = MutableLiveData(DEFAULT_TO_DATE)

    val news = Transformations.switchMap(
        MultipleSwitchMapTrigger(
            currentQuery,
            currentSort,
            Pair(currentFromDate, currentToDate)
        )
    ) {
        repository.getNewsPages(
            it.first,
            FromEnum.EVERYTHING,
            it.second,
            it.third.first,
            it.third.second
        ).cachedIn(viewModelScope)
    }

    fun searchNewsArticles(query: String) {
        currentQuery.value = query
    }

    fun sortNewsArticles(query: String) {
        currentSort.value = query
    }

    fun filterNewsArticles(fromDate: String, toDate: String) {
        currentFromDate.value = fromDate
        currentToDate.value = toDate
    }

    companion object {
        private const val DEFAULT_QUERY = "bitcoin"
        private val DEFAULT_SORT: String? = null
        private val DEFAULT_FROM_DATE: String? = null
        private val DEFAULT_TO_DATE: String? = null
    }
}
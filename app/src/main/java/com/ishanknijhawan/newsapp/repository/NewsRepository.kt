package com.ishanknijhawan.newsapp.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.ishanknijhawan.newsapp.api.NewsApi
import com.ishanknijhawan.newsapp.data.NewsPaging
import com.ishanknijhawan.newsapp.enum.FromEnum
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(private val newsApi: NewsApi) {

    fun getNewsPages(
        query: String?,
        from: FromEnum,
        sortBy: String?,
        fromDate: String?,
        toDate: String?
    ) = Pager(
        config = PagingConfig(
            pageSize = 10,
            maxSize = 100,
            enablePlaceholders = true
        ),
        pagingSourceFactory = { NewsPaging(newsApi, query, from, sortBy, fromDate, toDate) }
    ).liveData
}
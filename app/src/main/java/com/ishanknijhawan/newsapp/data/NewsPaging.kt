package com.ishanknijhawan.newsapp.data

import androidx.paging.PagingSource
import com.ishanknijhawan.newsapp.api.NewsApi
import com.ishanknijhawan.newsapp.response.NewsResponse
import com.ishanknijhawan.newsapp.enum.FromEnum
import retrofit2.HttpException
import java.io.IOException

const val PAGING_INDEX = 1

class NewsPaging(
    private val newsApi: NewsApi,
    private val query: String?,
    private val from: FromEnum,
    private val sortBy: String?,
    private val fromDate: String?,
    private val toDate: String?
) :
    PagingSource<Int, News>() {
    private lateinit var response: NewsResponse
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, News> {
        val position = params.key ?: PAGING_INDEX
        return try {
            response = if (from == FromEnum.EVERYTHING) {
                newsApi.getEverything(
                    query,
                    params.loadSize,
                    position,
                    sortBy,
                    fromDate,
                    toDate,
                    NewsApi.API_KEY,
                )
            } else {
                newsApi.getTopHeadlines(query, "us", params.loadSize, position, NewsApi.API_KEY)
            }
            val news = response.articles

            LoadResult.Page(
                data = news,
                prevKey = if (position == PAGING_INDEX) null else position - 1,
                nextKey = if (news.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}
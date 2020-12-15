package com.ishanknijhawan.newsapp.api

import com.ishanknijhawan.newsapp.response.NewsResponse
import com.ishanknijhawan.newsapp.response.SourceResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    companion object {
        const val BASE_URL = "https://newsapi.org/v2/"
        //50 requests were complete, so created a key with different account
        //const val API_KEY = "a639ae68256348a5bf3e8415f3bdab47"
        const val API_KEY = "95d45b9b9d8b4cc1bbe85cf5788b93a1"
    }

    @GET("top-headlines")
    suspend fun getTopHeadlines(
        @Query("q") query: String?,
        @Query("country") country: String,
        @Query("pageSize") page: Int,
        @Query("page") perPage: Int,
        @Query("apiKey") apiKey: String,
    ): NewsResponse

    @GET("everything")
    suspend fun getEverything(
        @Query("q") query: String?,
        @Query("pageSize") page: Int,
        @Query("page") perPage: Int,
        @Query("sortBy") sortBy: String?,
        @Query("from") fromDate: String?,
        @Query("to") toDate: String?,
        @Query("apiKey") apiKey: String,
    ): NewsResponse

    @GET("sources")
    suspend fun getSources(
        @Query("apiKey") apiKey: String
    ): SourceResponse
}
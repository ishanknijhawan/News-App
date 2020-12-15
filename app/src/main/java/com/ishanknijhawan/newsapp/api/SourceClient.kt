package com.ishanknijhawan.newsapp.api

import okhttp3.OkHttpClient
import okhttp3.Request

object SourceClient {
    private val okHttpClient = OkHttpClient()
    private val request = Request
        .Builder()
        .url("${NewsApi.BASE_URL}sources?apiKey=${NewsApi.API_KEY}")
        .build()

    val api = okHttpClient.newCall(
        request
    )
}
package com.ishanknijhawan.newsapp.data


import com.google.gson.annotations.SerializedName

data class News(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    @SerializedName("source")
    val source: SourceX,
    val title: String,
    val url: String,
    val urlToImage: String
)

data class SourceX(
    val id: Any,
    val name: String
)
package com.javipacheco.demokotlinakka.services

import akme.*
import kategory.*
import com.javipacheco.demokotlinakka.api.RedditApi
import com.javipacheco.demokotlinakka.models.Events
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiService {

    private val redditApi: RedditApi

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://www.reddit.com")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        redditApi = retrofit.create(RedditApi::class.java)
    }

    fun getNews(after: String, limit: String): Service<ListKW<Events.RedditNewsDataEvent>> =
            redditApi.getTop(after, limit).toService().map { ListKW(it.data.children).map { it.data.toEvent() } }

}
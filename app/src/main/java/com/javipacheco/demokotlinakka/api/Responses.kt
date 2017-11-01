package com.javipacheco.demokotlinakka.api

import com.javipacheco.demokotlinakka.models.Events

sealed class Responses {

    data class RedditNewsResponse(val data: RedditDataResponse) : Responses()

    data class RedditDataResponse(
            val children: List<RedditChildrenResponse>,
            val after: String?,
            val before: String?
    ) : Responses()

    data class RedditChildrenResponse(val data: RedditNewsDataResponse) : Responses()

    data class RedditNewsDataResponse(
            val author: String,
            val title: String,
            val num_comments: Int,
            val created: Long,
            val thumbnail: String,
            val url: String
    ) : Responses() {

        fun toEvent(): Events.RedditNewsDataEvent =
                Events.RedditNewsDataEvent(
                        author, title, num_comments, created, thumbnail, url
                )

    }

}
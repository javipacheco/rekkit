package com.javipacheco.demokotlinakka.models

import kategory.*

sealed class Events {

    data class RedditNewsDataEvent(
            val author: String,
            val title: String,
            val num_comments: Int,
            val created: Long,
            val thumbnailUrl: String,
            val imageUrl: Option<String>,
            val url: String
    ) : Events()

}

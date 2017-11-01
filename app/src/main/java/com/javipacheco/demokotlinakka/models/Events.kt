package com.javipacheco.demokotlinakka.models

sealed class Events {

    data class RedditNewsDataEvent(
            val author: String,
            val title: String,
            val num_comments: Int,
            val created: Long,
            val thumbnail: String,
            val url: String
    ) : Events()

}

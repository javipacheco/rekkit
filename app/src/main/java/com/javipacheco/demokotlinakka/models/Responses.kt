package com.javipacheco.demokotlinakka.models

sealed class Responses {

    class RedditNewsResponse(val data: RedditDataResponse) : Responses()

    class RedditDataResponse(
            val children: List<RedditChildrenResponse>,
            val after: String?,
            val before: String?
    ) : Responses()

    class RedditChildrenResponse(val data: RedditNewsDataResponse) : Responses()

    class RedditNewsDataResponse(
            val author: String,
            val title: String,
            val num_comments: Int,
            val created: Long,
            val thumbnail: String,
            val url: String
    ) : Responses()

}
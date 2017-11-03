package com.javipacheco.demokotlinakka.api

import com.javipacheco.demokotlinakka.models.Events
import kategory.Option

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
            val url: String,
            val preview: RedditNewsPreviewResponse?
    ) : Responses() {

        fun toEvent(): Events.RedditNewsDataEvent =
                Events.RedditNewsDataEvent(
                        author, title, num_comments, created, thumbnail, Option.fromNullable(preview?.images?.firstOrNull()?.source?.url), url
                )

    }

    data class RedditNewsPreviewResponse(
            val enabled: Boolean,
            val images: List<RedditNewsImagesResponse?>)

    data class RedditNewsImagesResponse(
            val source: RedditNewsImageResponse,
            val resolutions: List<RedditNewsImageResponse>)

    data class RedditNewsImageResponse(
            val url: String,
            val width: Int,
            val height: Int)

}
package rekkit.api

import akme.toOption
import rekkit.models.States
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
            val id: String,
            val name: String,
            val author: String,
            val title: String,
            val num_comments: Int,
            val created: Long,
            val thumbnail: String,
            val url: String,
            val preview: RedditNewsPreviewResponse?
    ) : Responses() {

        fun toNewsItemState(): States.NewsItemState =
                States.NewsItemState(
                        id,
                        name,
                        author,
                        title,
                        num_comments,
                        created,
                        thumbnail,
                        preview?.images?.firstOrNull()?.source?.url.toOption(),
                        url
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
package com.javipacheco.demokotlinakka.models

import kategory.*

sealed class States {

    data class NewsState(val items: ListKW<NewsItemState>) : States()

    data class NewsItemState(
            val id: String,
            val name: String,
            val author: String,
            val title: String,
            val num_comments: Int,
            val created: Long,
            val thumbnailUrl: String,
            val imageUrl: Option<String>,
            val url: String
    ) : States()

}

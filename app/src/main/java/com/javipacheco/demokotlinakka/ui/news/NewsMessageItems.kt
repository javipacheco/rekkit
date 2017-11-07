package com.javipacheco.demokotlinakka.ui.news

sealed class NewsMessageItems {

    object NoNewsMessage : NewsMessageItems()
    object ErrorLoadingNewsMessage : NewsMessageItems()
    object ErrorLoadingViewsMessage : NewsMessageItems()

}
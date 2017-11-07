package com.javipacheco.demokotlinakka.models

import com.javipacheco.demokotlinakka.ui.main.NavigationItems
import com.javipacheco.demokotlinakka.ui.news.NewsMessageItems

sealed class Commands {

    // Main
    data class NavigationCommand(val item: NavigationItems) : Commands()
    data class ShowMessageCommand(val item: NewsMessageItems) : Commands()
    object CloseDrawerCommand : Commands()

    // News
    data class GetNewsCommand(val limit: Int) : Commands()

    // Navigation
    object LoadNews : Commands()
    data class GoToUrl(val url: String) : Commands()

}
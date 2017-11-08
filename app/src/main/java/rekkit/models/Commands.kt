package rekkit.models

import rekkit.ui.main.MainMessageItems
import rekkit.ui.main.MainNavigationItems
import rekkit.ui.news.NewsMessageItems

sealed class Commands {

    // Main
    data class MainNavigationCommand(val item: MainNavigationItems) : Commands()
    data class MainShowMessageCommand(val item: MainMessageItems) : Commands()
    object MainCloseDrawerCommand : Commands()

    // News
    data class NewsGetItemsCommand(val limit: Int) : Commands()
    data class NewsShowMessageCommand(val item: NewsMessageItems) : Commands()

    // Navigation
    object NavigationLoadNewsCommand : Commands()
    data class NavigationGoToUrlCommand(val url: String) : Commands()

}
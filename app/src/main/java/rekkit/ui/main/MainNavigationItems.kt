package rekkit.ui.main

import rekkit.R

sealed class MainNavigationItems(val id: Int) {

    object News : MainNavigationItems(R.id.nav_news)
    object GitHub : MainNavigationItems(R.id.nav_github)
    object NotFound : MainNavigationItems(0)

    companion object {

        fun toNavigationItem(itemId: Int): MainNavigationItems = when (itemId) {
            News.id -> News
            GitHub.id -> GitHub
            else -> NotFound
        }

    }

}
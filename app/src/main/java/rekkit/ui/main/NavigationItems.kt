package rekkit.ui.main

import rekkit.R

sealed class NavigationItems(val id: Int) {

    object News : NavigationItems(R.id.nav_news)
    object GitHub : NavigationItems(R.id.nav_github)
    object NotFound : NavigationItems(0)

    companion object {

        fun toNavigationItem(itemId: Int): NavigationItems = when (itemId) {
            News.id -> News
            GitHub.id -> GitHub
            else -> NotFound
        }

    }

}
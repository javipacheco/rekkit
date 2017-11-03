package com.javipacheco.demokotlinakka.ui.main

import com.javipacheco.demokotlinakka.R

sealed class NavigationItems {

    object News : NavigationItems()
    object GitHub : NavigationItems()
    object NotFound : NavigationItems()

    companion object {

        fun toNavigationItem(itemId: Int): NavigationItems = when (itemId) {
            R.id.nav_news -> News
            R.id.nav_github -> GitHub
            else -> NotFound
        }

    }

}
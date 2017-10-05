package com.javipacheco.demokotlinakka.ui.main

import com.javipacheco.demokotlinakka.R

sealed class NavigationItems {

    object Camera : NavigationItems()
    object Gallery : NavigationItems()
    object SlideShow : NavigationItems()
    object Manage : NavigationItems()
    object Share : NavigationItems()
    object Send : NavigationItems()
    object NotFound : NavigationItems()

    companion object {

        fun toNavigationItem(itemId: Int): NavigationItems = when (itemId) {
            R.id.nav_camera -> Camera
            R.id.nav_gallery -> Gallery
            R.id.nav_slideshow -> SlideShow
            R.id.nav_manage -> Manage
            R.id.nav_share -> Share
            R.id.nav_send -> Send
            else -> NotFound
        }

    }

}
package com.javipacheco.demokotlinakka.services

import com.javipacheco.demokotlinakka.ui.main.NavigationItems
import akme.*
import com.javipacheco.demokotlinakka.models.Events
import kategory.ListKW

interface MainUiService {

    fun init(): Service<Unit>

    fun writeMessage(items: ListKW<Events.RedditNewsDataEvent>): Service<Unit>

    fun showMessage(msg: String): Service<Unit>

    fun navigation(item: NavigationItems): Service<Unit>

    fun closeDrawer(): Service<Unit>

}
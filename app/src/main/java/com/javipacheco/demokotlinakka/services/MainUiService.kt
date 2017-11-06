package com.javipacheco.demokotlinakka.services

import com.javipacheco.demokotlinakka.ui.main.NavigationItems
import akme.*
import com.javipacheco.demokotlinakka.models.States
import kategory.ListKW

interface MainUiService {

    fun init(): Service<Unit>

    fun showLoading(): Service<Unit>

    fun showNews(items: ListKW<States.NewsItemState>): Service<Unit>

    fun showMessage(msg: String): Service<Unit>

    fun navigation(item: NavigationItems): Service<Unit>

    fun closeDrawer(): Service<Unit>

}
package com.javipacheco.demokotlinakka.ui.news

import akme.Service
import com.javipacheco.demokotlinakka.models.States
import com.javipacheco.demokotlinakka.ui.main.NavigationItems
import kategory.ListKW

interface NewsUiService {

    fun showLoading(): Service<Unit>

    fun showNews(items: ListKW<States.NewsItemState>): Service<Unit>

    fun showMessage(item: NewsMessageItems): Service<Unit>

}
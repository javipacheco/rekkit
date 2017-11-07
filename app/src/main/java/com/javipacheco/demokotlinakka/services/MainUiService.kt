package com.javipacheco.demokotlinakka.services

import com.javipacheco.demokotlinakka.ui.main.NavigationItems
import akme.*
import com.javipacheco.demokotlinakka.models.States
import com.javipacheco.demokotlinakka.ui.main.MainMessageItems
import kategory.ListKW

interface MainUiService {

    fun showMessage(item: MainMessageItems): Service<Unit>

    fun navigation(item: NavigationItems): Service<Unit>

    fun closeDrawer(): Service<Unit>

}
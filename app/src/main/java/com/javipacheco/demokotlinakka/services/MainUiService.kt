package com.javipacheco.demokotlinakka.services

import com.javipacheco.demokotlinakka.ui.main.NavigationItems
import katkka.*

interface MainUiService {

    fun init(): Service<Unit>

    fun writeMessage(msg: String): Service<Unit>

    fun showMessage(msg: String): Service<Unit>

    fun navigation(item: NavigationItems): Service<Unit>

    fun closeDrawer(): Service<Unit>

}
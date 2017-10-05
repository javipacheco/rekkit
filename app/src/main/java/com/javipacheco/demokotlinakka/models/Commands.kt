package com.javipacheco.demokotlinakka.models

import com.javipacheco.demokotlinakka.ui.main.NavigationItems

sealed class Commands {

    object InitCommand : Commands()
    object FillMessageCommand : Commands()
    data class ShowMessageCommand(val msg: String) : Commands()
    data class NavigationCommand(val item: NavigationItems) : Commands()
    object CloseDrawerCommand : Commands()

}
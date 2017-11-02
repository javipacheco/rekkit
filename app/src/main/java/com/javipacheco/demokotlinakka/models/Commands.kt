package com.javipacheco.demokotlinakka.models

import com.javipacheco.demokotlinakka.ui.main.NavigationItems

sealed class Commands {

    // Main
    object InitCommand : Commands()
    object InitFailureCommand : Commands()
    data class GetNewsCommand(val limit: Int) : Commands()
    data class ShowMessageCommand(val msg: String) : Commands()
    data class NavigationCommand(val item: NavigationItems) : Commands()
    object CloseDrawerCommand : Commands()

    // Navigation
    data class GoToUrl(val url: String) : Commands()

}
package com.javipacheco.demokotlinakka.ui.main

sealed class MainMessageItems {

    object NavigationErrorMessage : MainMessageItems()
    object ItemNotFoundMessage : MainMessageItems()

}
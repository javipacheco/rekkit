package rekkit.ui.main

sealed class MainMessageItems {

    object NavigationErrorMessage : MainMessageItems()
    object ItemNotFoundMessage : MainMessageItems()

}
package rekkit.models

sealed class Notifications {

    // Main
    object MainNavigationFailedNotification : Notifications()

    // News
    object NewsNotLoadedNotification : Notifications()
    object NewsUiErrorNotification : Notifications()

}
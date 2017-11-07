package rekkit.models

sealed class Notifications {

    // Main
    object NavigationFailedNotification : Notifications()

    // News
    object NewsNotLoadedNotification : Notifications()
    object UiErrorNotification : Notifications()

}
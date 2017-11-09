package rekkit.actors

import akme.*
import kategory.*
import kotlinx.coroutines.experimental.channels.actor
import rekkit.models.Commands
import rekkit.models.Notifications
import rekkit.ui.main.MainUiService
import rekkit.ui.main.MainMessageItems

class MainActors(val uiService: MainUiService) {

    val mainActor = actor<Commands> {
        for (msg in channel) {
            when (msg) {
                is Commands.MainNavigationCommand ->
                    ServiceMonad().binding {
                        uiService.navigation(msg.item).bind()
                        uiService.closeDrawer().bind()
                        yields(Unit)
                    }.ev().fold({ _ ->
                        notificationActor.sendBlocking(Notifications.MainNavigationFailedNotification)
                    }, {})
                is Commands.MainCloseDrawerCommand ->
                    uiService.closeDrawer().fold({ _ ->
                        notificationActor.sendBlocking(Notifications.MainNavigationFailedNotification)
                    }, {})
                is Commands.MainShowMessageCommand ->
                    uiService.showMessage(msg.item)
            }
        }
    }

    val notificationActor = actor<Notifications> {
        for (msg in channel) {
            when (msg) {
                is Notifications.MainNavigationFailedNotification ->
                    uiService.showMessage(MainMessageItems.NavigationErrorMessage)
            }
        }
    }

}
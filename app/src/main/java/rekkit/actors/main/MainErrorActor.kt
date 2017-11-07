package rekkit.actors.main

import akka.actor.AbstractActor
import akka.actor.Props
import rekkit.models.Notifications.NavigationFailedNotification
import rekkit.services.MainUiService
import rekkit.ui.main.MainMessageItems

class MainErrorActor(val uiService: MainUiService): AbstractActor() {

    override fun createReceive(): Receive = receiveBuilder()
            .match(NavigationFailedNotification::class.java, { _ ->
                uiService.showMessage(MainMessageItems.NavigationErrorMessage)
            })
            .build()

    companion object {

        fun props(uiService: MainUiService): Props =
                Props.create(MainErrorActor::class.java, uiService)

    }
}
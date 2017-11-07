package com.javipacheco.demokotlinakka.actors.main

import akka.actor.AbstractActor
import akka.actor.Props
import com.javipacheco.demokotlinakka.models.Notifications.NavigationFailedNotification
import com.javipacheco.demokotlinakka.services.MainUiService
import com.javipacheco.demokotlinakka.ui.main.MainMessageItems

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
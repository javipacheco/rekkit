package com.javipacheco.demokotlinakka.actors.main

import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.Props
import com.javipacheco.demokotlinakka.models.Commands.*
import com.javipacheco.demokotlinakka.services.ApiService
import com.javipacheco.demokotlinakka.services.MainUiService
import kategory.*
import akme.*
import com.javipacheco.demokotlinakka.models.Notifications.*

class MainActor(val errorActor: ActorRef, val apiService: ApiService, val uiService: MainUiService): AbstractActor() {

    override fun createReceive(): Receive = receiveBuilder()
        .match(NavigationCommand::class.java, { cmd ->
            ServiceMonad().binding {
                uiService.navigation(cmd.item).bind()
                uiService.closeDrawer().bind()
                yields(Unit)
            }.ev().fold({ _ ->
                errorActor.tell(NavigationFailedNotification, context.system.deadLetters())
            }, {})
        })
        .match(CloseDrawerCommand::class.java, { cmd ->
            uiService.closeDrawer().fold({ _ ->
                errorActor.tell(NavigationFailedNotification, context.system.deadLetters())
            }, {})
        })
        .build()

    companion object {

        fun props(errorActor: ActorRef, apiService: ApiService, uiService: MainUiService): Props =
                Props.create(MainActor::class.java, errorActor, apiService, uiService)

    }

}
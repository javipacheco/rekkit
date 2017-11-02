package com.javipacheco.demokotlinakka.actors.commons

import akka.actor.AbstractActor
import akka.actor.Props
import akme.*
import kategory.*
import com.javipacheco.demokotlinakka.models.Commands
import com.javipacheco.demokotlinakka.services.MainUiService
import com.javipacheco.demokotlinakka.ui.commons.NavigationService

class NavigationActor(val navigationService: NavigationService): AbstractActor() {

    override fun createReceive(): Receive = receiveBuilder()
            .match(Commands.GoToUrl::class.java, { data ->
                ServiceMonad().binding {
                    navigationService.goToWeb(data.url).bind()
                    yields(Unit)
                }
            })
            .build()

    companion object {

        fun props(uiService: MainUiService): Props =
                Props.create(NavigationActor::class.java, uiService)

    }
}
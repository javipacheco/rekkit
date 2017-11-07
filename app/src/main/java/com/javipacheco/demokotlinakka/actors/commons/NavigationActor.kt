package com.javipacheco.demokotlinakka.actors.commons

import akka.actor.AbstractActor
import akka.actor.Props
import com.javipacheco.demokotlinakka.models.Commands.*
import com.javipacheco.demokotlinakka.ui.commons.NavigationService

class NavigationActor(val navigationService: NavigationService): AbstractActor() {

    override fun createReceive(): Receive = receiveBuilder()
            .match(LoadNews::class.java, { _ ->
                navigationService.loadNews()
            })
            .match(GoToUrl::class.java, {
                navigationService.goToWeb(it.url)
            })
            .build()

    companion object {

        fun props(uiService: NavigationService): Props =
                Props.create(NavigationActor::class.java, uiService)

    }
}
package com.javipacheco.demokotlinakka.actors

import akka.actor.AbstractActor
import akka.actor.Props
import akme.ServiceMonad
import com.javipacheco.demokotlinakka.models.Commands
import com.javipacheco.demokotlinakka.services.MainUiService
import kategory.binding

class MainErrorActor(val uiService: MainUiService): AbstractActor() {

    override fun createReceive(): Receive = receiveBuilder()
            .match(Commands.InitFailureCommand::class.java, { _ ->
                ServiceMonad().binding {
                    val s = uiService.showMessage("Error cargando la actividad").bind()
                    yields(Unit)
                }
            })
            .build()

    companion object {

        fun props(uiService: MainUiService): Props =
                Props.create(MainErrorActor::class.java, uiService)

    }
}
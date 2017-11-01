package com.javipacheco.demokotlinakka.actors

import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.Props
import com.javipacheco.demokotlinakka.models.Commands.*
import com.javipacheco.demokotlinakka.services.ApiService
import com.javipacheco.demokotlinakka.services.MainUiService
import kategory.binding
import kategory.ev
import akme.AkmeException
import akme.ServiceMonad

class MainActor(val errorActor: ActorRef, val apiService: ApiService, val uiService: MainUiService): AbstractActor() {

    override fun createReceive(): Receive = receiveBuilder()
        .match(InitCommand::class.java, { _ ->
            ServiceMonad().binding {
                uiService.init().bind()
                yields(Unit)
            }.ev().fold({ ex ->
                when(ex) {
                    is AkmeException.UiException ->
                        errorActor.tell(InitFailureCommand, context.system.deadLetters())
                }
            }, {})
        })
        .match(FillMessageCommand::class.java, { _ ->
            ServiceMonad().binding {
                val msg = apiService.getNews("", "10").bind()
                uiService.writeMessage(msg[0].data.author).bind()
                yields(Unit)
            }
        })
        .match(ShowMessageCommand::class.java, { cmd ->
            ServiceMonad().binding {
                uiService.showMessage(cmd.msg).bind()
                yields(Unit)
            }
        })
        .match(NavigationCommand::class.java, { cmd ->
            ServiceMonad().binding {
                uiService.navigation(cmd.item).bind()
                uiService.closeDrawer().bind()
                yields(Unit)
            }
        })
        .match(CloseDrawerCommand::class.java, { cmd ->
            ServiceMonad().binding {
                uiService.closeDrawer().bind()
                yields(Unit)
            }
        })
        .build()

    companion object {

        fun props(errorActor: ActorRef, apiService: ApiService, uiService: MainUiService): Props =
                Props.create(MainActor::class.java, errorActor, apiService, uiService)

    }

}
package com.javipacheco.demokotlinakka.actors

import akka.actor.AbstractActor
import akka.actor.Props
import com.javipacheco.demokotlinakka.models.Commands.*
import com.javipacheco.demokotlinakka.services.ApiService
import com.javipacheco.demokotlinakka.services.MainUiService
import kategory.binding
import kategory.ev
import katkka.KatkkaException
import katkka.ServiceMonad

class MainActor(val apiService: ApiService, val uiService: MainUiService): AbstractActor() {

    override fun createReceive(): Receive = receiveBuilder()
        .match(InitCommand::class.java, { _ ->
            ServiceMonad().binding {
                val s = uiService.init().bind()
                yields(Unit)
            }.ev().fold({ ex ->
                when(ex) {
                    is KatkkaException.UiException -> Unit
                }
            }, {})
        })
        .match(FillMessageCommand::class.java, { _ ->
            ServiceMonad().binding {
                val msg = apiService.getDogs().bind()
                val s = uiService.writeMessage(msg.getOrElse(0, {"Na de na"})).bind()
                yields(Unit)
            }
        })
        .match(ShowMessageCommand::class.java, { cmd ->
            ServiceMonad().binding {
                val s = uiService.showMessage(cmd.msg).bind()
                yields(Unit)
            }
        })
        .match(NavigationCommand::class.java, { cmd ->
            ServiceMonad().binding {
                val s1 = uiService.navigation(cmd.item).bind()
                val s2 = uiService.closeDrawer().bind()
                yields(Unit)
            }
        })
        .match(CloseDrawerCommand::class.java, { cmd ->
            ServiceMonad().binding {
                val s = uiService.closeDrawer().bind()
                yields(Unit)
            }
        })
        .build()

    companion object {

        fun props(apiService: ApiService, uiService: MainUiService): Props =
                Props.create(MainActor::class.java, apiService, uiService)

    }

}
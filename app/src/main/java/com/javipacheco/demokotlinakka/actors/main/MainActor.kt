package com.javipacheco.demokotlinakka.actors.main

import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.Props
import com.javipacheco.demokotlinakka.models.Commands.*
import com.javipacheco.demokotlinakka.services.ApiService
import com.javipacheco.demokotlinakka.services.MainUiService
import kategory.*
import akme.AkmeException
import akme.ServiceMonad
import com.javipacheco.demokotlinakka.models.States

class MainActor(val errorActor: ActorRef, val apiService: ApiService, val uiService: MainUiService): AbstractActor() {

    private var newsState: States.NewsState = States.NewsState(ListKW.empty())

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
        .match(GetNewsCommand::class.java, { info ->
            ServiceMonad().binding {
                uiService.showLoading()
                val after: Option<String> = Option.fromNullable(newsState.items.getOrNull(0)).map { it.name }
                val news = apiService.getNews(info.limit, after).bind()
                newsState = newsState.copy(items = news.combineK(newsState.items))
                uiService.showNews(news).bind()
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
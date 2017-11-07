package com.javipacheco.demokotlinakka.actors.news

import akka.actor.AbstractActor
import akka.actor.Props
import com.javipacheco.demokotlinakka.models.Notifications.*
import com.javipacheco.demokotlinakka.ui.news.NewsMessageItems.*
import com.javipacheco.demokotlinakka.ui.news.NewsUiService

class NewsErrorActor(val uiService: NewsUiService): AbstractActor() {

    override fun createReceive(): Receive = receiveBuilder()
            .match(NewsNotLoadedNotification::class.java, { _ ->
                uiService.showMessage(ErrorLoadingNewsMessage)
            })
            .match(UiErrorNotification::class.java, { _ ->
                uiService.showMessage(ErrorLoadingViewsMessage)
            })
            .build()

    companion object {

        fun props(uiService: NewsUiService): Props =
                Props.create(NewsErrorActor::class.java, uiService)

    }
}
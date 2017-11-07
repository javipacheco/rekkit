package rekkit.actors.news

import akka.actor.AbstractActor
import akka.actor.Props
import rekkit.models.Notifications.*
import rekkit.ui.news.NewsMessageItems.*
import rekkit.ui.news.NewsUiService

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
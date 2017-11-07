package rekkit.actors.news

import akka.actor.AbstractActor
import akka.actor.ActorRef
import akka.actor.Props
import akme.*
import akme.AkmeException.*
import rekkit.models.Commands.*
import rekkit.models.Notifications.*
import rekkit.models.States
import rekkit.services.ApiService
import rekkit.ui.news.NewsUiService
import kategory.*

class NewsActor(val errorActor: ActorRef, val apiService: ApiService, val uiService: NewsUiService): AbstractActor() {

    private var newsState: States.NewsState = States.NewsState(ListKW.empty())

    override fun createReceive(): Receive = receiveBuilder()
        .match(GetNewsCommand::class.java, { info ->
            ServiceMonad().binding {
                uiService.showLoading()
                val before: Option<String> = newsState.items.headOption().map { it.name }
                val news = apiService.getNews(info.limit, before).bind()
                newsState = newsState.copy(items = news.combineK(newsState.items))
                uiService.showNews(news).bind()
                yields(Unit)
            }.ev().fold({ ex ->
                when(ex) {
                    is ApiException ->
                        errorActor.tell(NewsNotLoadedNotification, context.system.deadLetters())
                    is UiException ->
                        errorActor.tell(UiErrorNotification, context.system.deadLetters())
                }
            }, {})
        })
        .match(ShowMessageCommand::class.java, {
            uiService.showMessage(it.item)
        })
        .build()

    companion object {

        fun props(errorActor: ActorRef, apiService: ApiService, uiService: NewsUiService): Props =
                Props.create(NewsActor::class.java, errorActor, apiService, uiService)

    }

}
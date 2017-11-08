package rekkit.actors

import akme.AkmeException
import akme.ServiceMonad
import akme.headOption
import akme.sendBlocking
import kategory.*
import kotlinx.coroutines.experimental.channels.actor
import rekkit.models.Commands
import rekkit.models.Notifications
import rekkit.models.States
import rekkit.services.ApiService
import rekkit.ui.news.NewsMessageItems
import rekkit.ui.news.NewsUiService

class NewsActors(val apiService: ApiService, val uiService: NewsUiService) {

    val mainActor = actor<Commands> {
        var newsState: States.NewsState = States.NewsState(ListKW.empty())

        for (msg in channel) {
            when (msg) {
                is Commands.NewsGetItemsCommand ->
                    ServiceMonad().binding {
                        uiService.showLoading()
                        val before: Option<String> = newsState.items.headOption().map { it.name }
                        val news = apiService.getNews(msg.limit, before).bind()
                        newsState = newsState.copy(items = news.combineK(newsState.items))
                        uiService.showNews(news).bind()
                        yields(Unit)
                    }.ev().fold({ ex ->
                        when(ex) {
                            is AkmeException.ApiException ->
                                notificationActor.sendBlocking(Notifications.NewsNotLoadedNotification)
                            is AkmeException.UiException ->
                                notificationActor.sendBlocking(Notifications.NewsUiErrorNotification)
                        }
                    }, {})
                is Commands.NewsShowMessageCommand ->
                    uiService.showMessage(msg.item)
            }
        }
    }

    val notificationActor = actor<Notifications> {
        for (msg in channel) {
            when (msg) {
                is Notifications.NewsNotLoadedNotification ->
                    uiService.showMessage(NewsMessageItems.ErrorLoadingNewsMessage)
                is Notifications.NewsUiErrorNotification ->
                    uiService.showMessage(NewsMessageItems.ErrorLoadingViewsMessage)
            }
        }
    }

}
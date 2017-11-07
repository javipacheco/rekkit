package rekkit.ui.news

import akka.actor.ActorSystem
import akme.*
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import rekkit.R
import rekkit.actors.commons.NavigationActor
import rekkit.actors.news.NewsActor
import rekkit.actors.news.NewsErrorActor
import rekkit.models.Commands.*
import rekkit.models.States
import rekkit.services.ApiService
import rekkit.ui.commons.NavigationService
import rekkit.ui.news.NewsMessageItems.*
import rekkit.ui.news.adapters.NewsAdapter
import kategory.ListKW
import kategory.getOrElse
import kotlinx.android.synthetic.main.news_fragment.*

class NewsFragment:
        Fragment(),
        NewsUiService,
        NavigationService,
        SwipeRefreshLayout.OnRefreshListener {

    val system = ActorSystem.create("AkmeSystem")
    val newsErrorActorRef = system.actorOf(NewsErrorActor.props(this), "news-error-actor")
    val navigationActorRef = system.actorOf(NavigationActor.props(this), "navigation-actor")
    val newsActorRef = system.actorOf(NewsActor.props(newsErrorActorRef, ApiService(), this), "news-actor")

    val limit = 10

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.news_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val layoutManager = LinearLayoutManager(activity)
        recycler.setLayoutManager(layoutManager)

        swipe_refresh.setOnRefreshListener(this)

        newsActorRef.tell(GetNewsCommand(limit), system.deadLetters())
    }

    override fun onRefresh() {
        newsActorRef.tell(GetNewsCommand(limit), system.deadLetters())
    }

    // News UI Service

    override fun showLoading(): Service<Unit> =
            activity.runOnUiThread {
                recycler.adapter.toOption().map { _ ->
                    Unit
                }.getOrElse {
                    progress_bar.visibility = View.VISIBLE
                    recycler.visibility = View.GONE
                    Unit
                }

            }.catchUi()

    override fun showNews(items: ListKW<States.NewsItemState>): Service<Unit> =
            activity.runOnUiThread {
                progress_bar.visibility = View.GONE
                recycler.visibility = View.VISIBLE

                if (items.isEmpty()) {
                    newsActorRef.tell(ShowMessageCommand(NoNewsMessage), system.deadLetters())
                } else {
                    recycler.adapter.toOption().map { adapter ->
                        (adapter as NewsAdapter).addAfterItems(items)
                    }.getOrElse {
                        recycler.adapter = NewsAdapter(items) { url ->
                            navigationActorRef.tell(GoToUrl(url), system.deadLetters())
                        }
                        Unit
                    }
                }
                swipe_refresh.setRefreshing(false)
            }.catchUi()

    override fun showMessage(item: NewsMessageItems): Service<Unit> = catchUi {
        val msg = when(item) {
            is NoNewsMessage -> getString(R.string.noMoreNews)
            is ErrorLoadingNewsMessage -> getString(R.string.loadingNewsError)
            is ErrorLoadingViewsMessage -> getString(R.string.loadingNewsError)
        }
        Snackbar.make(swipe_refresh, msg, Snackbar.LENGTH_LONG).show()
    }

}
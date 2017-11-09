package rekkit.ui.news

import akme.Service
import akme.catchUi
import akme.sendBlocking
import akme.toOption
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kategory.ListKW
import kategory.getOrElse
import kotlinx.android.synthetic.main.news_fragment.*
import rekkit.R
import rekkit.actors.NavigationActors
import rekkit.actors.NewsActors
import rekkit.models.Commands.*
import rekkit.models.States
import rekkit.services.ApiService
import rekkit.ui.commons.NavigationService
import rekkit.ui.news.NewsMessageItems.*
import rekkit.ui.news.adapters.NewsAdapter

class NewsFragment:
        Fragment(),
        NewsUiService,
        NavigationService,
        SwipeRefreshLayout.OnRefreshListener {

    val navigationActorRef = NavigationActors(this)
    val newsActorRef = NewsActors(ApiService(), this)

    val limit = 10

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater?.inflate(R.layout.news_fragment, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val layoutManager = LinearLayoutManager(activity)
        recycler.setLayoutManager(layoutManager)

        swipe_refresh.setOnRefreshListener(this)

        newsActorRef.mainActor.sendBlocking(NewsGetItemsCommand(limit))
    }

    override fun onRefresh() {
        newsActorRef.mainActor.sendBlocking(NewsGetItemsCommand(limit))
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
                    newsActorRef.mainActor.sendBlocking(NewsShowMessageCommand(NoNewsMessage))
                } else {
                    recycler.adapter.toOption().map { adapter ->
                        (adapter as NewsAdapter).addAfterItems(items)
                    }.getOrElse {
                        recycler.adapter = NewsAdapter(items) { url ->
                            navigationActorRef.mainActor.sendBlocking(NavigationGoToUrlCommand(url))
                        }
                        Unit
                    }
                }
                swipe_refresh.setRefreshing(false)
            }.catchUi()

    override fun showMessage(item: NewsMessageItems): Service<Unit> = activity.runOnUiThread {
        val msg = when(item) {
            is NoNewsMessage -> getString(R.string.noMoreNews)
            is ErrorLoadingNewsMessage -> getString(R.string.loadingNewsError)
            is ErrorLoadingViewsMessage -> getString(R.string.loadingNewsError)
        }
        Snackbar.make(swipe_refresh, msg, Snackbar.LENGTH_LONG).show()
    }.catchUi()

}
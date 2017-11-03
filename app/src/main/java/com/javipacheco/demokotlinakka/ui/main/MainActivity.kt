package com.javipacheco.demokotlinakka.ui.main

import akka.actor.ActorSystem
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.javipacheco.demokotlinakka.R
import com.javipacheco.demokotlinakka.actors.main.MainActor
import com.javipacheco.demokotlinakka.models.Commands.*
import com.javipacheco.demokotlinakka.services.ApiService
import com.javipacheco.demokotlinakka.services.MainUiService
import com.javipacheco.demokotlinakka.ui.main.NavigationItems.*
import akme.*
import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.javipacheco.demokotlinakka.actors.main.MainErrorActor
import com.javipacheco.demokotlinakka.models.Events
import com.javipacheco.demokotlinakka.ui.main.adapters.NewsAdapter
import kategory.ListKW
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import com.javipacheco.demokotlinakka.actors.commons.NavigationActor
import com.javipacheco.demokotlinakka.ui.commons.NavigationService


class MainActivity :
        AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        MainUiService,
        NavigationService {

    val system = ActorSystem.create("AkmeSystem")
    val mainErrorActorRef = system.actorOf(MainErrorActor.props(this), "main-error-actor")
    val navigationActorRef = system.actorOf(NavigationActor.props(this), "navigation-actor")
    val mainActorRef = system.actorOf(MainActor.props(mainErrorActorRef, ApiService(), this), "main-actor")

    override fun getActivity(): Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActorRef.tell(InitCommand, system.deadLetters())
        mainActorRef.tell(GetNewsCommand(30), system.deadLetters())

        val layoutManager = LinearLayoutManager(this)
        recycler.setLayoutManager(layoutManager)

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            mainActorRef.tell(CloseDrawerCommand, system.deadLetters())
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        mainActorRef.tell(NavigationCommand(Companion.toNavigationItem(item.itemId)), system.deadLetters())
        return true
    }

    // MainUiService

    override fun init(): Service<Unit> = catchUi {
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun showLoading(): Service<Unit> =
            runOnUiThread {
                progress_bar.visibility = View.VISIBLE
                recycler.visibility = View.GONE
            }.catchUi()

    override fun showNews(items: ListKW<Events.RedditNewsDataEvent>): Service<Unit> =
            runOnUiThread {
                progress_bar.visibility = View.GONE
                recycler.visibility = View.VISIBLE
                recycler.adapter = NewsAdapter(items) { url ->
                    navigationActorRef.tell(GoToUrl(url), system.deadLetters())
                }
            }.catchUi()

    override fun showMessage(msg: String): Service<Unit> =
            Snackbar.make(drawer_layout, msg, Snackbar.LENGTH_LONG).show().catchUi()

    override fun navigation(item: NavigationItems): Service<Unit> = when (item) {
        News -> showMessage("News")
        GitHub -> showMessage("GitHub")
        NotFound -> showMessage("Oh!! Noo!!")
    }

    override fun closeDrawer(): Service<Unit> =
            drawer_layout.closeDrawer(GravityCompat.START).catchUi()

}

package rekkit.ui.main

import akka.actor.ActorSystem
import akme.*
import android.app.Activity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import rekkit.R
import rekkit.actors.commons.NavigationActor
import rekkit.actors.main.MainActor
import rekkit.actors.main.MainErrorActor
import rekkit.models.Commands.*
import rekkit.services.ApiService
import rekkit.services.MainUiService
import rekkit.ui.commons.NavigationService
import rekkit.ui.main.MainMessageItems.*
import rekkit.ui.main.NavigationItems.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

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

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        nav_view.setCheckedItem(News.id)
        nav_view.getMenu().performIdentifierAction(News.id, 0);

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

    override fun showMessage(item: MainMessageItems): Service<Unit> = catchUi {
        val msg = when(item) {
            is NavigationErrorMessage -> getString(R.string.navigationError)
            is ItemNotFoundMessage -> getString(R.string.itemNotFoundError)
        }
        Snackbar.make(drawer_layout, msg, Snackbar.LENGTH_LONG).show()
    }

    override fun navigation(item: NavigationItems): Service<Unit> = when (item) {
        News ->
            navigationActorRef.tell(LoadNews, system.deadLetters()).catchUi()
        GitHub ->
            navigationActorRef.tell(GoToUrl(getString(R.string.github_url)), system.deadLetters()).catchUi()
        NotFound ->
            showMessage(ItemNotFoundMessage)
    }

    override fun closeDrawer(): Service<Unit> =
            drawer_layout.closeDrawer(GravityCompat.START).catchUi()

}

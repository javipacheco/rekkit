package rekkit.ui.main

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
import rekkit.actors.NavigationActors
import rekkit.models.Commands.NavigationGoToUrlCommand
import rekkit.models.Commands.NavigationLoadNewsCommand
import rekkit.ui.commons.NavigationService
import rekkit.ui.main.MainMessageItems.ItemNotFoundMessage
import rekkit.ui.main.MainMessageItems.NavigationErrorMessage
import rekkit.ui.main.MainNavigationItems.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import rekkit.actors.MainActors
import rekkit.models.Commands

class MainActivity :
        AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        MainUiService,
        NavigationService {

    val navigationActorRef = NavigationActors(this)
    val mainActorRef = MainActors(this)

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
            mainActorRef.mainActor.sendBlocking(Commands.MainCloseDrawerCommand)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        mainActorRef.mainActor.sendBlocking(Commands.MainNavigationCommand(Companion.toNavigationItem(item.itemId)))
        return true
    }

    // MainUiService

    override fun showMessage(item: MainMessageItems): Service<Unit> = runOnUiThread {
        val msg = when(item) {
            is NavigationErrorMessage -> getString(R.string.navigationError)
            is ItemNotFoundMessage -> getString(R.string.itemNotFoundError)
        }
        Snackbar.make(drawer_layout, msg, Snackbar.LENGTH_LONG).show()
    }.catchUi()

    override fun navigation(item: MainNavigationItems): Service<Unit> = runOnUiThread {
        when (item) {
            News ->
                navigationActorRef
                        .mainActor
                        .sendBlocking(NavigationLoadNewsCommand)
            GitHub ->
                navigationActorRef
                        .mainActor
                        .sendBlocking(NavigationGoToUrlCommand(getString(R.string.github_url)))
            NotFound ->
                navigationActorRef
                        .mainActor
                        .sendBlocking(Commands.MainShowMessageCommand(ItemNotFoundMessage))
        }
    }.catchUi()

    override fun closeDrawer(): Service<Unit> = runOnUiThread {
            drawer_layout.closeDrawer(GravityCompat.START).catchUi()
    }.catchUi()

}

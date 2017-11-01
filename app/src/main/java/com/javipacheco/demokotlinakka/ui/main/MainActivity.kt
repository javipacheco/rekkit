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
import com.javipacheco.demokotlinakka.actors.MainActor
import com.javipacheco.demokotlinakka.models.Commands.*
import com.javipacheco.demokotlinakka.services.ApiService
import com.javipacheco.demokotlinakka.services.MainUiService
import com.javipacheco.demokotlinakka.ui.main.NavigationItems.*
import akme.*
import com.javipacheco.demokotlinakka.actors.MainErrorActor
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity :
        AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener,
        MainUiService {

    val system = ActorSystem.create("AkmeSystem")
    val mainErrorActorRef = system.actorOf(MainErrorActor.props(this), "main-error-actor")
    val mainActorRef = system.actorOf(MainActor.props(mainErrorActorRef, ApiService(), this), "main-actor")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainActorRef.tell(InitCommand, system.deadLetters())
        mainActorRef.tell(FillMessageCommand, system.deadLetters())

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            mainActorRef.tell(CloseDrawerCommand, system.deadLetters())
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        mainActorRef.tell(NavigationCommand(Companion.toNavigationItem(item.itemId)), system.deadLetters())
        return true
    }

    // MainUiService

    override fun init(): Service<Unit> = catchUi {
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            mainActorRef.tell(ShowMessageCommand("Replace with your own action"), system.deadLetters())
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun writeMessage(msg: String): Service<Unit> =
            runOnUiThread { my_text.setText(msg) }.catchUi()

    override fun showMessage(msg: String): Service<Unit> =
            Snackbar.make(drawer_layout, msg, Snackbar.LENGTH_LONG).show().catchUi()

    override fun navigation(item: NavigationItems): Service<Unit> = when (item) {
        Camera -> showMessage("Camera")
        Gallery -> showMessage("Gallery")
        SlideShow -> showMessage("SlideShow")
        Manage -> showMessage("Manage")
        Share -> showMessage("Share")
        Send -> showMessage("Send")
        NotFound -> showMessage("Oh!! Noo!!")
    }

    override fun closeDrawer(): Service<Unit> =
            drawer_layout.closeDrawer(GravityCompat.START).catchUi()

}

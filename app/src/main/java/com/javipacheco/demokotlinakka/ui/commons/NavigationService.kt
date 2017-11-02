package com.javipacheco.demokotlinakka.ui.commons

import akme.*
import android.app.Activity
import android.content.Intent
import android.net.Uri

interface NavigationService {

    fun getActivity(): Activity

    fun goToWeb(url: String): Service<Unit> = ServiceRight {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        getActivity().startActivity(i)
    }

}
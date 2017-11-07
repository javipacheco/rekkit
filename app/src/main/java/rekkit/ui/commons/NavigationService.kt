package rekkit.ui.commons

import akme.*
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import rekkit.R
import rekkit.ui.news.NewsFragment

interface NavigationService {

    fun getActivity(): Activity

    fun goToWeb(url: String): Service<Unit> = catchUi {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        getActivity().startActivity(i)
    }

    fun loadNews(): Service<Int> = catchUi {
        val fragmentTransaction = (getActivity() as AppCompatActivity).supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_activity_fragment_container, NewsFragment())
        fragmentTransaction.commit()
    }

}
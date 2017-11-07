package rekkit.ui.news

import akme.Service
import rekkit.models.States
import kategory.ListKW

interface NewsUiService {

    fun showLoading(): Service<Unit>

    fun showNews(items: ListKW<States.NewsItemState>): Service<Unit>

    fun showMessage(item: NewsMessageItems): Service<Unit>

}
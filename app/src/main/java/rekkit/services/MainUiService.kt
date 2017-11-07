package rekkit.services

import rekkit.ui.main.NavigationItems
import akme.*
import rekkit.ui.main.MainMessageItems

interface MainUiService {

    fun showMessage(item: MainMessageItems): Service<Unit>

    fun navigation(item: NavigationItems): Service<Unit>

    fun closeDrawer(): Service<Unit>

}
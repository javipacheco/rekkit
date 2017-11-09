package rekkit.ui.main

import rekkit.ui.main.MainNavigationItems
import akme.*
import rekkit.ui.main.MainMessageItems

interface MainUiService {

    fun showMessage(item: MainMessageItems): Service<Unit>

    fun navigation(item: MainNavigationItems): Service<Unit>

    fun closeDrawer(): Service<Unit>

}
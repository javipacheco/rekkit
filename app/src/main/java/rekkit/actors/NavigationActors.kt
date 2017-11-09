package rekkit.actors

import rekkit.models.Commands
import rekkit.models.Commands.*
import rekkit.ui.commons.NavigationService
import kotlinx.coroutines.experimental.channels.*

class NavigationActors(val navigationService: NavigationService) {

    val mainActor = actor<Commands> {
        for (msg in channel) {
            when (msg) {
                is NavigationLoadNewsCommand -> navigationService.loadNews()
                is NavigationGoToUrlCommand -> navigationService.goToWeb(msg.url)
            }
        }
    }

}
package ke.don.home.model

import ke.don.domain.datastore.Theme
import ke.don.domain.gameplay.server.ServerId

sealed interface HomeIntentHandler {
    data object DiscoverGames : HomeIntentHandler
    data object Refresh : HomeIntentHandler
    data object ShowThemeModal : HomeIntentHandler
    data object ShowLogoutModal : HomeIntentHandler
    data object ShowProfileMenu : HomeIntentHandler
    data object ShowNetworkChooser : HomeIntentHandler
    data object ShowMenu : HomeIntentHandler
    data object NavigateToRules: HomeIntentHandler
    data object NavigateToNewGame: HomeIntentHandler
    data class NavigateToGame(val id : ServerId): HomeIntentHandler
    data object NavigateToEdit: HomeIntentHandler
    data class LogOut(val navigateToAuth: () -> Unit) : HomeIntentHandler
    data class SetTheme(val theme: Theme) : HomeIntentHandler
}
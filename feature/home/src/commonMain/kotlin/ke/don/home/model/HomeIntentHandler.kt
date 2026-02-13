/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.home.model

import ke.don.domain.datastore.Theme
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.gameplay.server.ServerId
import ke.don.domain.gameplay.server.VersionCompatibility

sealed interface HomeIntentHandler {
    data object DiscoverGames : HomeIntentHandler
    data object Refresh : HomeIntentHandler
    data object RefreshFromEmpty : HomeIntentHandler
    data object ShowThemeModal : HomeIntentHandler
    data object ShowLogoutModal : HomeIntentHandler
    data object ShowProfileMenu : HomeIntentHandler
    data object ShowNetworkChooser : HomeIntentHandler
    data object ShowMenu : HomeIntentHandler
    data object NavigateToRules : HomeIntentHandler
    data object NavigateToEdit : HomeIntentHandler
    data object NavigateToNewGame : HomeIntentHandler
    data class NavigateToGame(val id: ServerId) : HomeIntentHandler
    data class SelectGame(val game: GameIdentity? = null) : HomeIntentHandler
    data class LogOut(val navigateToAuth: () -> Unit) : HomeIntentHandler
    data class SetTheme(val theme: Theme) : HomeIntentHandler
    data class ShowVersionMismatch(val versionCompatibility: VersionCompatibility = VersionCompatibility.COMPATIBLE) : HomeIntentHandler
}

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
import ke.don.domain.gameplay.server.VersionCompatibility
import ke.don.domain.table.Profile
import ke.don.utils.result.ReadStatus

data class HomeState(
    val games: List<GameIdentity> = emptyList(),
    val profile: Profile = Profile(),
    val theme: Theme = Theme.System,

    val selectedGame: GameIdentity? = null,
    val readStatus: ReadStatus = ReadStatus.Loading,
    val showProfileMenu: Boolean = false,
    val showVersionMismatch: VersionCompatibility = VersionCompatibility.COMPATIBLE,
    val showMenu: Boolean = false,
    val showThemeModal: Boolean = false,
    val showLogoutModal: Boolean = false,
)

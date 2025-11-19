package ke.don.home.model

import ke.don.domain.datastore.Theme
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.table.Profile
import ke.don.utils.result.ReadStatus

data class HomeState(
    val games: List<GameIdentity> = emptyList(),
    val profile: Profile = Profile(),
    val theme: Theme = Theme.System,

    val readStatus: ReadStatus = ReadStatus.Loading,
    val showProfileMenu: Boolean = false,
    val showMenu: Boolean = false,
    val showThemeModal: Boolean = false,
    val showLogoutModal: Boolean = false
)

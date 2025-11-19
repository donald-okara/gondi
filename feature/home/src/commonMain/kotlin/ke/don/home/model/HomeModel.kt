package ke.don.home.model

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import ke.don.components.helpers.Matcha
import ke.don.domain.datastore.Theme
import ke.don.domain.gameplay.server.LanDiscovery
import ke.don.domain.gameplay.server.SERVICE_TYPE
import ke.don.domain.repo.ProfileRepository
import ke.don.domain.table.Profile
import ke.don.local.datastore.ProfileStore
import ke.don.local.datastore.ThemeRepository
import ke.don.local.misc.NetworkChooser
import ke.don.utils.result.ReadStatus
import ke.don.utils.result.toReadStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class HomeModel(
    private val profileStore: ProfileStore,
    private val profileRepository: ProfileRepository,
    private val lanDiscovery: LanDiscovery,
    private val networkChooser: NetworkChooser,
    private val themeRepository: ThemeRepository
): ScreenModel {
    private val _uiState = MutableStateFlow(HomeState())
    val uiState = _uiState.asStateFlow()

    init {
        screenModelScope.launch {
            combine(
                themeRepository.theme,
                profileStore.profileFlow
            ) { theme, profile ->
                _uiState.update { it.copy(theme = theme ?: Theme.System, profile = profile ?: Profile()) }
            }.collect {}
        }
    }

    fun onEvent(intent: HomeIntentHandler){
        when(intent){
            is HomeIntentHandler.Refresh -> refresh()
            is HomeIntentHandler.DiscoverGames -> reloadFromEmpty()
            is HomeIntentHandler.ShowThemeModal -> _uiState.update { state ->
                state.copy(
                    showThemeModal = !state.showThemeModal
                )
            }
            is HomeIntentHandler.ShowLogoutModal -> _uiState.update { state ->
                state.copy(
                    showLogoutModal = !state.showLogoutModal
                )
            }
            is HomeIntentHandler.ShowProfileMenu -> _uiState.update { state ->
                state.copy(
                    showProfileMenu = !state.showProfileMenu
                )
            }
            is HomeIntentHandler.ShowMenu -> _uiState.update { state ->
                state.copy(
                    showMenu = !state.showMenu
                )
            }
            is HomeIntentHandler.ShowNetworkChooser -> networkChooser.open()
            is HomeIntentHandler.SetTheme -> setTheme(intent.theme)
            is HomeIntentHandler.LogOut -> logOut(intent.navigateToAuth)
            else -> {}
        }
    }

    fun discoverGames() {
        try {
            lanDiscovery.start(
                serviceType = SERVICE_TYPE,
            ) { games ->
                if (!_uiState.value.games.contains(games)) {
                    _uiState.update { state ->
                        state.copy(
                            games = state.games + games
                        )
                    }
                }
            }

            _uiState.update { state ->
                state.copy(
                    readStatus = state.games.toReadStatus()
                )
            }
        } catch (e: Exception) {
            _uiState.update { state ->
                state.copy(
                    readStatus = ReadStatus.Error(e.message.toString())
                )
            }
            Matcha.showErrorToast(
                message = e.message,
                retryAction = { discoverGames() }
            )
        }
    }

    fun refresh(){
        _uiState.update { state ->
            state.copy(
                readStatus = ReadStatus.Refreshing
            )
        }
        discoverGames()
    }

    fun reloadFromEmpty(){
        screenModelScope.launch {
            _uiState.update { state ->
                state.copy(
                    readStatus = ReadStatus.Loading
                )
            }
            delay(1000)
            discoverGames()
        }
    }

    fun setTheme(theme: Theme){
        screenModelScope.launch {
            themeRepository.setTheme(theme)
        }
    }

    fun logOut(navigateToAuth: () -> Unit) {
        screenModelScope.launch {
            profileRepository.logOut()
            _uiState.update {
                it.copy(
                    showLogoutModal = false
                )
            }
            navigateToAuth()
        }
    }

}
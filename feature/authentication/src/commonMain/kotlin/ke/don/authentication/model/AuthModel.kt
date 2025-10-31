package ke.don.authentication.model

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import ke.don.components.helpers.Matcha
import ke.don.domain.repo.AuthClient
import ke.don.domain.result.ResultStatus
import ke.don.remote.api.SupabaseConfig.supabase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthModel(
    private val authClient: AuthClient
): ScreenModel {
    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState

    private val eventChannel = Channel<AuthEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        screenModelScope.launch {
            supabase.auth.sessionStatus.collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        val event = if (uiState.value.initiallyAuthenticated)
                            AuthEvent.SwitchMain
                        else
                            AuthEvent.SwitchProfile
                        eventChannel.send(event)
                    }
                    is SessionStatus.NotAuthenticated -> {
                        eventChannel.send(AuthEvent.SwitchSignIn)
                        updateState {
                            it.copy(
                                initiallyAuthenticated = false,
                            )
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    fun handleAction(action: AuthAction){
        when(action){
            is AuthAction.SignIn -> signInWithGoogle()
        }
    }

    fun updateState(transform: (AuthState) -> AuthState) {
        _uiState.update { state ->
            transform(state)
        }
    }

    fun signInWithGoogle() {
        screenModelScope.launch {
            updateState {
                it.copy(authStatus = ResultStatus.Loading)
            }
            try {
                authClient.signInWithGoogle()
            } catch (e: Exception) {
                updateState {
                    it.copy(authStatus = ResultStatus.Error(e.message ?: "Unknown error"))
                }
                Matcha.error(
                    title = "Sign in failed",
                    description = e.message ?: "Unknown error"
                )
            }
        }
    }

}
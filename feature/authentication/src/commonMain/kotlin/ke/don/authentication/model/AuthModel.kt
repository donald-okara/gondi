/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.authentication.model

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import ke.don.components.helpers.Matcha
import ke.don.domain.repo.AuthClient
import ke.don.domain.result.ResultStatus
import ke.don.domain.result.onFailure
import ke.don.domain.result.onSuccess
import ke.don.remote.api.SupabaseConfig.supabase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthModel(
    private val authClient: AuthClient,
) : ScreenModel {
    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState

    private val eventChannel = Channel<AuthEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        screenModelScope.launch {
            supabase.auth.sessionStatus.collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        val event = if (uiState.value.initiallyAuthenticated) {
                            AuthEvent.SwitchMain
                        } else {
                            AuthEvent.SwitchProfile
                        }
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

    fun handleAction(action: AuthAction) {
        when (action) {
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
            authClient.signInWithGoogle()
                .onSuccess { result ->
                    updateState {
                        it.copy(authStatus = ResultStatus.Success(data = result))
                    }
                }.onFailure { result ->
                    updateState {
                        it.copy(authStatus = ResultStatus.Error(message = result.message ?: "Unknown error"))
                    }
                }
        }
    }
}

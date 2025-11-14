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
import ke.don.domain.repo.ProfileRepository
import ke.don.domain.result.onSuccess
import ke.don.koffee.model.ToastDuration
import ke.don.remote.api.SupabaseConfig.supabase
import ke.don.utils.Logger
import ke.don.utils.result.onSuccess
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthModel(
    private val authClient: AuthClient,
    private val profileRepository: ProfileRepository,
) : ScreenModel {
    private val _uiState = MutableStateFlow(AuthState())
    val uiState: StateFlow<AuthState> = _uiState

    private val eventChannel = Channel<AuthEvent>()
    val events = eventChannel.receiveAsFlow()

    private val logger = Logger("AuthModel")

    init {
        screenModelScope.launch {
            supabase.auth.sessionStatus.collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> {
                        val event = if (uiState.value.initiallyAuthenticated) {
                            AuthEvent.SwitchMain
                        } else {
                            Matcha.success(
                                title = "Welcome to Gondi",
                            )
                            syncProfile()
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

    fun syncProfile() {
        screenModelScope.launch {
            profileRepository.getProfile().onSuccess { result ->
                _uiState.update {
                    it.copy(
                        profile = result,
                    )
                }
            }
        }
    }

    fun signInWithGoogle() {
        screenModelScope.launch {
            authClient.signInWithGoogle()
            Matcha.info(
                title = "Signing in with Google",
                description = "Check your browser",
                duration = ToastDuration.Long,
            )
        }
    }
}

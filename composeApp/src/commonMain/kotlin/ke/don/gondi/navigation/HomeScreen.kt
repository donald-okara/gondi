/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.gondi.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.profile.ProfileImageToken
import ke.don.components.scaffold.ScaffoldToken
import ke.don.domain.datastore.Theme
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.gameplay.server.LanDiscovery
import ke.don.domain.gameplay.server.LocalServer
import ke.don.domain.gameplay.server.SERVICE_TYPE
import ke.don.domain.repo.ProfileRepository
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import ke.don.domain.table.Profile
import ke.don.local.datastore.ProfileStore
import ke.don.local.datastore.ThemeRepository
import kotlinx.coroutines.launch
import org.koin.compose.getKoin
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class HomeScreen : Screen {
    @OptIn(
        ExperimentalMaterial3Api::class,
        ExperimentalMaterialApi::class,
        ExperimentalUuidApi::class,
    )
    @Composable
    override fun Content() {
        val koin = getKoin()
        val lanDiscovery = koin.get<LanDiscovery>()
        val server = koin.get<LocalServer>()
        var gamesList by remember {
            mutableStateOf<List<GameIdentity>>(emptyList())
        }

        val themeRepository = koin.get<ThemeRepository>()
        val theme by themeRepository.theme.collectAsState(
            initial = Theme.System,
        )
        val profileRepository = koin.get<ProfileRepository>()
        val profileStore = koin.get<ProfileStore>()

        /**
         * ALL THIS WILL BE GONE WHEN HOME IS IMPLEMENTED
         */
        val profile by profileStore.profileFlow.collectAsState(
            initial = null,
        )

        val navigator = LocalNavigator.currentOrThrow

        val coroutineScope = rememberCoroutineScope()

        ScaffoldToken(
            title = "Gondi",
            actions = {
                profile?.let {
                    ProfileImageToken(
                        // TODO: Temporary until profile is implemented
                        isHero = false,
                        profile = it,
                    )
                }
            },
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ButtonToken(
                    onClick = { // TODO, implement permission request
                        coroutineScope.launch {
                            lanDiscovery.start(
                                serviceType = SERVICE_TYPE,
                            ) { identity ->
                                if (!gamesList.contains(identity)) gamesList += identity
                            }
                        }
                    },
                    buttonType = ComponentType.Inverse,
                ) {
                    Text("Discover")
                }
                ButtonToken(
                    onClick = {
                        coroutineScope.launch {
                            server.stop()
                            server.start(
                                GameIdentity(
                                    id = Uuid.random().toString(),
                                    gameName = "Wamlambez",
                                    serviceType = SERVICE_TYPE,
                                    servicePort = 8080,
                                    moderatorName = "Goo goo",
                                    moderatorAvatarBackground = AvatarBackground.PURPLE_ORCHID,
                                    moderatorAvatar = Avatar.George,
                                ),
                            )
                        }
                    },
                    buttonType = ComponentType.Primary,
                ) {
                    Text("Advertise")
                }

                ButtonToken(
                    onClick = {
                        coroutineScope.launch {
                            profileRepository.logOut()
                            navigator.replaceAll(AuthScreen())
                        }
                    },
                    buttonType = ComponentType.Inverse,
                ) {
                    Text(
                        "Log out",
                    )
                }

                Theme.entries.forEach {
                    RadioButton(
                        selected = theme == it,
                        onClick = {
                            coroutineScope.launch {
                                themeRepository.setTheme(it)
                            }
                        },
                    )

                    Text(
                        text = it.name,
                    )
                }

                gamesList.forEach { item ->
                    ListItem(
                        icon = {
                            ProfileImageToken(
                                profile = Profile(
                                    avatar = item.moderatorAvatar,
                                    background = item.moderatorAvatarBackground,
                                    username = item.moderatorName,
                                ),
                                isHero = false,
                            )
                        },
                        text = {
                            Text(item.gameName)
                        },
                        secondaryText = {
                            Text(item.moderatorName)
                        },
                    )
                }
            }
        }
    }
}

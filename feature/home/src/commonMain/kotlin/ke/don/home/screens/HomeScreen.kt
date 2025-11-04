/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.home.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.empty_state.EmptyScreen
import ke.don.components.helpers.Matcha
import ke.don.components.profile.ProfileImageToken
import ke.don.components.scaffold.ScaffoldToken
import ke.don.domain.gameplay.server.LanDiscovery
import ke.don.domain.gameplay.server.LocalServer
import ke.don.domain.gameplay.server.SERVICE_TYPE
import ke.don.domain.table.Avatar
import ke.don.domain.table.Profile
import kotlinx.coroutines.launch
import org.koin.compose.getKoin

class HomeScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {

        val koin = getKoin()
        val lanDiscovery = koin.get<LanDiscovery>()
        val server = koin.get<LocalServer>()

        val coroutineScope = rememberCoroutineScope()

        ScaffoldToken(
            title = "Gondi",
            actions = {
                ProfileImageToken( // TODO: Temporary until profile is implemented
                    isHero = false,
                    profile = Profile(
                        name = "Donald Isoe",
                        avatar = Avatar.Leo,
                    ),
                )
            },
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                ButtonToken(
                    onClick = {
                        coroutineScope.launch {
                            lanDiscovery.start(
                                serviceType = SERVICE_TYPE
                            ) { host, port, name ->
                                // Update your list of available games
                                Matcha.info(title = "Discovery", description = "Discovered game: $name of host: $host")
                            }
                        }
                    },
                    buttonType = ComponentType.Inverse
                ){
                    Text("Discover")
                }
                ButtonToken(
                    onClick = {
                        coroutineScope.launch{
                            server.stop()
                            server.start(
                                serviceName = "Wamlambez",
                                serviceType = SERVICE_TYPE,
                                servicePort = 8080
                            )
                        }
                    },
                    buttonType = ComponentType.Primary
                ){
                    Text("Advertise")
                }

            }
        }
    }
}

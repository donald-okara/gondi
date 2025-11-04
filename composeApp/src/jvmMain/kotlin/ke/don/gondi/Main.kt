/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.gondi

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ke.don.domain.gameplay.server.LanAdvertiser
import ke.don.domain.gameplay.server.LocalServer
import ke.don.domain.gameplay.server.SERVICE_TYPE
import ke.don.remote.server.LanServerJvm
import ke.don.resources.Resources
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.getKoin
import java.awt.Dimension

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "Gondi",
        icon = painterResource(Resources.Images.LOGO),
    ) {
        window.minimumSize = Dimension(500, 0)

        val koin = getKoin()
        val server = koin.get<LocalServer>()
        val coroutineScope = rememberCoroutineScope()

        coroutineScope.launch {
            server.stop()
            server.start(
                serviceName = "Wamlambez",
                serviceType = SERVICE_TYPE,
                servicePort = 8080
            )
        }

        App()
    }
}

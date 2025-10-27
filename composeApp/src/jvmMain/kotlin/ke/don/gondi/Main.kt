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

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ke.don.resources.Resources
import org.jetbrains.compose.resources.painterResource

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Gondi",
        icon = painterResource(Resources.Images.LOGO),
    ) {
        App()
    }
}

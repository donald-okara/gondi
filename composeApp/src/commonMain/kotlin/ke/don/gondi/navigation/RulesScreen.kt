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

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ke.don.components.guide.RulesContent
import ke.don.components.scaffold.NavigationIcon
import ke.don.components.scaffold.ScaffoldToken

class RulesScreen : Screen {
    /**
     * Composes the Rules screen UI: a scaffold titled "Rules" with a back navigation icon and the screen body.
     *
     * The current navigator is retrieved and tapping the back icon calls `navigator.pop()`; the scaffold body
     * displays `RulesContent`.
     *
     * @throws IllegalStateException if no navigator is available in the current composition.
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        ScaffoldToken(
            title = "Rules",
            navigationIcon = NavigationIcon.Back {
                navigator.pop()
            },
        ) { RulesContent() }
    }
}

/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.profile.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ke.don.components.indicator.ProgressBar
import ke.don.components.scaffold.NavigationIcon
import ke.don.components.scaffold.ScaffoldToken
import ke.don.profile.model.EditProfileEvent
import ke.don.profile.model.EditProfileState
import ke.don.resources.color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBoardingContent(
    modifier: Modifier = Modifier,
    state: EditProfileState,
    handleEvent: (EditProfileEvent) -> Unit,
    navigateToMain: () -> Unit = {},
) {
    var step by remember {
        mutableStateOf(Steps.Rules)
    }

    val content: @Composable () -> Unit = remember(
        step,
        state,
        handleEvent,
        navigateToMain,
    ) {
        @Composable {
            AnimatedContent(targetState = step) { currentStep ->
                when (currentStep) {
                    Steps.Rules -> RulesContent(
                        next = {
                            step = Steps.Phases
                        },
                    )
                    Steps.Phases -> PhasesContent(
                        next = {
                            step = Steps.Profile
                        },
                        back = {
                            step = Steps.Rules
                        },
                    )
                    Steps.Profile -> EditContent(
                        state = state,
                        onEvent = handleEvent,
                        onSave = navigateToMain,
                        back = {
                            step = Steps.Phases
                        },
                    )
                }
            }
        }
    }

    ScaffoldToken(
        modifier = modifier,
        navigationIcon = NavigationIcon.Back(navigateToMain),
        scrollState = rememberScrollState(),
    ) {
        AnimatedContent(
            targetState = step.description,
            transitionSpec = { slideInVertically { it } togetherWith slideOutVertically { -it } },
        ) { description ->
            Text(
                description,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
        }

        Text(
            "Welcome, let's get you ready to play",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Step ${step.ordinal + 1} of ${Steps.entries.size}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        ProgressBar(
            progress = (step.ordinal + 1) / 3f,
            progressColor = if (step == Steps.Profile) state.editProfile.background.color() else MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(8.dp))

        content()
    }
}

private enum class Steps(
    val description: String,
) {
    Rules("First, The Rules"),
    Phases("Next, The Game Phases"),
    Profile("Finally, Your Profile"),
}

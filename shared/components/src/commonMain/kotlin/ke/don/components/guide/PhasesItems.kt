/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.guide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing

@Composable
fun GamePhases() {
    Column {
        // Title
        Text(
            text = "Game Phases",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
        )

        Spacer(modifier = Modifier.height(Theme.spacing.medium))

        Column(verticalArrangement = Arrangement.spacedBy(Theme.spacing.large)) {
            // --- Phase 1: Night Phase ---
            PhaseItem(
                number = "1",
                title = "Night Phase",
                description = "During the night, all players are \"sleeping.\" Special roles perform their actions in a specific order:",
                bullets = listOf(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("Gondi: ") }
                        append("Secretly agree on one player to eliminate.")
                    },
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("Doctor: ") }
                        append("Chooses one player to protect for the night.")
                    },
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("Detective: ") }
                        append("Chooses one player to investigate their alignment.")
                    },
                ),
            )

            // --- Phase 2: Day Phase ---
            PhaseItem(
                number = "2",
                title = "Day Phase",
                description = "The day begins with an announcement of who was eliminated during the night (if anyone). Then, the discussion phase starts:",
                bullets = listOf(
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("Town Hall: ") }
                        append("Players discuss who they suspect is a Gondi. This is where accusations are made and defenses are mounted.")
                    },
                    buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("Court: ") }
                        append(
                            "All living players vote to eliminate one suspect. A player is eliminated if they receive the majority of votes." +
                                "\nThe voted out player will be eliminated in the next sleep phase",
                        )
                    },
                ),
            )

            // --- Cycle Repeats ---
            Column {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Theme.spacing.medium)) {
                    Icon(
                        imageVector = Icons.Default.Repeat, // replace with your icon
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(Theme.spacing.large),
                    )
                    Text(
                        text = "Cycle Repeats",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }

                Spacer(modifier = Modifier.height(Theme.spacing.small))

                Text(
                    text = "The game alternates between Night and Day phases until one of the winning conditions is met.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = Theme.spacing.extraLarge),
                )
            }
        }
    }
}

@Composable
private fun PhaseItem(
    number: String,
    title: String,
    description: String,
    bullets: List<AnnotatedString>,
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(Theme.spacing.medium)) {
            Box(
                modifier = Modifier
                    .size(Theme.spacing.large)
                    .background(MaterialTheme.colorScheme.primary, shape = CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = number,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }

        Spacer(modifier = Modifier.height(Theme.spacing.small))

        Column(modifier = Modifier.padding(start = Theme.spacing.extraLarge)) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = Theme.spacing.extraSmall),
            )

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                bullets.forEach { bullet ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text("• ", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
                        Text(
                            text = bullet,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        )
                    }
                }
            }
        }
    }
}

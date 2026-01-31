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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material.icons.outlined.HowToVote
import androidx.compose.material.icons.outlined.ModeNight
import androidx.compose.material.icons.outlined.RecordVoiceOver
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ke.don.components.card.CardToken
import ke.don.components.icon.IconBox
import ke.don.components.steps.VerticalStep
import ke.don.components.steps.VerticalStepItem
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.resources.Resources
import org.jetbrains.compose.resources.stringResource

data class NightPhaseData(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun GamePhases(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Screen Title
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PhaseChip(
                label = stringResource(Resources.Strings.Guide.PHASE_1),
                color = Color(0xFF6366F1)
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(Resources.Strings.Guide.THE_SILENT_NIGHT),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(Resources.Strings.Guide.EVERYONE_CLOSE_YOUR_EYES),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic
            )
        }

        // Night Phase
        NightPhaseSection(
            steps = getNightSteps()
        )

        // Day Phase
        DayPhaseSection()
    }
}


@Composable
fun NightPhaseSection(
    steps: List<NightPhaseData>,
    modifier: Modifier = Modifier
) {
    CardToken(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            // Replaced LazyColumn with Column to avoid infinite height exception when nested in a scrollable parent
            Column {
                steps.forEachIndexed { index, data ->
                    VerticalStepItem(
                        step = VerticalStep(
                            index = index,
                            data = data,
                            icon = data.icon,
                            color = data.color,
                            label = data.title
                        )
                    ) { stepData ->
                        Column {
                            Text(
                                text = stepData.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = stepData.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun DayPhaseSection(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PhaseChip(
                label = stringResource(Resources.Strings.Guide.PHASE_2),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(Resources.Strings.Guide.THE_COURT_DAY),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(Resources.Strings.Guide.COURT_DAY_DESCRIPTION),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(Modifier.height(32.dp))

        val courtSteps = listOf(
            Triple(
                stringResource(Resources.Strings.Guide.ACCUSE_TITLE),
                stringResource(Resources.Strings.Guide.ACCUSE_DESCRIPTION),
                Icons.Outlined.RecordVoiceOver
            ),
            Triple(
                stringResource(Resources.Strings.Guide.SECOND_TITLE),
                stringResource(Resources.Strings.Guide.SECOND_DESCRIPTION),
                Icons.Outlined.Handshake
            ),
            Triple(
                stringResource(Resources.Strings.Guide.VOTE_TITLE),
                stringResource(Resources.Strings.Guide.VOTE_DESCRIPTION),
                Icons.Outlined.HowToVote
            )
        )

        // Replaced LazyColumn with Column to avoid infinite height exception when nested in a scrollable parent
        LazyVerticalGrid(
            columns = GridCells.Adaptive(300.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .heightIn(min = 200.dp, max = Theme.spacing.largeScreenSize)
                .fillMaxWidth()
        ) {
            items(courtSteps) { (title, desc, icon) ->
                CourtStepCard(title = title, description = desc, icon = icon)
            }
        }

        Spacer(Modifier.height(32.dp))

        ProTipCard()
    }
}

@Composable
fun CourtStepCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    icon: ImageVector
) {
    CardToken(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            IconBox(
                icon = icon,
                accentColor = MaterialTheme.colorScheme.primary,
                sizeInt = 64
            )

            Spacer(Modifier.width(16.dp))

            Text(text = title, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text(text = description, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun ProTipCard(
    modifier: Modifier = Modifier
) {
    CardToken(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.TipsAndUpdates, null, tint = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(Resources.Strings.Guide.PRO_TIP),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = stringResource(Resources.Strings.Guide.PRO_TIP_DESCRIPTION),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun PhaseChip(label: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color.copy(alpha = 0.2f), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    )
    {
        Text(
            text = label,
            color = color,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun getNightSteps() = listOf(
    NightPhaseData(
        title = stringResource(Resources.Strings.Guide.GONDI_AWAKENING),
        description = stringResource(Resources.Strings.Guide.GONDI_AWAKENING_DESCRIPTION),
        icon = Icons.Outlined.ModeNight,
        color = Color(0xFF6E70EF)
    ),
    NightPhaseData(
        title = stringResource(Resources.Strings.Guide.THE_DOCTORS_VIGIL),
        description = stringResource(Resources.Strings.Guide.THE_DOCTORS_VIGIL_DESCRIPTION),
        icon = Icons.Outlined.Shield,
        color = Color(0xFF00CED1)
    ),
    NightPhaseData(
        title = stringResource(Resources.Strings.Guide.THE_INVESTIGATION),
        description = stringResource(Resources.Strings.Guide.THE_INVESTIGATION_DESCRIPTION),
        icon = Icons.Outlined.Visibility,
        color = Color(0xFFFFBF00)
    )
)

/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.steps

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HorizontalStepper(
    steps: List<Step>,
    currentStep: Int,
    modifier: Modifier = Modifier,
    onStepClick: ((Int) -> Unit)? = null,
) {
    LazyRow(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        itemsIndexed(steps) { index, step ->
            StepItem(
                step = step,
                isActive = index == currentStep,
                onClick = onStepClick,
            )

            if (index != steps.lastIndex) {
                StepDivider(isActive = index == (currentStep - 1))
            }
        }
    }
}

@Composable
private fun StepItem(
    step: Step,
    isActive: Boolean,
    onClick: ((Int) -> Unit)?,
) {
    val borderColor by animateColorAsState(
        targetValue = if (isActive) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline
        },
    )

    val textColor by animateColorAsState(
        targetValue = if (isActive) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
    )

    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .clickable(enabled = onClick != null) {
                onClick?.invoke(step.index)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .border(
                    width = 2.dp,
                    color = borderColor,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = (step.index + 1).toString(),
                fontWeight = FontWeight.Bold,
                color = textColor,
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = step.label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun StepDivider(
    modifier: Modifier = Modifier,
    isActive: Boolean,
) {
    val color by animateColorAsState(
        targetValue = if (isActive) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outline
        },
    )
    Column(
        modifier = modifier
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .width(48.dp)
                    .height(2.dp)
                    .background(
                        color.copy(alpha = 0.4f),
                    ),
            )
        }

        Spacer(Modifier.height(6.dp))

        Text(
            text = "",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}

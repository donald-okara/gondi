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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ke.don.components.steps.HorizontalStepper
import ke.don.components.steps.Step
import ke.don.design.theme.spacing
import kotlinx.coroutines.launch

@Composable
fun RulesContent(
    modifier: Modifier = Modifier,
) {
    val steps = listOf(
        Step(index = 0, label = "Objective"),
        Step(index = 1, label = "Roles"),
        Step(index = 2, label = "Phases"),
        Step(index = 3, label = "Conduct"),
    )

    val activeStep = remember { mutableStateOf(0) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Sync stepper when user scrolls
    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                activeStep.value = index
            }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
    ) {
        HorizontalStepper(
            steps = steps.map {
                it.copy(isActive = it.index == activeStep.value)
            },
            currentStep = activeStep.value,
            onStepClick = { index ->
                activeStep.value = index
                scope.launch {
                    listState.animateScrollToItem(index)
                }
            }
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .width(MaterialTheme.spacing.largeScreenSize),
            contentPadding = PaddingValues(vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(
                MaterialTheme.spacing.large,
                Alignment.Top
            )
        ) {
            item { VictoryConditionsSection() } // index 0
            item { RolesList() }             // index 1
            item { GamePhases() }               // index 2
            item { CodeOfConductSection() }     // index 3
        }
    }
}

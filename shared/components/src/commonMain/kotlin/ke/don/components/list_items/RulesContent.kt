/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.list_items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ke.don.design.theme.spacing

@Composable
fun RulesContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(
                state = rememberScrollState(),
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.Top),
    ) {
        GameObjective()
        CodeOfConductSection()
        RolesList()
        GamePhases()
    }
}

/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.dialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ke.don.components.button.ButtonToken
import ke.don.components.button.CheckBoxToken
import ke.don.components.button.ComponentType

/**
 * Shows a confirmation dialog with an optional leading icon, title, message, and an optional checklist that must be completed before confirming.
 *
 * When a `checklist` is provided, the Confirm action is enabled only after every checklist item is checked. The dialog calls `onConfirm` when the Confirm button is pressed and `onDismiss` when the Cancel button or outside dismiss is triggered.
 *
 * @param modifier Modifier to apply to the dialog container.
 * @param icon Optional leading icon displayed in the header.
 * @param title Dialog title shown prominently at the top.
 * @param message Informational message displayed under the title.
 * @param checklist Optional list of checklist item labels; if non-null, each item is rendered with a checkbox and must be checked to enable Confirm.
 * @param dialogType Visual style/type applied to the Confirm button and checklist checkboxes.
 * @param onConfirm Callback invoked when the Confirm action is taken.
 * @param onDismiss Callback invoked when the dialog is dismissed or Cancel is pressed.
 */
@Composable
fun ConfirmationDialogToken(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String,
    message: String,
    checklist: List<String>? = null,
    dialogType: ComponentType,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    var checkedItems by remember { mutableStateOf(List(checklist?.size ?: 0) { false }) }
    val allChecked = remember(checkedItems) { checkedItems.all { it } }

    DialogToken(onDismissRequest = onDismiss, modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .width(500.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Header
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                    )
                }
            }

            Text(
                text = message,
                style = MaterialTheme.typography.labelMedium,
            )

            HorizontalDivider()

            // ✅ Optional Checklist
            checklist?.let {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    it.forEachIndexed { index, item ->
                        val isChecked = checkedItems[index]

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    checkedItems = checkedItems.toMutableList().apply {
                                        this[index] = !isChecked
                                    }
                                },
                        ) {
                            CheckBoxToken(
                                checkboxType = dialogType,
                                checked = isChecked,
                                onCheckedChange = { checked ->
                                    checkedItems = checkedItems.toMutableList().apply {
                                        this[index] = checked
                                    }
                                },
                            )
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }

            // ✅ Actions
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End),
                modifier = Modifier.fillMaxWidth(),
            ) {
                ButtonToken(
                    onClick = onDismiss,
                    buttonType = ComponentType.Neutral,
                ) {
                    Text("Cancel")
                }
                ButtonToken(
                    onClick = onConfirm,
                    buttonType = dialogType,
                    enabled = allChecked || checklist == null,
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}

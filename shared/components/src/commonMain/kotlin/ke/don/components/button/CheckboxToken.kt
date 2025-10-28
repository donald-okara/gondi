/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.button

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Renders a Material 3 Checkbox with configurable checked state, enablement, modifier, and colors.
 *
 * @param checked Current checked state of the Checkbox.
 * @param onCheckedChange Callback invoked with the new checked state when the user toggles the Checkbox; pass `null` to make the Checkbox read-only.
 * @param modifier Modifier to apply to the Checkbox.
 * @param enabled Whether the Checkbox is interactive.
 * @param checkboxType Determines the visual styling source used for default colors.
 * @param colors Colors to use for the Checkbox; by default derived from `checkboxType` for the given `enabled` state.
 */
@Composable
fun CheckBoxToken(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    checkboxType: ComponentType,
    colors: CheckboxColors = checkboxType.animatedCheckboxColors(enabled),
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
    )
}

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

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Step(
    val index: Int,
    val label: String,
    val isActive: Boolean = false,
)

data class VerticalStep<T>(
    val index: Int,
    val color: Color? = null,
    val icon: ImageVector? = null,
    val label: String,
    val data: T,
)

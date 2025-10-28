/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.resources

import androidx.compose.ui.unit.dp

/**
 * Gets the width of the screen in pixels.
 *
 * @return The width of the screen in pixels.
 */
expect fun getScreenWidth(): Float

fun Float.toWindowSizeClass() = getWindowSizeClass(this.dp)

fun isCompact() = getScreenWidth().toWindowSizeClass() == WindowSizeClass.Compact

/**
 * Returns the height of the screen in pixels.
 *
 * @return The screen height as a [Float].
 */
expect fun getScreenHeight(): Float

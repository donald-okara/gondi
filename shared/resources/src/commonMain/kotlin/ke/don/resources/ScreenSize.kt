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
/**
 * Obtain the current screen width for the running platform.
 *
 * Platform-specific implementations must provide the measured width.
 *
 * @return The screen width in pixels.
 */
expect fun getScreenWidth(): Float

/**
 * Convert a width in pixels to its corresponding WindowSizeClass.
 *
 * @receiver Width in pixels.
 * @return The `WindowSizeClass` that corresponds to the receiver width.
 */
fun Float.toWindowSizeClass() = getWindowSizeClass(this.dp)

/**
 * Determine whether the current screen width maps to the Compact window size class.
 *
 * @return `true` if the current screen width corresponds to `WindowSizeClass.Compact`, `false` otherwise.
 */
fun isCompact() = getScreenWidth().toWindowSizeClass() == WindowSizeClass.Compact

/**
 * Obtains the screen height in density-independent pixels (dp).
 *
 * @return The screen height in dp as a Float.
 */

expect fun getScreenHeight(): Float

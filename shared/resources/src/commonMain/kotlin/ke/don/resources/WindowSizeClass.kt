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

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Represents the different window size classes based on screen width.
 * This enum helps in creating responsive UIs by categorizing screen sizes.
 *
 * - [Compact]: Typically represents phone-sized screens.
 * - [Medium]: Typically represents tablet or foldable-sized screens.
 * - [Expanded]: Typically represents desktop or larger screen sizes.
 */
enum class WindowSizeClass { Compact, Medium, Expanded }

/**
 * Determines the [WindowSizeClass] based on the provided window width in Dp.
 *
 * This function categorizes the window size into one of three classes:
 * - [WindowSizeClass.Compact]: Typically for phones (width < 600.dp).
 * - [WindowSizeClass.Medium]: Typically for tablets and foldables (600.dp <= width < 840.dp).
 * - [WindowSizeClass.Expanded]: Typically for desktops (width >= 840.dp).
 *
 * @param windowDpWidth The width of the window in Dp.
 * @return The corresponding [WindowSizeClass].
 */
fun getWindowSizeClass(windowDpWidth: Dp): WindowSizeClass {
    return when {
        windowDpWidth < 600.dp -> WindowSizeClass.Compact // Phones
        windowDpWidth < 840.dp -> WindowSizeClass.Medium // Tablets, Foldables
        else -> WindowSizeClass.Expanded // Desktops
    }
}

/**
 * Provides the valid content width range for this window size class.
 *
 * @return The content width range in Dp for this size class:
 * - Compact: 0.dp..600.dp
 * - Medium: 600.dp..840.dp
 * - Expanded: 840.dp..2400.dp
 */
fun WindowSizeClass.contentWidthRange(): ClosedRange<Dp> = when (this) {
    WindowSizeClass.Compact -> 0.dp..600.dp // phones
    WindowSizeClass.Medium -> 600.dp..840.dp // tablets / half desktop
    WindowSizeClass.Expanded -> 840.dp..2400.dp // large desktop / ultrawide
}
/**
 * Provides the recommended icon size for this window size class.
 *
 * @return The icon size in Dp: 32.dp for Compact and Medium, 48.dp for Expanded.
 */
fun WindowSizeClass.iconSize(): Dp = when (this) {
    WindowSizeClass.Compact -> 32.dp // phones
    WindowSizeClass.Medium -> 32.dp // tablets / half desktop
    WindowSizeClass.Expanded -> 48.dp // desktop / large
}

/**
 * Selects the number of grid columns appropriate for the window size class.
 *
 * @return The number of grid columns: `1` for Compact, `2` for Medium, `3` for Expanded.
 */
fun WindowSizeClass.getGridColumns(): Int = when (this) {
    WindowSizeClass.Compact -> 1
    WindowSizeClass.Medium -> 2
    WindowSizeClass.Expanded -> 3
}
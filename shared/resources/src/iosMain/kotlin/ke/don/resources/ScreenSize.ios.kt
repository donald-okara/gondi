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

import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectGetHeight
import platform.CoreGraphics.CGRectGetWidth
import platform.UIKit.UIScreen

/**
 * Retrieve the main device screen's width in points.
 *
 * @return The main screen width in points as a `Float`.
 */
@OptIn(ExperimentalForeignApi::class)
actual fun getScreenWidth(): Float {
    val bounds = UIScreen.mainScreen.bounds
    return CGRectGetWidth(bounds).toFloat()
}

/**
 * Retrieves the main device screen height in points.
 *
 * @return The height of the main screen in points as a Float.
 */
@OptIn(ExperimentalForeignApi::class)
actual fun getScreenHeight(): Float {
    val bounds = UIScreen.mainScreen.bounds
    return CGRectGetHeight(bounds).toFloat()
}
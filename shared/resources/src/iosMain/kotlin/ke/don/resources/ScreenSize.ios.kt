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

@OptIn(ExperimentalForeignApi::class)
actual fun getScreenWidth(): Float {
    val bounds = UIScreen.mainScreen.bounds
    return CGRectGetWidth(bounds).toFloat()
}

@OptIn(ExperimentalForeignApi::class)
actual fun getScreenHeight(): Float {
    val bounds = UIScreen.mainScreen.bounds
    return CGRectGetHeight(bounds).toFloat()
}

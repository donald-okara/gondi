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

import java.awt.GraphicsEnvironment
import java.awt.Toolkit

/**
 * Get the screen width in density-independent pixels (dp).
 *
 * If the JVM is running in a headless environment, this returns 0f. If the system-reported
 * screen DPI is non-positive, a fallback DPI of 96 is used when converting pixels to dp.
 *
 * @return The screen width in density-independent pixels (dp); `0f` if running in a headless environment.
 */
actual fun getScreenWidth(): Float {
    if (GraphicsEnvironment.isHeadless()) return 0f
    val tk = Toolkit.getDefaultToolkit()
    val size = tk.screenSize
    val dpi = tk.screenResolution.takeIf { it > 0 } ?: 96
    // dp = px * 160 / dpi
    return size.width * 160f / dpi
}

/**
 * Get the screen height in density-independent pixels (dp).
 *
 * If the JVM is running in a headless environment this returns 0f. When the system-reported
 * screen DPI is not positive, a fallback DPI of 96 is used to convert pixels to dp.
 *
 * @return The screen height in dp; `0f` if running in a headless environment.
 */
actual fun getScreenHeight(): Float {
    if (GraphicsEnvironment.isHeadless()) return 0f
    val tk = Toolkit.getDefaultToolkit()
    val size = tk.screenSize
    val dpi = tk.screenResolution.takeIf { it > 0 } ?: 96
    return size.height * 160f / dpi
}

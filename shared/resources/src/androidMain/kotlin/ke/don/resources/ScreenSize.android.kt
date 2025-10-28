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

import android.content.res.Configuration
import android.content.res.Resources

/**
 * Obtains the device screen width in density-independent pixels (dp).
 *
 * Attempts to use the system configuration's `screenWidthDp`; if that value is undefined, falls back to calculating width from display metrics (`widthPixels / density`).
 *
 * @return The screen width in dp as a `Float`.
 */
actual fun getScreenWidth(): Float {
    val configuration = android.content.res.Resources.getSystem().configuration
    val sw = configuration.screenWidthDp
    // dp; fallback if undefined
    return if (sw != Configuration.SCREEN_WIDTH_DP_UNDEFINED) {
        sw.toFloat()
    } else {
        android.content.res.Resources.getSystem().displayMetrics.run { widthPixels / density }
    }
}

/**
 * Obtain the device screen height in density-independent pixels (dp).
 *
 * When the system configuration provides `screenHeightDp`, that value is returned.
 * Otherwise the height is computed from display metrics as `heightPixels / density`.
 *
 * @return The screen height in density-independent pixels (dp) as a Float.
 */
actual fun getScreenHeight(): Float {
    val configuration = android.content.res.Resources.getSystem().configuration
    val sh = configuration.screenHeightDp
    return if (sh != Configuration.SCREEN_HEIGHT_DP_UNDEFINED) {
        sh.toFloat()
    } else {
        Resources.getSystem().displayMetrics.run { heightPixels / density }
    }
}

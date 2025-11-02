/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.utils

import android.util.Log

actual class Logger actual constructor(private val tag: String) {

    actual fun error(message: String, throwable: Throwable?) {
        Log.e(tag, "❌ $message", throwable)
    }

    actual fun warn(message: String, throwable: Throwable?) {
        Log.w(tag, "⚠️ $message", throwable)
    }

    actual fun info(message: String, throwable: Throwable?) {
        Log.i(tag, "ℹ️ $message", throwable)
    }

    actual fun debug(message: String, throwable: Throwable?) {
        Log.d(tag, "🐛 $message", throwable)
    }
}

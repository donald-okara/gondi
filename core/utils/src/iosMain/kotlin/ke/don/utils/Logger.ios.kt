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

actual class Logger actual constructor(private val tag: String) {
    actual fun error(message: String, throwable: Throwable?) {
        println("${colorTag(tag, "31")} ❌ $message\n${throwable?.stackTraceToString()}")
    }

    actual fun warn(message: String, throwable: Throwable?) {
        println("${colorTag(tag, "33")} ⚠️ $message\n${throwable?.stackTraceToString()}")
    }

    actual fun info(message: String, throwable: Throwable?) {
        println("${colorTag(tag, "36")} ℹ️ $message\n${throwable?.stackTraceToString()}")
    }

    actual fun debug(message: String, throwable: Throwable?) {
        println("${colorTag(tag, "32")} 🐛 $message\n${throwable?.stackTraceToString()}")
    }
}

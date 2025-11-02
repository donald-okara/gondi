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

fun String.getInitials(): String {
    val words = this.trim().split("\\s+".toRegex())
    return when {
        words.isEmpty() -> ""
        words.size == 1 -> words[0].take(2).uppercase()
        else -> (
            words[0].firstOrNull()?.toString().orEmpty() +
                words[1].firstOrNull()?.toString().orEmpty()
            ).uppercase()
    }
}

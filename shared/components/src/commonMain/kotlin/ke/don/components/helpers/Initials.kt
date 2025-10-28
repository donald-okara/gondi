/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.helpers

/**
 * Generates initials from the provided full name.
 *
 * For an empty or all-whitespace input, returns an empty string. For a single word,
 * returns the first two characters of that word in uppercase. For two or more words,
 * returns the first letter of the first two words concatenated and uppercased.
 *
 * @param name The input name from which to derive initials.
 * @return A string of initials according to the rules above.
 */
fun getInitials(name: String): String {
    val words = name.trim().split("\\s+".toRegex())
    return when {
        words.isEmpty() -> ""
        words.size == 1 -> words[0].take(2).uppercase()
        else -> (
            words[0].firstOrNull()?.toString().orEmpty() +
                words[1].firstOrNull()?.toString().orEmpty()
            ).uppercase()
    }
}

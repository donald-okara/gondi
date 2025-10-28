/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
@file:Suppress("SwallowedException")

package ke.don.components.helpers

private val isJUnitPresent: Boolean by lazy {
    runCatching {
        Class.forName("org.junit.Assert")
        true
    }.getOrDefault(false)
}

/**
 * Reports whether execution is running inside a JUnit-based unit test environment.
 *
 * @return `true` if JUnit is present on the runtime classpath (indicating a JUnit-based unit test), `false` otherwise.
 */
actual fun isRunningUnitTest(): Boolean = isJUnitPresent

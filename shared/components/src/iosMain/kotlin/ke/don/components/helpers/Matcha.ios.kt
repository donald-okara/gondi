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

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSClassFromString
import platform.Foundation.NSProcessInfo

/**
 * Detects whether the current process appears to be running unit tests on Apple platforms by checking for XCTest presence or the `XCTestConfigurationFilePath` environment variable.
 *
 * @return `true` if XCTest is present or `XCTestConfigurationFilePath` is set in the environment, `false` otherwise.
 */
@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual fun isRunningUnitTest(): Boolean {
    val hasXCTest = NSClassFromString("XCTest") != null
    val env = NSProcessInfo.processInfo.environment
    val hasEnv = (env["XCTestConfigurationFilePath"] != null)
    return hasXCTest || hasEnv
}

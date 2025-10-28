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
import platform.Foundation.NSClassFromString
import platform.Foundation.NSProcessInfo
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual fun isRunningUnitTest(): Boolean {
    val hasXCTest = NSClassFromString("XCTest") != null
    val env = NSProcessInfo.processInfo.environment
    val hasEnv = (env["XCTestConfigurationFilePath"] != null)
    return hasXCTest || hasEnv
}

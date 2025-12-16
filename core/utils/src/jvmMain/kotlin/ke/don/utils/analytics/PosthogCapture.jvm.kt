/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.utils.analytics

import ke.don.utils.Logger
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

actual object PosthogCapture {
    @OptIn(markerClass = [ExperimentalTime::class])
    actual fun capture(
        event: String,
        distinctId: String?,
        properties: Map<String, Any>?,
        userProperties: Map<String, Any>?,
        userPropertiesSetOnce: Map<String, Any>?,
        groups: Map<String, String>?,
        timestamp: Instant?,
    ) {
        val logger = Logger("PosthogCaptureJvm")
        logger.info("Posthog sdk is not implemented for JVM yet")
    }
}

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

import com.posthog.PostHog
import ke.don.utils.Logger
import java.util.Date
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

actual object PosthogCapture {
    @OptIn(ExperimentalTime::class)
    actual fun capture(
        event: String,
        distinctId: String?,
        properties: Map<String, Any>?,
        userProperties: Map<String, Any>?,
        userPropertiesSetOnce: Map<String, Any>?,
        groups: Map<String, String>?,
        timestamp: Instant?,
    ) {
        val logger = Logger("PosthogCapture")
        try {
            PostHog.capture(
                event = event,
                distinctId = distinctId,
                properties = properties,
                userProperties = userProperties,
                userPropertiesSetOnce = userPropertiesSetOnce,
                groups = groups,
                timestamp = (timestamp ?: Clock.System.now()).toDate(),
            )
        } catch (e: Exception) {
            logger.error(
                "Failed to capture event: $event",
                e,
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
fun Instant.toDate(): Date =
    Date(this.toEpochMilliseconds())

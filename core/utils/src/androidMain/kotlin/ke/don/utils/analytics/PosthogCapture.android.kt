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
        try{
            PostHog.capture(
                event = event,
                distinctId = distinctId,
                properties = properties,
                userProperties = userProperties,
                userPropertiesSetOnce = userPropertiesSetOnce,
                groups = groups,
                timestamp = (timestamp ?: Clock.System.now()).toDate()
            )
        }catch (e: Exception) {
            logger.error(
                "Failed to capture event: $event",
                e
            )
        }
    }
}

@OptIn(ExperimentalTime::class)
fun Instant.toDate(): Date =
    Date(this.toEpochMilliseconds())

package ke.don.utils.analytics

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
    }
}
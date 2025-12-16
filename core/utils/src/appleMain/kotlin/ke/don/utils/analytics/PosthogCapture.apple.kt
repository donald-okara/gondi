package ke.don.utils.analytics

actual object PosthogCapture {
    @OptIn(markerClass = [kotlin.time.ExperimentalTime::class])
    actual fun capture(
        event: String,
        distinctId: String?,
        properties: Map<String, Any>?,
        userProperties: Map<String, Any>?,
        userPropertiesSetOnce: Map<String, Any>?,
        groups: Map<String, String>?,
        timestamp: kotlin.time.Instant?,
    ) {
    }
}
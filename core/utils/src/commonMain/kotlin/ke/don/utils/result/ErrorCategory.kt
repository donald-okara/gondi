package ke.don.utils.result

enum class ErrorCategory {
    REQUEST_TIMEOUT,
    UNAUTHORIZED,
    CONFLICT,
    TOO_MANY_REQUESTS,
    NO_INTERNET,
    PAYLOAD_TOO_LARGE,
    SERVER,
    SERVER_ERROR,
    SERIALIZATION,
    DECODING,
    UNKNOWN,
}
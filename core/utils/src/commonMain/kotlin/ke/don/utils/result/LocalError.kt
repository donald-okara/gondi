package ke.don.utils.result

data class LocalError(
    val message: String,
    val cause: String,
): Error

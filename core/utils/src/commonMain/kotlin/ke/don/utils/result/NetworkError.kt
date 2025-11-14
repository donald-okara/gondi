package ke.don.utils.result


data class NetworkError(
    val category: ErrorCategory = ErrorCategory.UNKNOWN,
    val message: String? = null,
    val code: Int? = null,
    val debugMessage: String? = null, // optional, only for logs
) : Error
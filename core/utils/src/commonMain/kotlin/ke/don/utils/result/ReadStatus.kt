package ke.don.utils.result

// For long-lived screen data
sealed interface ReadStatus {
    data object Loading : ReadStatus
    data object Empty : ReadStatus
    data object Success : ReadStatus // no payload
    data class Error(val message: String) : ReadStatus
}

// For one-shot results
sealed interface ResultStatus<out T> {
    data object Idle : ResultStatus<Nothing>
    data object Loading : ResultStatus<Nothing>
    data class Success<T>(val data: T) : ResultStatus<T>
    data class Error(val message: String) : ResultStatus<Nothing>
}

// --- For read status (untied)
fun <T> List<T>?.toReadStatus(): ReadStatus =
    if (this.isNullOrEmpty()) ReadStatus.Empty else ReadStatus.Success

inline fun ReadStatus.onLoading(action: () -> Unit): ReadStatus {
    if (this is ReadStatus.Loading) action()
    return this
}

inline fun ReadStatus.onSuccess(action: () -> Unit): ReadStatus {
    if (this is ReadStatus.Success) action()
    return this
}

inline fun ReadStatus.onError(action: (String) -> Unit): ReadStatus {
    if (this is ReadStatus.Error) action(this.message)
    return this
}

inline fun <T> ResultStatus<T>.onLoading(action: () -> Unit): ResultStatus<T> {
    if (this is ResultStatus.Loading) action()
    return this
}

inline fun <T> ResultStatus<T>.onSuccess(action: (T) -> Unit): ResultStatus<T> {
    if (this is ResultStatus.Success) action(this.data)
    return this
}

inline fun <T> ResultStatus<T>.onError(action: (String) -> Unit): ResultStatus<T> {
    if (this is ResultStatus.Error) action(this.message)
    return this
}

val ReadStatus.isLoading: Boolean
    get() = this is ReadStatus.Loading

val ReadStatus.isSuccess: Boolean
    get() = this is ReadStatus.Success

val ReadStatus.isError: Boolean
    get() = this is ReadStatus.Error

val ReadStatus.isEmpty: Boolean
    get() = this is ReadStatus.Empty

val ReadStatus.message: String?
    get() = (this as? ReadStatus.Error)?.message

val <T> ResultStatus<T>.isIdle: Boolean
    get() = this is ResultStatus.Idle

val <T> ResultStatus<T>.isLoading: Boolean
    get() = this is ResultStatus.Loading

val <T> ResultStatus<T>.isSuccess: Boolean
    get() = this is ResultStatus.Success

val <T> ResultStatus<T>.isError: Boolean
    get() = this is ResultStatus.Error

val <T> ResultStatus<T>.data: T?
    get() = (this as? ResultStatus.Success)?.data

val <T> ResultStatus<T>.message: String?
    get() = (this as? ResultStatus.Error)?.message
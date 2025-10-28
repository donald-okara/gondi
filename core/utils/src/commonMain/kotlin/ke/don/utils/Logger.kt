package ke.don.utils

expect class Logger(tag: String) {
    fun error(
        message: String,
        throwable: Throwable? = null
    )
    fun warn(
        message: String,
        throwable: Throwable? = null
    )
    fun info(
        message: String,
        throwable: Throwable? = null
    )
    fun debug(
        message: String,
        throwable: Throwable? = null
    )

}

internal fun colorTag(tag: String, colorCode: String): String =
    "\u001B[${colorCode}m$tag\u001B[0m"
package ke.don.utils

actual class Logger actual constructor(private val tag: String) {

    private fun colorTag(tag: String, colorCode: String): String =
        "\u001B[${colorCode}m[$tag]\u001B[0m"

    actual fun error(message: String, throwable: Throwable?) {
        println("${colorTag(tag, "31")} ❌ $message\n${throwable?.stackTraceToString()}")
    }

    actual fun warn(message: String, throwable: Throwable?) {
        println("${colorTag(tag, "33")} ⚠️ $message\n${throwable?.stackTraceToString()}")
    }

    actual fun info(message: String, throwable: Throwable?) {
        println("${colorTag(tag, "36")} ℹ️ $message\n${throwable?.stackTraceToString()}")
    }

    actual fun debug(message: String, throwable: Throwable?) {
        println("${colorTag(tag, "32")} 🐛 $message\n${throwable?.stackTraceToString()}")
    }
}

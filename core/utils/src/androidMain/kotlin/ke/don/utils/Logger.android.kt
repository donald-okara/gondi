package ke.don.utils

import android.util.Log

actual class Logger actual constructor(private val tag: String) {

    actual fun error(message: String, throwable: Throwable?) {
        Log.e(colorTag(tag, "31"), "❌ $message", throwable)
    }

    actual fun warn(message: String, throwable: Throwable?) {
        Log.w(colorTag(tag, "33"), "⚠️ $message", throwable)
    }

    actual fun info(message: String, throwable: Throwable?) {
        Log.i(colorTag(tag, "36"), "ℹ️ $message", throwable)
    }

    actual fun debug(message: String, throwable: Throwable?) {
        Log.d(colorTag(tag, "32"), "🐛 $message", throwable)
    }
}

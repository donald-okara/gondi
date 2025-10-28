package ke.don.components.helpers

actual fun isRunningUnitTest(): Boolean {
    return try {
        Class.forName("org.junit.Assert") != null
    } catch (e: ClassNotFoundException) {
        false
    }
}
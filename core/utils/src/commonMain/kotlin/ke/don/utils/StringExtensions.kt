package ke.don.utils

fun String.getInitials(): String {
    val words = this.trim().split("\\s+".toRegex())
    return when {
        words.isEmpty() -> ""
        words.size == 1 -> words[0].take(2).uppercase()
        else -> (
                words[0].firstOrNull()?.toString().orEmpty() +
                        words[1].firstOrNull()?.toString().orEmpty()
                ).uppercase()
    }
}
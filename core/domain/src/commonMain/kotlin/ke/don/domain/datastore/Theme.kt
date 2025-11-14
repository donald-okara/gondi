package ke.don.domain.datastore

enum class Theme {
    System,
    Light,
    Dark;

    companion object{
        fun fromString(value: String?): Theme {
            return value?.let { valueOf(it) } ?: System
        }
    }
}
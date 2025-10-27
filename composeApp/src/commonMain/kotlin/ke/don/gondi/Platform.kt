package ke.don.gondi

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
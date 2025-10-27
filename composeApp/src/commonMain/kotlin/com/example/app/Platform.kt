package com.example.app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
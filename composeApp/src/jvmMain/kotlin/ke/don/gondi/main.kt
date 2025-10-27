package com.example.app

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import ke.don.gondi.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KleanBoy",
    ) {
        App()
    }
}
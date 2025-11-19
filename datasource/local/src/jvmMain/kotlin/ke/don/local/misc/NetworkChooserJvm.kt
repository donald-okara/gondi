/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.local.misc

import java.io.IOException

class NetworkChooserJvm : NetworkChooser {
    override fun open() {
        try {
            val os = System.getProperty("os.name").lowercase()

            // Linux executes differently → run & return early
            if (os.contains("linux")) {
                tryLinuxNetworkCommands()
                return
            }

            // Non-Linux choose proper command
            val command: Array<String> = when {
                os.contains("mac") || os.contains("darwin") ->
                    arrayOf("open", "/System/Library/PreferencePanes/Network.prefPane")

                os.contains("win") ->
                    arrayOf("control.exe", "ncpa.cpl")

                else -> {
                    println("Network settings not supported on: $os")
                    return
                }
            }

            Runtime.getRuntime().exec(command)
        } catch (e: IOException) {
            println("Failed to open network settings: ${e.message}")
        }
    }

    private fun tryLinuxNetworkCommands() {
        val commands = listOf(
            arrayOf("nm-connection-editor"),
            arrayOf("gnome-control-center", "network"),
            arrayOf("systemsettings5", "kcm_networkmanagement"),
            arrayOf("nmtui"),
        )

        for (command in commands) {
            try {
                Runtime.getRuntime().exec(command)
                return // success
            } catch (_: IOException) {
                // try next command
            }
        }

        println("Failed to open network settings: No compatible Linux command found")
    }
}

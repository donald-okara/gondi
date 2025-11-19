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

class NetworkChooserJvm() : NetworkChooser {
    override fun open() {
        // Attempt to launch the standard Wi-Fi connection dialog via NetworkManager
        val commands = listOf(
            "nm-connection-editor", // GUI editor (GNOME)
            "nmcli device wifi list", // CLI fallback
        )

        for (cmd in commands) {
            try {
                Runtime.getRuntime().exec(cmd)
                return
            } catch (e: Exception) {
                // Try next
            }
        }

        println("No Wi-Fi picker available on this system")
    }
}

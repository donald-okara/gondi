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

            // Always return Array<String>
            val command: Array<String> = when {
                os.contains("linux") ->
                    arrayOf("nm-connection-editor")

                os.contains("mac") || os.contains("darwin") ->
                    arrayOf("open", "/System/Library/PreferencePanes/Network.prefPane")

                os.contains("win") ->
                    arrayOf("control.exe", "ncpa.cpl")

                else -> {
                    println("Network settings not supported on $os")
                    return
                }
            }

            Runtime.getRuntime().exec(command)
        } catch (e: IOException) {
            println("Failed to open network settings: ${e.message}")
        }
    }
}

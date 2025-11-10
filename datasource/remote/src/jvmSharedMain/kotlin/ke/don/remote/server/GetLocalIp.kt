/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.remote.server

import java.net.Inet4Address
import java.net.NetworkInterface

fun getLocalIpAddress(): String {
    val interfaces = NetworkInterface.getNetworkInterfaces()?.toList() ?: return "127.0.0.1"
    for (iface in interfaces) {
        if (!iface.isUp || iface.isLoopback || iface.name.contains("docker", true) || iface.name.contains("vm", true)) continue

        val addresses = iface.inetAddresses.toList()
        for (address in addresses) {
            if (address is Inet4Address && !address.isLoopbackAddress) {
                return address.hostAddress
            }
        }
    }
    return "127.0.0.1"
}

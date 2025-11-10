package ke.don.remote.server

import java.net.Inet4Address
import java.net.NetworkInterface

fun getLocalIpAddress(): String {
    val interfaces = NetworkInterface.getNetworkInterfaces().toList()
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

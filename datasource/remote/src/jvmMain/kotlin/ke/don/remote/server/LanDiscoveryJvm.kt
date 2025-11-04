package ke.don.remote.server

// jvmMain
import ke.don.domain.gameplay.server.LanDiscovery
import ke.don.utils.Logger
import java.net.InetAddress
import java.net.NetworkInterface
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceListener

class LanDiscoveryJvm : LanDiscovery {
    private val logger = Logger("LanDiscovery_JVM")
    private var jmdns: JmDNS? = null

    override fun start(
        serviceType: String,
        onDiscovered: (host: String, port: Int, name: String) -> Unit
    ) {
        // Bind JmDNS to the active network interface
        val localIp = getLocalIpAddress()
        logger.info("Binding JmDNS to $localIp")
        jmdns = JmDNS.create(InetAddress.getByName(localIp))

        val normalizedType = if (serviceType.endsWith(".")) serviceType else "$serviceType."
        jmdns?.addServiceListener(normalizedType, object : ServiceListener {
            override fun serviceAdded(event: ServiceEvent) {
                logger.info("🟡 Service added: ${event.name}")
                // resolve asynchronously
                jmdns?.getServiceInfo(normalizedType, event.name, true)?.let { info ->
                    val address = info.inet4Addresses.firstOrNull()?.hostAddress ?: return
                    logger.info("✅ Resolved: ${event.name} at $address:${info.port}")
                    onDiscovered(address, info.port, event.name)
                }
            }

            override fun serviceRemoved(event: ServiceEvent) {
                logger.error("❌ Game removed: ${event.name}")
            }

            override fun serviceResolved(event: ServiceEvent) {
                logger.info("🔹 Service resolved: ${event.name}")
            }
        })

        logger.info("🔍 Started LAN discovery for $normalizedType")
    }

    override fun stop() {
        jmdns?.close()
        logger.info("🛑 LAN Discovery stopped")
    }

    private fun getLocalIpAddress(): String {
        val interfaces = NetworkInterface.getNetworkInterfaces().toList()
        for (iface in interfaces) {
            if (!iface.isUp || iface.isLoopback || iface.name.contains("docker", true) || iface.name.contains("vm", true)) continue

            val addresses = iface.inetAddresses.toList()
            for (address in addresses) {
                if (address is java.net.Inet4Address && !address.isLoopbackAddress) {
                    return address.hostAddress
                }
            }
        }
        return "127.0.0.1"
    }

}

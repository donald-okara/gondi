package ke.don.remote.server

// jvmMain
import ke.don.domain.gameplay.server.LanDiscovery
import ke.don.utils.Logger
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceListener

class LanDiscoveryJvm(
) : LanDiscovery {
    private val logger = Logger("LanDiscovery_JVM")
    private var jmdns: JmDNS? = null

    override fun start(serviceType: String, onDiscovered: (host: String, port: Int, name: String) -> Unit) {
        jmdns = JmDNS.create()
        jmdns?.addServiceListener(serviceType, object : ServiceListener {
            override fun serviceAdded(event: ServiceEvent) {
                // Attempt to resolve
                val info = jmdns?.getServiceInfo(serviceType, event.name)
                if (info != null) {
                    val address = info.inet4Addresses.firstOrNull()?.hostAddress ?: return
                    onDiscovered(address, info.port, event.name)
                    logger.info("🎮 Discovered game: ${event.name} at $address:${info.port}")
                }
            }

            override fun serviceRemoved(event: ServiceEvent) {
                logger.error("❌ Game removed: ${event.name}")
            }

            override fun serviceResolved(event: ServiceEvent) {
                logger.info("✅ Game resolved: ${event.name}")
            }
        })
        println("🔍 Started LAN discovery for $serviceType")
    }

    override fun stop() {
        jmdns?.close()
        logger.info("🛑 LAN Discovery stopped")
    }
}

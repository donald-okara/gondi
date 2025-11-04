package ke.don.remote.server

// jvmMain
import ke.don.domain.gameplay.server.LanAdvertiser
import ke.don.utils.Logger
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo
import java.net.InetAddress

class LanAdvertiserJvm : LanAdvertiser {
    private val logger = Logger("LanAdvertiser_JVM")
    private var jmdns: JmDNS? = null
    private var serviceInfo: ServiceInfo? = null

    override fun start(serviceHost: String, serviceName: String, serviceType: String, servicePort: Int) {
        val ip = InetAddress.getByName(serviceHost)

        jmdns = JmDNS.create(ip)

        serviceInfo = ServiceInfo.create(
            serviceType,
            serviceName,
            servicePort,
            "path=/chat"
        )

        jmdns?.registerService(serviceInfo)
        logger.info("📡 Advertised $serviceName on ${ip.hostAddress}:$servicePort")
    }

    override fun stop() {
        try {
            serviceInfo?.let { jmdns?.unregisterService(it) }
        } catch (e: Exception) {
            logger.error("⚠️ Failed to unregister service: ${e.message}")
        } finally {
            jmdns?.close()
            logger.error("🛑 LAN Advertiser stopped")
        }
    }
}

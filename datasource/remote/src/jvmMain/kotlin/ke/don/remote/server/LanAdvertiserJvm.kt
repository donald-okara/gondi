package ke.don.remote.server

// jvmMain
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.gameplay.server.LanAdvertiser
import ke.don.utils.Logger
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo
import java.net.InetAddress

class LanAdvertiserJvm : LanAdvertiser {
    private val logger = Logger("LanAdvertiser_JVM")
    private var jmdns: JmDNS? = null
    private var serviceInfo: ServiceInfo? = null

    override fun start(gameIdentity: GameIdentity) {
        val ip = InetAddress.getByName(gameIdentity.serviceHost)
        jmdns = JmDNS.create(ip)

        val fullType = if (gameIdentity.serviceType.endsWith("."))
            gameIdentity.serviceType
        else
            "${gameIdentity.serviceType}."

        // ✅ Create TXT record metadata
        val txtRecord = mapOf(
            "id" to gameIdentity.id,
            "mod_name" to gameIdentity.moderatorName,
            "mod_avatar" to gameIdentity.moderatorAvatar?.name,
            "background" to gameIdentity.moderatorAvatarBackground.name,
            "gameName" to gameIdentity.gameName
        )

        // ✅ Create advertised service
        serviceInfo = ServiceInfo.create(
            fullType,
            gameIdentity.gameName,
            gameIdentity.servicePort,
            0, // weight
            0, // priority
            txtRecord
        )

        try {
            jmdns?.registerService(serviceInfo)
            logger.info("📡 Advertised '${gameIdentity.gameName}' by ${gameIdentity.moderatorName} on ${ip.hostAddress}:${gameIdentity.servicePort}")
        } catch (e: Exception) {
            logger.error("💥 Failed to advertise service: ${e.message}")
        }
    }

    override fun stop() {
        try {
            serviceInfo?.let { jmdns?.unregisterService(it) }
        } catch (e: Exception) {
            logger.error("⚠️ Failed to unregister service: ${e.message}")
        } finally {
            jmdns?.close()
            logger.info("🛑 LAN Advertiser stopped")
        }
    }
}

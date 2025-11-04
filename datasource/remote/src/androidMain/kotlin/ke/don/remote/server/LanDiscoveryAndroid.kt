package ke.don.remote.server

// jvmMain
import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.gameplay.server.LanDiscovery
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import ke.don.utils.Logger

class LanDiscoveryAndroid(
    private val context: Context,
) : LanDiscovery {

    private val nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
    private var discoveryListener: NsdManager.DiscoveryListener? = null
    private val logger = Logger("LanDiscovery_Android")

    override fun start(
        serviceType: String,
        onDiscovered: (GameIdentity) -> Unit
    ) {
        discoveryListener = object : NsdManager.DiscoveryListener {

            override fun onDiscoveryStarted(type: String?) {
                logger.info("🔍 Discovery started for type: $type")
            }

            override fun onServiceFound(service: NsdServiceInfo) {
                logger.info("🟡 Service found: ${service.serviceName} (${service.serviceType})")

                nsdManager.resolveService(service, object : NsdManager.ResolveListener {
                    override fun onServiceResolved(resolved: NsdServiceInfo) {
                        val host = resolved.host?.hostAddress ?: return logger.error("⚠️ Host was null for ${resolved.serviceName}")
                        val port = resolved.port
                        val type = resolved.serviceType
                        val name = resolved.serviceName

                        val txt = resolved.attributes.mapValues { it.value?.decodeToString() ?: "" }

                        val id = txt["id"] ?: "Unknown"
                        val moderatorName = txt["mod_name"] ?: "Unknown"
                        val moderatorAvatarName = txt["mod_avatar"]
                        val moderatorBackgroundName = txt["background"]

                        val moderatorAvatar = runCatching { Avatar.fromValue(moderatorAvatarName) }.getOrDefault(Avatar.entries.first())
                        val moderatorBackground = runCatching { AvatarBackground.fromValue(moderatorBackgroundName) ?: AvatarBackground.entries.first() }.getOrDefault(AvatarBackground.entries.first())

                        val identity = GameIdentity(
                            id = id,
                            serviceHost = host,
                            serviceType = type,
                            servicePort = port,
                            gameName = name,
                            moderatorName = moderatorName,
                            moderatorAvatarBackground = moderatorBackground,
                            moderatorAvatar = moderatorAvatar,
                        )

                        logger.info("✅ Resolved: $identity")
                        onDiscovered(identity)
                    }

                    override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                        logger.error("❌ Resolve failed for ${serviceInfo.serviceName} (error $errorCode)")
                    }
                })
            }

            override fun onServiceLost(service: NsdServiceInfo) {
                logger.error("❌ Lost service: ${service.serviceName}")
            }

            override fun onDiscoveryStopped(type: String?) {
                logger.info("🛑 Discovery stopped for type: $type")
            }

            override fun onStartDiscoveryFailed(type: String?, errorCode: Int) = handleDiscoveryFailure(type, errorCode)
            override fun onStopDiscoveryFailed(type: String?, errorCode: Int) = handleDiscoveryFailure(type, errorCode)

            private fun handleDiscoveryFailure(type: String?, errorCode: Int) {
                logger.error("🚫 Discovery failure for $type (error $errorCode)")
                try {
                    nsdManager.stopServiceDiscovery(this)
                } catch (e: IllegalArgumentException) {
                    logger.error("⚠️ Tried to stop unregistered listener: ${e.message}")
                }
            }
        }

        val fullType = if (serviceType.endsWith(".")) serviceType else "$serviceType."
        nsdManager.discoverServices(fullType, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
        logger.info("📡 Starting LAN service discovery for $fullType")
    }

    override fun stop() {
        discoveryListener?.let {
            nsdManager.stopServiceDiscovery(it)
            logger.info("🛑 Stopped LAN service discovery")
        }
    }
}



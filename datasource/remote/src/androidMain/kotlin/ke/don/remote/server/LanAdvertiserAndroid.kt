package ke.don.remote.server

// jvmMain
import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import ke.don.domain.gameplay.server.LanAdvertiser
import ke.don.utils.Logger

class LanAdvertiserAndroid(
    private val context: Context,
) : LanAdvertiser {
    private val nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
    private var registrationListener: NsdManager.RegistrationListener? = null
    private val logger = Logger("LanAdvertiser_Android")

    override fun start(serviceHost: String, serviceName: String, serviceType: String, servicePort: Int) {
        val serviceInfo = NsdServiceInfo().apply {
            this.serviceName = serviceName
            this.serviceType = if (serviceType.endsWith(".")) serviceType else "$serviceType."
            this.port = servicePort
        }

        registrationListener = object : NsdManager.RegistrationListener {
            override fun onServiceRegistered(info: NsdServiceInfo) {
                logger.info("✅ Service registered: ${info.serviceName} on $serviceHost:$servicePort")
            }

            override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                logger.error("❌ Registration failed: $errorCode")
            }

            override fun onServiceUnregistered(info: NsdServiceInfo) {
                logger.error("🛑 Service unregistered: ${info.serviceName}")
            }

            override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                logger.error("⚠️ Unregistration failed: $errorCode")
            }
        }

        try {
            logger.info("📡 Registering service on $serviceHost:$servicePort (${serviceType})")
            nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)
        } catch (e: Exception) {
            logger.error("💥 Failed to start advertiser: ${e.message}")
        }
    }

    override fun stop() {
        try {
            registrationListener?.let {
                nsdManager.unregisterService(it)
                logger.info("🛑 LAN advertiser stopped")
            }
        } catch (e: Exception) {
            logger.error("⚠️ Error stopping advertiser: ${e.message}")
        }
    }
}


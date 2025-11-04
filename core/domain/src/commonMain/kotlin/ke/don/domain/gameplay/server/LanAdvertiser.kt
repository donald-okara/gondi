package ke.don.domain.gameplay.server

// commonMain
interface LanAdvertiser {
    fun start(serviceHost: String, serviceName: String, serviceType: String, servicePort: Int)
    fun stop()
}

interface LanDiscovery {
    fun start(serviceType: String, onDiscovered: (host: String, port: Int, name: String) -> Unit)
    fun stop()
}

const val SERVICE_TYPE = "_gondi._tcp."

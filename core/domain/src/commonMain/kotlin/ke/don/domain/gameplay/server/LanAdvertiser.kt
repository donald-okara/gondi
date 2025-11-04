package ke.don.domain.gameplay.server

// commonMain
interface LanAdvertiser {
    fun start(gameIdentity: GameIdentity)
    fun stop()
}

interface LanDiscovery {
    fun start(serviceType: String, onDiscovered: (GameIdentity) -> Unit)
    fun stop()
}

const val SERVICE_TYPE = "_gondi._tcp."

package ke.don.domain.gameplay.server

interface LocalServer {
    suspend fun start(serviceName: String, serviceType: String, servicePort: Int)
    suspend fun stop()
}
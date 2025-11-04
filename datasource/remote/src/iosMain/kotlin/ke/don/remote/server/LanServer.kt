package ke.don.remote.server

import ke.don.domain.gameplay.server.LocalServer

class LanServer(
    private val port: Int
) : LocalServer {
    override suspend fun start(serviceName: String, serviceType: String, servicePort: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun stop() {
        TODO("Not yet implemented")
    }
}


package ke.don.remote.server

import ke.don.domain.gameplay.server.LocalServer

import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.gameplay.server.LanAdvertiser
import ke.don.utils.Logger
import kotlinx.coroutines.channels.consumeEach
import kotlin.time.Duration.Companion.seconds

class LanServerJvm(
    private val advertiser: LanAdvertiser
) : LocalServer {

    private val logger = Logger("LanServer_JVM")
    private var server: EmbeddedServer<*, *>? = null

    override suspend fun start(identity: GameIdentity) {
        val host = getLocalIpAddress()

        // Start the Ktor WebSocket server
        server = embeddedServer(CIO, port = identity.servicePort, host = host) {
            install(ContentNegotiation) { json() }

            install(WebSockets) {
                pingPeriod = 15.seconds
                timeout = 30.seconds
                maxFrameSize = Long.MAX_VALUE
                masking = false
            }

            routing {
                get("/") {
                    call.respond(mapOf("status" to "WebSocket server running on LAN"))
                }

                webSocket("/chat") {
                    send("Connected to LAN WebSocket server 🚀")

                    incoming.consumeEach { frame ->
                        if (frame is Frame.Text) {
                            val text = frame.readText()
                            logger.info("Received: $text")
                            send("Echo: $text")
                        }
                    }
                }
            }
        }.start(wait = false)

        // 👇 Create and start advertiser
        advertiser.start(
            gameIdentity = GameIdentity(
                id = identity.id,
                gameName = identity.gameName,
                moderatorName = identity.moderatorName,
                moderatorAvatar = identity.moderatorAvatar,
                servicePort = identity.servicePort,
                serviceHost = host,
                serviceType = identity.serviceType,
                moderatorAvatarBackground = identity.moderatorAvatarBackground
            )
        )
        logger.debug("✅ LAN WebSocket server started and advertised on ws://$host:${identity.servicePort}/chat")
    }

    override suspend fun stop() {
        advertiser.stop()
        server?.stop()
    }
}
fun getLocalIpAddress(): String {
    val interfaces = java.net.NetworkInterface.getNetworkInterfaces().toList()
    for (iface in interfaces) {
        if (!iface.isUp || iface.isLoopback || iface.name.contains("docker", true) || iface.name.contains("vm", true)) continue

        val addresses = iface.inetAddresses.toList()
        for (address in addresses) {
            if (address is java.net.Inet4Address && !address.isLoopbackAddress) {
                return address.hostAddress
            }
        }
    }
    return "0.0.0.0"
}


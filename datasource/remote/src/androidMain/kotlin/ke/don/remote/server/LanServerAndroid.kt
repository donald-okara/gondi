package ke.don.remote.server
//
//import ke.don.domain.gameplay.server.LocalServer
//import io.ktor.server.cio.*
//import io.ktor.server.engine.*
//import io.ktor.server.application.*
//import io.ktor.server.response.*
//import io.ktor.server.routing.*
//import io.ktor.server.plugins.contentnegotiation.*
//import io.ktor.serialization.kotlinx.json.*
//import io.ktor.server.websocket.*
//import io.ktor.websocket.*
//import io.ktor.websocket.send
//import ke.don.domain.gameplay.GameEngine
//import ke.don.domain.gameplay.ModeratorCommand
//import ke.don.domain.gameplay.ModeratorEngine
//import ke.don.domain.gameplay.server.GameIdentity
//import ke.don.domain.gameplay.server.LanAdvertiser
//import ke.don.domain.gameplay.server.ServerMessage
//import ke.don.domain.gameplay.server.ServerUpdate
//import ke.don.domain.state.GameState
//import ke.don.domain.state.Player
//import ke.don.local.db.LocalDatabase
//import ke.don.remote.gameplay.validateIntent
//import ke.don.utils.Logger
//import kotlinx.coroutines.channels.consumeEach
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.flow.firstOrNull
//import kotlinx.serialization.json.Json
//import kotlin.time.Duration.Companion.seconds
//
//
//class LanServerAndroid(
//    private val advertiser: LanAdvertiser,
//    private val database: LocalDatabase,
//    private val gameEngine: GameEngine,
//    private val moderatorEngine: ModeratorEngine,
//) : LocalServer {
//
//    private val logger = Logger("LanServer_JVM")
//    private var server: EmbeddedServer<*, *>? = null
//
//    // 👇 Keep track of all connected WebSocket sessions
//
//    // 👇 Provide a Json instance for encoding messages
//    private val json = Json { prettyPrint = true }
//
//    private val sessions = mutableSetOf<DefaultWebSocketServerSession>()
//
//    override suspend fun start(identity: GameIdentity) {
//        val host = getLocalIpAddress()
//
//        server = embeddedServer(CIO, port = identity.servicePort, host = host) {
//            install(ContentNegotiation) { json() }
//
//            install(WebSockets) {
//                pingPeriod = 15.seconds
//                timeout = 30.seconds
//                maxFrameSize = Long.MAX_VALUE
//                masking = false
//            }
//
//            routing {
//                webSocket("/game") {
//                    // Register this new client session
//                    sessions += this
//                    send(Json.encodeToString(ServerUpdate.Announcement("Connected to Gondi server ✅")))
//
//                    try {
//                        incoming.consumeEach { frame ->
//                            if (frame is Frame.Text) {
//                                val text = frame.readText()
//                                handleMessage(text)
//                            }
//                        }
//                    } finally {
//                        // Remove session on disconnect
//                        sessions -= this
//                    }
//                }
//            }
//        }.start(wait = false)
//
//        advertiser.start(
//            gameIdentity = identity.copy(serviceHost = host)
//        )
//
//        logger.debug("✅ LAN WebSocket server started and advertised on ws://$host:${identity.servicePort}/chat")
//    }
//
//    override suspend fun stop() {
//        advertiser.stop()
//        server?.stop()
//    }
//
//    override suspend fun handleModeratorCommand(command: ModeratorCommand) {
//        moderatorEngine.handle(command)
//
//        // After moderator actions, broadcast updates
//        val newState = database.getCurrentGameState()
//        val players = database.getAllPlayersSnapshot()
//        broadcast(ServerUpdate.GameStateSnapshot(newState))
//        broadcast(ServerUpdate.PlayersSnapshot(players))
//
//        logger.debug("Moderator command executed ✅ ($command)")
//    }
//
//
//    suspend fun DefaultWebSocketServerSession.sendJson(message: ServerMessage) {
//        send(json.encodeToString(message))
//    }
//
//    private suspend fun DefaultWebSocketServerSession.handleMessage(
//        json: String,
//    ) {
//        try {
//            val message = this@LanServerAndroid.json.decodeFromString<ServerMessage>(json)
//            when (message) {
//                is ServerMessage.PlayerIntentMsg -> {
//                    val currentPhase = database.getCurrentGameState()?.phase ?: return
//
//                    if (!validateIntent(db = database, intent = message.intent, currentPhase = currentPhase)) {
//                        send(Json.encodeToString(ServerUpdate.Error("Invalid intent")))
//                        return
//                    }
//
//
//                    // 1️⃣ Apply the intent
//                    gameEngine.reduce(message.intent)
//
//                    // 2️⃣ Broadcast updated state
//                    val newState = database.getCurrentGameState()
//                    val players = database.getAllPlayersSnapshot()
//                    broadcast(ServerUpdate.GameStateSnapshot(newState))
//                    broadcast(ServerUpdate.PlayersSnapshot(players))
//
//                    send(Json.encodeToString(ServerUpdate.Announcement("Intent processed ✅")))
//                }
//
//                is ServerMessage.ModeratorCommandMsg -> {
//                    handleModeratorCommand(message.command)
//                }
//
//                is ServerMessage.GetGameState -> {
//                    val newState = database.getCurrentGameState()
//                    val players = database.getAllPlayersSnapshot()
//
//                    send(Json.encodeToString(ServerUpdate.GameStateSnapshot(newState)))
//                    send(Json.encodeToString(ServerUpdate.PlayersSnapshot(players)))
//                }
//            }
//        } catch (e: Exception) {
//            send(Json.encodeToString(ServerUpdate.Error("Error: ${e.message}")))
//        }
//    }
//
//    private suspend fun broadcast(update: ServerUpdate) {
//        val text = Json.encodeToString(update)
//        sessions.forEach { it.send(Frame.Text(text)) }
//    }
//}
//suspend fun LocalDatabase.getCurrentGameState(): GameState? =
//    getAllGameState().firstOrNull()?.first()
//
//suspend fun LocalDatabase.getAllPlayersSnapshot(): List<Player> =
//    getAllPlayers().first()
//
//
//fun getLocalIpAddress(): String {
//    val interfaces = java.net.NetworkInterface.getNetworkInterfaces().toList()
//    for (iface in interfaces) {
//        if (!iface.isUp || iface.isLoopback || iface.name.contains("docker", true) || iface.name.contains("vm", true)) continue
//
//        val addresses = iface.inetAddresses.toList()
//        for (address in addresses) {
//            if (address is java.net.Inet4Address && !address.isLoopbackAddress) {
//                return address.hostAddress
//            }
//        }
//    }
//    return "0.0.0.0"
//}
//

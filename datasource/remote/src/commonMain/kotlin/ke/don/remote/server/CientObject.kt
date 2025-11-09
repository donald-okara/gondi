package ke.don.remote.server

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket

object CientObject {
    val client = HttpClient {
        install(WebSockets)
    }

    suspend fun getWebsocket(host: String, port: String) = client.webSocket("ws://$host:$port/game")
}
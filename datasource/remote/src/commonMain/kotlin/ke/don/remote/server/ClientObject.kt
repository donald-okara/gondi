package ke.don.remote.server

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets

object ClientObject {
    val client = HttpClient {
        install(WebSockets)
    }
}
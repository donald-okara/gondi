/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.remote.server

import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.WebSockets

object ClientObject {
    val client = HttpClient {
        install(WebSockets)
    }
}

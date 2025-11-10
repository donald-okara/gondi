/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
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

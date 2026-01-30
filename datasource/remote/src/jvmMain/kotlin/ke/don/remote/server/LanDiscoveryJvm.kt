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

// jvmMain
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.gameplay.server.LanDiscovery
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import ke.don.utils.Logger
import java.net.InetAddress
import javax.jmdns.JmDNS
import javax.jmdns.ServiceEvent
import javax.jmdns.ServiceInfo
import javax.jmdns.ServiceListener

class LanDiscoveryJvm : LanDiscovery {
    private val logger = Logger("LanDiscovery_JVM")
    private var jmdns: JmDNS? = null

    override fun start(
        serviceType: String,
        onDiscovered: (GameIdentity) -> Unit,
    ) {
        val localIp = getLocalIpAddress()
        logger.info("Binding JmDNS to $localIp")
        jmdns = JmDNS.create(InetAddress.getByName(localIp))

        val normalizedType = if (serviceType.endsWith(".")) serviceType else "$serviceType."

        jmdns?.addServiceListener(
            normalizedType,
            object : ServiceListener {
                override fun serviceAdded(event: ServiceEvent) {
                    logger.info("🟡 Service added: ${event.name}")
                    // Do nothing else here
                }

                override fun serviceRemoved(event: ServiceEvent) {
                    logger.error("❌ Game removed: ${event.name}")
                }

                override fun serviceResolved(event: ServiceEvent) {
                    val info = event.info
                    logger.info("🟡 Service resolved: ${event.name} with info: $info")

                    val host = info.inet4Addresses
                        .firstOrNull()
                        ?.hostAddress
                        ?: run {
                            logger.error("❌ No IPv4 address for ${event.name}")
                            return
                        }

                    val identity = parseGameIdentity(info)?.copy(
                        serviceHost = host
                    ) ?: return

                    logger.info(
                        "✅ Resolved ${identity.gameName} hosted by ${identity.moderatorName} at $host:${identity.servicePort}"
                    )

                    onDiscovered(identity)
                }

            },
        )

        logger.info("🔍 Started LAN discovery for $normalizedType")
    }

    override fun stop() {
        try {
            jmdns?.close()
            logger.info("🛑 LAN Discovery stopped")
        } catch (e: Exception) {
            logger.error("⚠️ Error stopping discovery: ${e.message}")
        }
    }

    private fun parseGameIdentity(info: ServiceInfo): GameIdentity? {
        val port = info.port
        val type = info.type
        val txt = info.propertyNames.toList().associateWith { info.getPropertyString(it) }
        logger.info("Parsing GameIdentity from TXT: $txt")

        val id = txt["id"] ?: "Unknown"
        val gameName = txt["gameName"] ?: info.name ?: "Unknown"
        val moderatorName = txt["mod_name"] ?: "Unknown"
        val version = txt["version"] ?: "Unknown"
        val moderatorAvatar = txt["mod_avatar"]?.let { Avatar.fromValue(it) } ?: Avatar.George // fallback
        val moderatorBackground = txt["background"]?.let { AvatarBackground.fromValue(it) } ?: AvatarBackground.entries.first() // fallback

        return GameIdentity(
            id = id,
            serviceHost = "",
            serviceType = type,
            servicePort = port,
            gameName = gameName,
            moderatorName = moderatorName,
            moderatorAvatar = moderatorAvatar,
            version = version,
            moderatorAvatarBackground = moderatorBackground,
        )
    }
}

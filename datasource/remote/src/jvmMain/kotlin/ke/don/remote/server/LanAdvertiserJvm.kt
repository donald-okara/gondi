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
import ke.don.domain.gameplay.server.LanAdvertiser
import ke.don.utils.Logger
import java.net.InetAddress
import javax.jmdns.JmDNS
import javax.jmdns.ServiceInfo


class LanAdvertiserJvm : LanAdvertiser {
    private val logger = Logger("LanAdvertiser_JVM")
    private var jmdns: JmDNS? = null
    private var serviceInfo: ServiceInfo? = null

    override fun start(gameIdentity: GameIdentity) {
        stop()

        try {
            val ip = InetAddress.getByName(gameIdentity.serviceHost)
            val fullType = gameIdentity.serviceType.ensureDot()

            val txtRecord = mapOf(
                "id" to gameIdentity.id,
                "mod_name" to gameIdentity.moderatorName,
                "mod_avatar" to gameIdentity.moderatorAvatar?.name,
                "background" to gameIdentity.moderatorAvatarBackground.name,
                "gameName" to gameIdentity.gameName,
                "ttl" to "5", // ⬅ forces fast expiry
            )

            jmdns = JmDNS.create(ip)
            serviceInfo = ServiceInfo.create(
                fullType,
                gameIdentity.gameName,
                gameIdentity.servicePort,
                0,
                0,
                txtRecord,
            )

            jmdns!!.registerService(serviceInfo)
            logger.info("📡 Advertised '${gameIdentity.gameName}'")
        } catch (e: Exception) {
            logger.error("💥 JVM advertise error: $e")
        }
    }

    override fun stop() {
        try {
            jmdns?.apply {
                serviceInfo?.let {
                    unregisterService(it)
                }

                Thread.sleep(750) // required by mdns spec for goodbye propagation
                close()
            }
        } catch (_: Exception) {}
        finally {
            jmdns = null
            serviceInfo = null
            logger.info("🛑 JVM advertiser stopped cleanly")
        }
    }
}

private fun String.ensureDot() = if (endsWith(".")) this else "$this."

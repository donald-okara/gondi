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
import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import ke.don.domain.gameplay.server.GameIdentity
import ke.don.domain.gameplay.server.LanAdvertiser
import ke.don.utils.Logger

class LanAdvertiserAndroid(
    private val context: Context,
) : LanAdvertiser {
    private val nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
    private var registrationListener: NsdManager.RegistrationListener? = null
    private val logger = Logger("LanAdvertiser_Android")

    override fun start(gameIdentity: GameIdentity) {
        val serviceInfo = NsdServiceInfo().apply {
            serviceName = gameIdentity.gameName
            serviceType = if (gameIdentity.serviceType.endsWith(".")) gameIdentity.serviceType else "${gameIdentity.serviceType}."
            port = gameIdentity.servicePort

            // Embed custom data as TXT records
            setAttribute("id", gameIdentity.id)
            setAttribute("mod_name", gameIdentity.moderatorName)
            setAttribute("background", gameIdentity.moderatorAvatarBackground.name)
            gameIdentity.moderatorAvatar?.let { setAttribute("mod_avatar", it.name) }
        }

        registrationListener = object : NsdManager.RegistrationListener {
            override fun onServiceRegistered(info: NsdServiceInfo) {
                logger.info("✅ Service registered: ${info.serviceName} on ${gameIdentity.serviceHost}:${gameIdentity.servicePort}")
            }

            override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                logger.error("❌ Registration failed: $errorCode")
            }

            override fun onServiceUnregistered(info: NsdServiceInfo) {
                logger.error("🛑 Service unregistered: ${info.serviceName}")
            }

            override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                logger.error("⚠️ Unregistration failed: $errorCode")
            }
        }

        try {
            logger.info("📡 Registering service on ${gameIdentity.serviceHost}:${gameIdentity.servicePort} (${gameIdentity.serviceType})")
            nsdManager.registerService(serviceInfo, NsdManager.PROTOCOL_DNS_SD, registrationListener)
        } catch (e: Exception) {
            logger.error("💥 Failed to start advertiser: ${e.message}")
        }
    }

    override fun stop() {
        try {
            registrationListener?.let {
                nsdManager.unregisterService(it)
                logger.info("🛑 LAN advertiser stopped")
            }
        } catch (e: Exception) {
            logger.error("⚠️ Error stopping advertiser: ${e.message}")
        }
    }
}

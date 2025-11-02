/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.remote.repo

import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.ktor.http.HttpStatusCode
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import ke.don.domain.repo.AuthClient
import ke.don.domain.result.NetworkError
import ke.don.domain.result.Result
import ke.don.remote.api.SupabaseConfig.supabase
import ke.don.utils.Logger
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.io.BufferedReader
import java.net.URI

class AuthClientJvm : AuthClient {
    val logger = Logger("AuthClient")

    override suspend fun signInWithGoogle(): Result<Unit, NetworkError> {
        return try {
            startAuthServer()
            logger.info("🟢 Starting Google sign-in flow...")
            val result = supabase.auth.signUpWith(Google, redirectUrl = "http://localhost:3000/auth-callback")

            if (result != null) {
                // Depending on the lib version this may be .url or just a string
                val authUrl = result.toString()
                logger.info("🌍 Auth URL = $authUrl")
                Desktop.getDesktop().browse(URI(authUrl))
                Result.success(Unit)
            } else {
                logger.warn("❌ signUpWith(Google) returned null")
                Result.error(NetworkError(
                    message = "Failed to sign in with Google",
                    debugMessage = "signUpWith(Google) returned null",
                ))
            }
        } catch (e: Exception) {
            logger.error("❌ Error during sign-in: $e", e)
            e.printStackTrace()
            Result.error(NetworkError(
                message = "Failed to sign in with Google",
                debugMessage = e.message,
                code = 500,
            ))
        }
    }
}

fun startAuthServer() {
    val logger = Logger("AuthServer")

    // 🧹 Try to free port 3000 first
    freePort(3000, logger)

    logger.debug("🟡 Starting local auth server on http://localhost:3000 ...")
    embeddedServer(Netty, port = 3000) {
        routing {
            get("/auth-callback") {
                logger.info("📩 Callback hit! Query params: ${call.parameters.entries()}")
                val code = call.parameters["code"]
                if (code != null) {
                    logger.info("✅ Received auth code: $code")
                    call.application.launch {
                        try {
                            logger.info("🔄 Exchanging code for session...")
                            supabase.auth.exchangeCodeForSession(code)
                            logger.info("🎉 Session exchange successful!")
                        } catch (e: Exception) {
                            logger.error("🔥 Failed to exchange code: $e")
                            e.printStackTrace()
                        }
                    }
                    call.respondText("Login successful! 🎊 You can close this window.")
                } else {
                    logger.error("⚠️ Missing 'code' in callback query")
                    call.respondText("Missing auth code", status = HttpStatusCode.BadRequest)
                }
            }
        }
    }.start(wait = false)
}

fun freePort(port: Int, logger: Logger) {
    try {
        val os = System.getProperty("os.name").lowercase()
        val command = when {
            "win" in os -> arrayOf("cmd.exe", "/c", "netstat -ano | findstr :$port")
            else -> arrayOf("bash", "-c", "lsof -ti:$port")
        }

        val process = ProcessBuilder(*command).start()
        process.waitFor(2, java.util.concurrent.TimeUnit.SECONDS)

        val reader = BufferedReader(process.inputStream.reader())
        val lines = reader.readLines()

        for (line in lines) {
            val pid = when {
                "win" in os -> line.trim().split(Regex("\\s+")).lastOrNull()
                else -> line.trim()
            }
            if (!pid.isNullOrBlank()) {
                logger.error("⚙️ Killing process on port $port (PID $pid)")
                val killCmd = when {
                    "win" in os -> arrayOf("cmd.exe", "/c", "taskkill /PID $pid /F")
                    else -> arrayOf("bash", "-c", "kill -9 $pid")
                }
                val killProcess = ProcessBuilder(*killCmd).start()
                killProcess.waitFor(2, java.util.concurrent.TimeUnit.SECONDS)
            }
        }

        if (lines.isEmpty()) {
            logger.info("✅ Port $port is free")
        }
    } catch (e: Exception) {
        logger.error("⚠️ Failed to free port $port: ${e.message}", e)
    }
}

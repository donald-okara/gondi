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
import ke.don.domain.repo.AuthClient
import ke.don.domain.result.NetworkError
import ke.don.domain.result.Result
import ke.don.remote.api.SupabaseConfig.supabase
import ke.don.utils.Logger
import java.net.URI


class AuthClientAndroid : AuthClient {
    val logger = Logger("AuthClient")

    override suspend fun signInWithGoogle(): Result<Unit, NetworkError> {
        return try {
            logger.info("🟢 Starting Google sign-in flow...")

            Result.success(Unit)
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

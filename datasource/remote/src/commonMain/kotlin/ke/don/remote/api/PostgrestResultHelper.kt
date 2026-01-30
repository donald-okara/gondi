/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.remote.api

import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.postgrest.result.PostgrestResult
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.util.network.UnresolvedAddressException
import ke.don.utils.Logger
import ke.don.utils.result.ErrorCategory
import ke.don.utils.result.NetworkError
import ke.don.utils.result.Result
import kotlinx.serialization.SerializationException

val logger = Logger("Postgrest")

inline fun <reified D : Any> PostgrestResult.toDomainResult(
    log: Boolean = false,
): Result<D, NetworkError> =
    try {
        val decoded = decodeAs<D>()
        if (log) logger.info("Decoded: $decoded")
        Result.Success(decoded)
    } catch (e: Exception) {
        if (log) logger.error("Error: $e")
        Result.Error(e.toNetworkError())
    }

/**
 * Converts a [PostgrestResult] into a [Result] containing a list of items.
 */
inline fun <reified D : Any> PostgrestResult.toDomainListResult(
    log: Boolean = false,
): Result<List<D>, NetworkError> =
    try {
        val decoded = decodeList<D>()
        if (log) logger.info("Decoded: $decoded")
        Result.Success(decoded)
    } catch (e: Exception) {
        if (log) logger.error("Error: $e")
        Result.Error(e.toNetworkError())
    }

/**
 * Converts a [PostgrestResult] into a [Result] containing a single item.
 */
inline fun <reified D : Any> PostgrestResult.toDomainSingleResult(
    log: Boolean = false,
): Result<D, NetworkError> =
    try {
        val decoded = decodeSingle<D>()
        if (log) logger.info("Decoded: $decoded")
        Result.Success(decoded)
    } catch (e: Exception) {
        if (log) logger.error("Error: $e")
        Result.Error(e.toNetworkError())
    }

inline fun <reified D : Any> PostgrestResult.toUnitResult(
    log: Boolean = false,
): Result<Unit, NetworkError> {
    try {
        val decoded = decodeAs<D>()
        if (log) logger.info("Decoded: $decoded")
        return Result.Success(Unit)
    } catch (e: Exception) {
        if (log) logger.error("Error: ${e.toNetworkError()}")
        return Result.Error(e.toNetworkError())
    }
}

inline fun <T> runCatchingNetwork(
    block: () -> Result<T, NetworkError>,
): Result<T, NetworkError> {
    return try {
        block()
    } catch (e: Exception) {
        Result.Error(e.toNetworkError())
    }
}

val ErrorCategory.errorTitle
    get() = when (this) {
        ErrorCategory.REQUEST_TIMEOUT -> "Taking too long"
        ErrorCategory.UNAUTHORIZED -> "Sign-in required"
        ErrorCategory.CONFLICT -> "Action can’t be completed"
        ErrorCategory.TOO_MANY_REQUESTS -> "Slow down a bit"
        ErrorCategory.NO_INTERNET -> "You’re offline"
        ErrorCategory.PAYLOAD_TOO_LARGE -> "File is too large"
        ErrorCategory.SERVER -> "Something went wrong"
        ErrorCategory.SERIALIZATION,
        ErrorCategory.DECODING -> "Data issue"
        ErrorCategory.UNKNOWN -> "Something went wrong"
    }

fun Exception.toNetworkError(): NetworkError {
    val logger = Logger("NetworkError")

    logger.error("Error: $this")

    val (category, userMessage) = when (this) {
        is UnresolvedAddressException,
        is HttpRequestException ->
            ErrorCategory.NO_INTERNET to "You appear to be offline. Check your connection and try again."

        is HttpRequestTimeoutException ->
            ErrorCategory.REQUEST_TIMEOUT to "This is taking longer than expected. Please try again."

        is ServerResponseException,
        is ResponseException ->
            ErrorCategory.SERVER to "We’re having trouble on our end. Please try again shortly."

        is SerializationException ->
            ErrorCategory.SERIALIZATION to "We ran into a problem reading the data."

        else ->
            ErrorCategory.UNKNOWN to "Something went wrong. Please try again."
    }

    return NetworkError(
        category = category,
        message = userMessage,
        code = (this as? ResponseException)?.response?.status?.value,
        debugMessage = this.message, // keep original for logs
    )
}

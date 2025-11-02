/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.domain.result

/**
 * A sealed type representing the result of an operation.
 *
 * @param D The type of the successful data.
 * @param E The type of the error, which must extend [Error].
 */
sealed interface Result<out D, out E : Error> {

    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : ke.don.domain.result.Error>(val error: E) : Result<Nothing, E>

    companion object {
        fun <D> success(data: D): Result<D, Nothing> = Success(data)
        fun <E : ke.don.domain.result.Error> error(error: E): Result<Nothing, E> = Error(error)
    }
}

/**
 * Executes [action] if this is [Result.Success].
 */
inline fun <D, E : Error> Result<D, E>.onSuccess(
    action: (D) -> Unit,
): Result<D, E> = apply {
    if (this is Result.Success) action(data)
}

/**
 * Executes [action] if this is [Result.Error].
 */
inline fun <D, E : Error> Result<D, E>.onFailure(
    action: (E) -> Unit,
): Result<D, E> = apply {
    if (this is Result.Error) action(error)
}

/**
 * Maps the success value to another type.
 */
inline fun <D, E : Error, R> Result<D, E>.map(
    transform: (D) -> R,
): Result<R, E> = when (this) {
    is Result.Success -> Result.Success(transform(data))
    is Result.Error -> this
}

/**
 * Maps the error value to another type.
 */
inline fun <D, E : Error, NE : Error> Result<D, E>.mapError(
    transform: (E) -> NE,
): Result<D, NE> = when (this) {
    is Result.Success -> this
    is Result.Error -> Result.Error(transform(error))
}

/**
 * Folds the result into a single value.
 */
inline fun <D, E : Error, R> Result<D, E>.fold(
    onSuccess: (D) -> R,
    onError: (E) -> R,
): R = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error -> onError(error)
}

/**
 * Recovers from an error by providing a fallback value.
 */
inline fun <D, E : Error> Result<D, E>.recover(
    transform: (E) -> D,
): Result<D, Nothing> = when (this) {
    is Result.Success -> this
    is Result.Error -> Result.Success(transform(error))
}

/**
 * Returns the success data or null.
 */
fun <D, E : Error> Result<D, E>.getOrNull(): D? =
    (this as? Result.Success)?.data

/**
 * Returns the error or null.
 */
fun <D, E : Error> Result<D, E>.errorOrNull(): E? =
    (this as? Result.Error)?.error

/**
 * Checks if this is a success.
 */
fun Result<*, *>.isSuccess(): Boolean = this is Result.Success

/**
 * Checks if this is an error.
 */
fun Result<*, *>.isError(): Boolean = this is Result.Error

/**
 * Typealias for results where only success/failure is relevant.
 */
typealias EmptyResult<E> = Result<Unit, E>

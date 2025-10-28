/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.helpers

import ke.don.koffee.domain.Koffee
import ke.don.koffee.model.ToastAction
import ke.don.koffee.model.ToastDuration
import ke.don.koffee.model.ToastType

/**
 * A helper object for showing different types of toasts using the Koffee library.
 * It provides convenient functions for displaying info, success, warning, error, and neutral toasts
 * with customizable titles, descriptions, durations, and actions.
 */
object Matcha {
    private val defaultDismiss = ToastAction(
        label = "Got it",
        onClick = {},
        dismissAfter = true,
    )

    inline fun runIfNotUnitTest(block: () -> Unit) {
        if (!isRunningUnitTest()) block()
    }

    fun info(
        title: String,
        description: String,
        duration: ToastDuration = ToastDuration.Short,
        primaryAction: ToastAction? = null,
        secondaryAction: ToastAction? = defaultDismiss,
        isAppVisible: Boolean = true,
    ) = runIfNotUnitTest {
        Koffee.show(
            title = title,
            description = description,
            type = ToastType.Info,
            duration = duration,
            primaryAction = primaryAction,
            secondaryAction = secondaryAction,
            isAppVisible = isAppVisible,
        )
    }

    fun success(
        title: String,
        description: String = "",
        duration: ToastDuration = ToastDuration.Short,
        primaryAction: ToastAction? = null,
        secondaryAction: ToastAction? = null,
        isAppVisible: Boolean = true,
    ) = runIfNotUnitTest {
        Koffee.show(
            title = title,
            description = description,
            type = ToastType.Success,
            duration = duration,
            primaryAction = primaryAction,
            secondaryAction = secondaryAction,
            isAppVisible = isAppVisible,
        )
    }

    fun warning(
        title: String,
        description: String,
        duration: ToastDuration = ToastDuration.Long,
        primaryAction: ToastAction? = null,
        secondaryAction: ToastAction? = defaultDismiss,
        isAppVisible: Boolean = true,
    ) = runIfNotUnitTest {
        Koffee.show(
            title = title,
            description = description,
            type = ToastType.Warning,
            duration = duration,
            primaryAction = primaryAction,
            secondaryAction = secondaryAction,
            isAppVisible = isAppVisible,
        )
    }

    fun error(
        title: String,
        description: String = "",
        duration: ToastDuration = ToastDuration.Long,
        primaryAction: ToastAction? = null,
        secondaryAction: ToastAction? = defaultDismiss,
        isAppVisible: Boolean = true,
    ) = runIfNotUnitTest {
        Koffee.show(
            title = title,
            description = description,
            type = ToastType.Error,
            duration = duration,
            primaryAction = primaryAction,
            secondaryAction = secondaryAction,
            isAppVisible = isAppVisible,
        )
    }

    fun neutral(
        title: String,
        description: String,
        duration: ToastDuration = ToastDuration.Short,
        primaryAction: ToastAction? = null,
        secondaryAction: ToastAction? = null,
        isAppVisible: Boolean = true,
    ) = runIfNotUnitTest {
        Koffee.show(
            title = title,
            description = description,
            type = ToastType.Neutral,
            duration = duration,
            primaryAction = primaryAction,
            secondaryAction = secondaryAction,
            isAppVisible = isAppVisible,
        )
    }

    fun showErrorToast(
        title: String? = null,
        message: String?,
        retryAction: (() -> Unit)? = null,
        duration: ToastDuration = ToastDuration.Long,
    ) = runIfNotUnitTest {
        error(
            title = title ?: "Something went wrong",
            description = message ?: "Something went wrong",
            primaryAction = if (retryAction != null) ToastAction(label = "Retry", onClick = retryAction, dismissAfter = true) else null,
            duration = duration,
        )
    }
}

expect fun isRunningUnitTest(): Boolean

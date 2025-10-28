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

    /**
     * Executes the provided [block] only when the code is not running unit tests.
     *
     * @param block The action to run if the current process is not a unit test.
     */
    inline fun runIfNotUnitTest(block: () -> Unit) {
        if (!isRunningUnitTest()) block()
    }

    /**
     * Display an informational toast with optional primary and secondary actions.
     *
     * @param description Optional descriptive text shown below the title.
     * @param primaryAction Optional action presented on the toast (e.g., a button).
     * @param secondaryAction Optional secondary action presented on the toast (e.g., dismiss or alternate button).
     * @param isAppVisible When `true`, the toast is intended to be shown only while the app is visible; when `false`, it may be shown regardless of app visibility.
     */
    fun info(
        title: String,
        description: String = "",
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

    /**
     * Displays a success toast with an optional description and action buttons.
     *
     * @param title The main title shown on the toast.
     * @param description Optional additional text shown below the title.
     * @param duration How long the toast is displayed.
     * @param primaryAction Optional primary action shown on the toast.
     * @param secondaryAction Optional secondary action shown on the toast.
     * @param isAppVisible Whether the toast should be shown only when the app is visible.
     */
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

    /**
     * Displays a warning toast with an optional description and action buttons.
     *
     * @param title The toast title.
     * @param description The optional detailed message shown under the title.
     * @param duration How long the toast is displayed.
     * @param primaryAction Optional primary action for the toast.
     * @param secondaryAction Optional secondary action for the toast (defaults to a dismiss action).
     * @param isAppVisible If `true`, show the toast only when the app is visible; if `false`, show regardless of visibility.
     */
    fun warning(
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
            type = ToastType.Warning,
            duration = duration,
            primaryAction = primaryAction,
            secondaryAction = secondaryAction,
            isAppVisible = isAppVisible,
        )
    }

    /**
     * Displays an error toast with an optional description and actions.
     *
     * @param title The title text shown on the toast.
     * @param description Optional descriptive text shown below the title.
     * @param duration How long the toast should be shown.
     * @param primaryAction Optional primary action displayed on the toast.
     * @param secondaryAction Optional secondary action displayed on the toast; defaults to a dismiss action.
     * @param isAppVisible When true, show the toast only if the app is visible.
     */
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

    /**
     * Displays a neutral toast with the given title and description.
     *
     * Does nothing when running inside a unit test.
     *
     * @param title The toast title.
     * @param description The toast description/body.
     * @param duration How long the toast is shown.
     * @param primaryAction Optional primary action shown on the toast (invoked when the action is tapped).
     * @param secondaryAction Optional secondary action shown on the toast.
     * @param isAppVisible Whether the app is currently visible (used to decide toast presentation).
     */
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

    /**
     * Shows a generic error toast with an optional retry action.
     *
     * If `title` or `message` are null, they default to "Something went wrong". If `retryAction` is provided,
     * the toast includes a "Retry" action that invokes `retryAction` and dismisses the toast. No toast is shown while running unit tests.
     *
     * @param title Optional title to display; defaults to "Something went wrong" when null.
     * @param message Optional description to display; defaults to "Something went wrong" when null.
     * @param retryAction Optional callback invoked when the user taps the "Retry" action.
     * @param duration Toast display duration; defaults to `ToastDuration.Long`.
     */
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

/**
 * Determines whether the current execution is running under unit tests.
 *
 * @return `true` if the runtime environment is executing unit tests, `false` otherwise.
 */
expect fun isRunningUnitTest(): Boolean
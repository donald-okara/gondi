/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.dialog

import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing

/**
 * Displays composable content inside a themed dialog surface with medium shape and standard padding.
 *
 * @param onDismissRequest Called when the dialog is requested to be dismissed.
 * @param modifier Optional [Modifier] applied to the inner Surface.
 * @param content The composable content to show inside the dialog's surface.
 */
@Composable
fun DialogToken(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            modifier = modifier
                .wrapContentSize(),
            shape = MaterialTheme.shapes.medium,
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetToken(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    sheetState: SheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    ),
    content: @Composable () -> Unit,
){
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        containerColor = containerColor,
        contentColor = contentColor,
        modifier = modifier.width(Theme.spacing.largeScreenSize),
        sheetState = sheetState
    ){
        content()
    }
}

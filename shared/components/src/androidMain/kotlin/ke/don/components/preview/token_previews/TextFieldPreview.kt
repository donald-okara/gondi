/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.preview.token_previews

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.components.text_field.TextFieldToken

@DevicePreviews
@Composable
fun FormTextFieldPreview(
    @PreviewParameter(DarkModeProvider::class) isDarkTheme: Boolean,
) {
    DevicePreviewContainer(isDarkTheme = isDarkTheme) {
        Column {
            TextFieldToken(
                label = "Name",
                text = "",
                onValueChange = {},
                comment = "20% discount",
                enabled = true,
                isError = true,
                errorMessage = "Name is too short",
                showLength = true,
                nameLength = 100,
                maxLength = 200,
                modifier = Modifier.width(500.dp),
            )
        }
    }
}

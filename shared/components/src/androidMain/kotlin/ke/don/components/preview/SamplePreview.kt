/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.components.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

data class DemoData(val title: String, val isDark: Boolean = false)

class DemoDataProvider : PreviewParameterProvider<DemoData> {
    override val values = sequenceOf(
        DemoData("Lucy", true),
        DemoData("Lucy", false),
        DemoData("Annie", true),
        DemoData("Annie", false),
    )
}

@DevicePreviews
@Composable
fun MyScreenPreview(
    @PreviewParameter(DemoDataProvider::class) demoData: DemoData,
) {
    DevicePreviewContainer(isDarkTheme = demoData.isDark) {
        SampleScreen(demoData.title)
    }
}

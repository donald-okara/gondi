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
import ke.don.domain.datastore.Theme

data class DemoData(val title: String, val theme: Theme)

class DemoDataProvider : PreviewParameterProvider<DemoData> {
    override val values = sequenceOf(
        DemoData("Lucy", Theme.Dark),
        DemoData("Lucy", Theme.Light),
        DemoData("Annie", Theme.Dark),
        DemoData("Annie", Theme.Light),
    )
}

/**
 * Preview of SampleScreen using provided DemoData inside a DevicePreviewContainer.
 *
 * Renders SampleScreen with demoData.title and sets the container's dark theme according to demoData.isDark.
 *
 * @param demoData DemoData supplying the screen title and whether the preview uses a dark theme.
 */
@DevicePreviews
@Composable
fun MyScreenPreview(
    @PreviewParameter(DemoDataProvider::class) demoData: DemoData,
) {
    DevicePreviewContainer(demoData.theme) {
        SampleScreen(demoData.title)
    }
}

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

import androidx.compose.ui.tooling.preview.Preview

// androidMain
@Preview(
    name = "Mobile",
    group = "Devices",
    device = "spec:width=411dp,height=890dp,dpi=420", // typical phone
)
@Preview(
    name = "Tablet",
    group = "Devices",
    device = "spec:width=720dp,height=800dp,dpi=360", // typical tablet
)
@Preview(
    name = "Desktop",
    group = "Devices",
    device = "spec:width=1280dp,height=800dp,dpi=240", // wide desktop-like
)
annotation class DevicePreviews

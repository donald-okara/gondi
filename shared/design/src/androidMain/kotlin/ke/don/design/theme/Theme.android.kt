package ke.don.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

actual val systemIsDark: Boolean
    @Composable get() = isSystemInDarkTheme()
package ke.don.components.preview.token_previews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import ke.don.components.list_items.CodeOfConductSection
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.domain.datastore.Theme

@DevicePreviews
@Composable
fun CodeOfConductPreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    DevicePreviewContainer(theme) {
        Box(modifier = Modifier.fillMaxSize()) { CodeOfConductSection() }
    }
}
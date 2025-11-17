package ke.don.components.preview.token_previews

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import ke.don.components.list_items.GamePhases
import ke.don.components.list_items.RolesList
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.design.theme.spacing
import ke.don.domain.datastore.Theme

@DevicePreviews
@Composable
fun RolesPreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    DevicePreviewContainer(theme) {
        Box {
            RolesList(
                modifier = Modifier.padding(
                    MaterialTheme.spacing.medium
                )
            )
        }

    }
}


@DevicePreviews
@Composable
fun PhasesPreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    DevicePreviewContainer(theme) {
        Box {
            GamePhases()
        }

    }
}
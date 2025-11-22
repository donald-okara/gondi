package ke.don.components.preview.token_previews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ke.don.components.indicator.GlowingSelectableSurface
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.components.profile.PlayerItem
import ke.don.design.theme.AppTheme
import ke.don.domain.datastore.Theme
import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.Role
import ke.don.domain.state.Player
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground

@Preview
@Composable
fun GLowingPreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    DevicePreviewContainer(theme) {
        AppTheme(theme) {
            val colors = listOf(
                MaterialTheme.colorScheme.primary to "Primary",
                MaterialTheme.colorScheme.secondary to "Secondary",
                MaterialTheme.colorScheme.tertiary to "Tertiary",
                MaterialTheme.colorScheme.error to "Error",

                AppTheme.extendedColors.info.color to "Info",
                AppTheme.extendedColors.success.color to "Success",
                AppTheme.extendedColors.warning.color to "Warning",
            )
            var selectedColor by remember { mutableStateOf(colors.first().first) }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    colors.forEach { (color, label) ->
                        GlowingSelectableSurface(
                            selected = selectedColor == color,
                            onClick = { selectedColor = color },
                            glowingColor = color
                        ) {
                            Text(
                                text = label,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@DevicePreviews
@Composable
fun PlayersPreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    DevicePreviewContainer(theme) {
        AppTheme(theme) {

            var selectedColor by remember { mutableStateOf(ActionType.ACCUSE) }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Adaptive(130.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ActionType.entries){ actionType ->
                        PlayerItem(
                            actionType = actionType,
                            onClick = { selectedColor = actionType },
                            isSelected = selectedColor == actionType,
                            player = Player(
                                name = "Player Name",
                                role = Role.ACCOMPLICE,
                                avatar = Avatar.Alexander,
                                background = AvatarBackground.PURPLE_LILAC,
                                isAlive = true,
                            )
                        )
                    }
                }
            }
        }
    }
}
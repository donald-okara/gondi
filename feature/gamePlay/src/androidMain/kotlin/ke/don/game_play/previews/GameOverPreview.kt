package ke.don.game_play.previews

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.PreviewParameter
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.components.preview.token_previews.ThemeProvider
import ke.don.components.scaffold.NavigationIcon
import ke.don.components.scaffold.ScaffoldToken
import ke.don.domain.datastore.Theme
import ke.don.domain.gameplay.Faction
import ke.don.domain.gameplay.Role
import ke.don.domain.state.Player
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import ke.don.game_play.shared.SharedGameOver
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
fun GameOverPreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    val players = remember {
        listOf(
            Player(id = "1", name = "Matt Foley", role = Role.VILLAGER, Avatar.Alexander, background = AvatarBackground.PURPLE_LILAC, isAlive = false),
            Player(id = "2", name = "Stefon Zelesky", role = Role.VILLAGER, Avatar.Christian, background = AvatarBackground.PINK_HOT, isAlive = false),
            Player(id = "3", name = "David S. Pumpkins", role = Role.GONDI, Avatar.Amaya, background = AvatarBackground.YELLOW_BANANA),
            Player(id = "4", name = "Roseanne Roseannadanna", role = Role.GONDI, Avatar.Aidan, background = AvatarBackground.GREEN_LEAFY),
            Player(id = "5", name = "Todd O'Connor", role = Role.VILLAGER, Avatar.Kimberly, background = AvatarBackground.ORANGE_CORAL, isAlive = false),
            Player(id = "6", name = "Pat O'Neill", role = Role.VILLAGER, Avatar.George, background = AvatarBackground.PURPLE_AMETHYST, isAlive = false),
            Player(id = "7", name = "Hans", role = Role.VILLAGER, Avatar.Jocelyn, background = AvatarBackground.GREEN_MINTY),
            Player(id = "8", name = "Franz", role = Role.MODERATOR, Avatar.Jameson, background = AvatarBackground.YELLOW_GOLDEN),
        )
    }

    val winningFaction = Faction.GONDI

    DevicePreviewContainer(theme){
        ScaffoldToken(
            title = "Game Over",
            navigationIcon = NavigationIcon.Back {},
        ) {
            SharedGameOver(
                players = players,
                myPlayer = players.first(),
                winnerFaction = winningFaction,
                isModerator = true,
            )
        }

    }
}
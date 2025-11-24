package ke.don.game_play.previews

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.PreviewParameter
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.components.preview.token_previews.ThemeProvider
import ke.don.components.scaffold.NavigationIcon
import ke.don.components.scaffold.ScaffoldToken
import ke.don.domain.datastore.Theme
import ke.don.domain.gameplay.Role
import ke.don.domain.state.Player
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import ke.don.game_play.moderator.model.ModeratorState
import ke.don.game_play.moderator.screens.ModeratorLobby

@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
fun PlayersPreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
   val moderatorState = remember {
       ModeratorState()
   }

    val players = remember {
        listOf(
            Player(id = "1", name = "Matt Foley", role = Role.VILLAGER, Avatar.Alexander, background = AvatarBackground.PURPLE_LILAC),
            Player(id = "2", name = "Stefon Zelesky", role = Role.VILLAGER, Avatar.Christian, background = AvatarBackground.PINK_HOT),
            Player(id = "3", name = "David S. Pumpkins", role = Role.GONDI, Avatar.Amaya, background = AvatarBackground.YELLOW_BANANA),
            Player(id = "4", name = "Roseanne Roseannadanna", role = Role.DETECTIVE, Avatar.Aidan, background = AvatarBackground.GREEN_LEAFY),
            Player(id = "5", name = "Todd O'Connor", role = Role.VILLAGER, Avatar.Kimberly, background = AvatarBackground.ORANGE_CORAL),
            Player(id = "6", name = "Pat O'Neill", role = Role.VILLAGER, Avatar.George, background = AvatarBackground.PURPLE_AMETHYST),
            Player(id = "7", name = "Hans", role = Role.VILLAGER, Avatar.Jocelyn, background = AvatarBackground.GREEN_MINTY),
            Player(id = "8", name = "Franz", role = Role.GONDI, Avatar.Jameson, background = AvatarBackground.YELLOW_GOLDEN)
        )
    }

    DevicePreviewContainer(theme){
        ScaffoldToken(
            title = "Lobby",
            navigationIcon = NavigationIcon.Back {}
        ){
            ModeratorLobby(
                moderatorState = moderatorState,
                players = players,
                onEvent = {}
            )
        }
    }
}
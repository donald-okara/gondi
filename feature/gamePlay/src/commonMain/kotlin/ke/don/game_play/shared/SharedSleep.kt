package ke.don.game_play.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.domain.gameplay.SelectedPlayer
import ke.don.domain.state.KnownIdentity
import ke.don.domain.state.Player
import ke.don.game_play.shared.components.PlayersGrid


@Composable
fun SharedSleep(
    modifier: Modifier = Modifier,
    myPlayerId: String?,
    instruction: String,
    isModerator: Boolean,
    onProceed: () -> Unit,
    enabled: Boolean = false,
    knownIdentity: List<String> = emptyList(),
    onSelectPlayer: (String) -> Unit,
    alivePlayers: List<Player>,
    selectedPlayers: List<SelectedPlayer> = emptyList(),
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            if (isModerator){
                ButtonToken(
                    modifier = Modifier.fillMaxWidth(),
                    buttonType = ComponentType.Primary,
                    onClick = onProceed,
                    enabled = enabled
                ) {
                    Text(
                        "Proceed"
                    )
                }
            }
        }
        item {
            Text(
                text = instruction,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

        }
        item{
            PlayersGrid(
                alivePlayers = alivePlayers,
                selectedPlayers = selectedPlayers,
                onSelectPlayer = onSelectPlayer,
                myPlayerId = myPlayerId,
                knownIdentities = knownIdentity,
                showEmpty = false,
                availableSlots = 0
            )
        }
    }
}
package ke.don.game_play.shared.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import ke.don.design.theme.spacing
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.table.Profile

@Composable
fun AccusationItem(
    modifier: Modifier = Modifier,
    gameState: GameState,
    accuser: Player,
    accused: Player,
    seconder: Player,
    myProfile: Player
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.CenterVertically)
    ){
        Text(
            text = "${accuser.name} accuses ${accused.name}",
            style = MaterialTheme.typography.headlineMedium
        )

        AnimatedVisibility(visible = gameState.second == null) {
            Text(
                text = "Waiting for a second to proceed to court",
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Row(
            modifier = Modifier
                .padding(vertical = MaterialTheme.spacing.medium)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

        }

    }
}
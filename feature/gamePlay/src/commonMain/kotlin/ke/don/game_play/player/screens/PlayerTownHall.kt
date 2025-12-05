package ke.don.game_play.player.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.game_play.player.model.PlayerHandler
import ke.don.game_play.player.model.PlayerState
import ke.don.game_play.shared.SharedTownHall
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun PlayerTownHall(
    modifier: Modifier = Modifier,
    gameState: GameState,
    myPlayer: Player,
    players: List<Player>,
    playerState: PlayerState,
    onEvent: (PlayerHandler) -> Unit,
) {
    val accuser by remember(
        gameState.accusedPlayer
    ) {
        derivedStateOf {
            players.find { it.id == gameState.accusedPlayer?.playerId }
        }
    }

    val seconder by remember(
        gameState.accusedPlayer
    ) {
        derivedStateOf {
            players.find { it.id == gameState.second?.playerId }
        }
    }

    val accused by remember(
        gameState.accusedPlayer
    ) {
        derivedStateOf {
            players.find { it.id == gameState.accusedPlayer?.targetId }
        }
    }

    SharedTownHall(
        players = players,
        onSelectPlayer = { TODO("Add accusation modal") },
        myPlayerId = myPlayer.id,
        seconder = seconder,
        accuser = accuser,
        accused = accused,
        knownIdentity = myPlayer.knownIdentities.map { it.playerId },
        onSecond = { accused?.id?.let { onEvent(PlayerHandler.Send(PlayerIntent.Second(myPlayer.id, gameState.round, it))) } },
        goToCourt = {},
        exoneratePlayer = {},
        onShowRules = {onEvent(PlayerHandler.ShowRulesModal)},
        isModerator = false,
        announcements = playerState.announcements,
        modifier = modifier
    )
}
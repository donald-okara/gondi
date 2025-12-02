package ke.don.game_play.player.screens

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.Role
import ke.don.domain.gameplay.SelectedPlayer
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.game_play.player.model.PlayerHandler
import ke.don.game_play.player.model.PlayerState
import ke.don.game_play.shared.SharedSleep
import ke.don.game_play.shared.components.PlayersGrid

@Composable
fun PlayerSleep(
    modifier: Modifier = Modifier,
    gameState: GameState,
    myPlayer: Player,
    players: List<Player>,
    playerState: PlayerState,
    onEvent: (PlayerHandler) -> Unit,
) {
    val currentRound = gameState.round
    val alivePlayers = players.filter { it.isAlive }
    val selectedPlayers by remember(myPlayer, gameState) {
        derivedStateOf {
            when (myPlayer.role) {
                Role.GONDI -> {
                    gameState.pendingKills.map { playerId ->
                        SelectedPlayer(playerId, ActionType.KILL)
                    }
                }

                Role.DOCTOR -> {
                    gameState.lastSavedPlayerId?.let {
                        listOf(SelectedPlayer(it, ActionType.SAVE))
                    } ?: emptyList()
                }
                Role.DETECTIVE -> {
                    val lastInvestigated = myPlayer.knownIdentities.find { identity -> identity.round == currentRound }
                    listOf(SelectedPlayer(lastInvestigated?.playerId ?: "", ActionType.INVESTIGATE))
                }

                else -> emptyList()
            }
        }
    }

    val instruction by remember {
        derivedStateOf {
            when (myPlayer.role) {
                Role.GONDI -> "You are a Gondi, Pick a player to kill"
                Role.DOCTOR -> "You are a Doctor, Pick a player to save"
                Role.DETECTIVE -> "You are a Detective, Pick a player to investigate"
                else -> "Have a good rest, Please do not snore"
            }
        }
    }

    SharedSleep(
        modifier = modifier,
        myPlayerId = myPlayer.id,
        onSelectPlayer = {
            onEvent(PlayerHandler.SelectPlayer(it))
        },
        alivePlayers = players,
        selectedPlayers = selectedPlayers,
        instruction = instruction,
        knownIdentity = myPlayer.knownIdentities.map { it.playerId },
        isModerator = false,
        onProceed = {}
    )
}
package ke.don.game_play.moderator.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.SelectedPlayer
import ke.don.domain.gameplay.isActingInSleep
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.nextPhase
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.shared.SharedSleep

@Composable
fun ModeratorSleep(
    modifier: Modifier = Modifier,
    gameState: GameState,
    myPlayer: Player,
    players: List<Player>,
    onEvent: (ModeratorHandler) -> Unit,
) {
    val currentRound = gameState.round
    val actingPlayers =
        players.filter { player ->
            isActingInSleep(player, currentRound)
        }.map { it.id }
    val alivePlayers = players.filter { it.isAlive }
    val lastSaved = gameState.lastSavedPlayerId to ActionType.SAVE
    val pendingKills = gameState.pendingKills.map { it to ActionType.KILL }

    val selectedPlayers =
        buildList {
            lastSaved.first?.let {
                add(SelectedPlayer(it, lastSaved.second))
            }
            addAll(pendingKills)
        }

    val instruction = if (actingPlayers.isEmpty())
        "You can now proceed to Town hall" else
        "Someone has not done their part yet"

    SharedSleep(
        modifier = modifier,
        myPlayerId = myPlayer.id,
        onSelectPlayer = {
            onEvent(ModeratorHandler.SelectPlayer(it))
        },
        alivePlayers = alivePlayers,
        selectedPlayers = selectedPlayers,
        instruction = instruction,
        knownIdentity = myPlayer.knownIdentities.map { it.playerId },
        isModerator = true,
        enabled = actingPlayers.isEmpty(),
        onProceed = {
            onEvent(
                ModeratorHandler.HandleModeratorCommand(
                    ModeratorCommand.AdvancePhase(
                        gameState.id,
                        gameState.phase.nextPhase
                    )
                )
            )
        },
        actingPlayers = actingPlayers,
        onShowRules = { onEvent(ModeratorHandler.ShowRulesModal) }
    )
}


package ke.don.game_play.moderator.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.state.GamePhase
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.moderator.model.ModeratorState
import ke.don.game_play.shared.SharedTownHall
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun ModeratorTownHall(
    modifier: Modifier = Modifier,
    gameState: GameState,
    moderatorState: ModeratorState,
    myPlayer: Player,
    players: List<Player>,
    onEvent: (ModeratorHandler) -> Unit,
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

    val nextPhase by remember(
        gameState.accusedPlayer
    ) {
        derivedStateOf {
            if (gameState.accusedPlayer == null) GamePhase.SLEEP else GamePhase.COURT
        }
    }


    SharedTownHall(
        players = players,
        onSelectPlayer = { },
        myPlayerId = myPlayer.id,
        seconder = seconder,
        accuser = accuser,
        accused = accused,
        isModerator = true,
        onSecond = {},
        goToCourt = {
            onEvent(
                ModeratorHandler.HandleModeratorCommand(
                    ModeratorCommand.AdvancePhase(
                        gameState.id,
                        nextPhase
                    )
                )
            )
        },
        exoneratePlayer = {
            onEvent(
                ModeratorHandler.HandleModeratorCommand(
                    ModeratorCommand.ExoneratePlayer(
                        gameState.id
                    )
                )
            )
        },
        announcements = moderatorState.announcements,
        onShowRules = { onEvent(ModeratorHandler.ShowRulesModal) },
        modifier = modifier
    )
}
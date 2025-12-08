package ke.don.game_play.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import ke.don.game_play.moderator.model.Announcement
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun SharedCourt(
    modifier: Modifier = Modifier,
    myPlayer: Player,
    onVote: () -> Unit,
    proceed: () -> Unit,
    showRules: () -> Unit,
    accused: Player?,
    seconder: Player?,
    isModerator: Boolean = false,
    accuser: Player?,
    votes: List<Vote>,
    players: List<Player>,
    announcements: List<Announcement>
) {
    val notVoted by remember(players, votes) {
        derivedStateOf {
            players.filter { player ->
                votes.none { vote -> vote.voterId == player.id }
                    .and(player.isAlive)
                    .and(player != accused)
            }.map { value ->
                value.id
            }
        }
    }

    SharedTownHall(
        players = players,
        onSelectPlayer = {},
        myPlayerId = myPlayer.id,
        seconder = seconder,
        accuser = accuser,
        accused = accused,
        knownIdentity = myPlayer.knownIdentities.map { it.playerId },
        onSecond = {},
        proceed = proceed,
        exoneratePlayer = {},
        onShowRules = showRules,
        isModerator = isModerator,
        actingPlayers = notVoted,
        announcements = announcements,
        modifier = modifier,
        isCourt = true,
        onVote = onVote
    )
}
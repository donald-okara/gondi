package ke.don.gondi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ke.don.domain.gameplay.server.ServerId
import ke.don.game_play.player.di.GAME_PLAYER_SCOPE
import ke.don.game_play.player.model.GondiClient
import ke.don.game_play.player.model.PlayerHandler
import ke.don.game_play.player.screens.MainPlayerContent
import kotlinx.coroutines.launch
import org.koin.compose.getKoin
import org.koin.core.qualifier.named
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MainPlayerScreen(
    private val serverId: ServerId
) : Screen {
    @OptIn(ExperimentalComposeUiApi::class, ExperimentalUuidApi::class)
    @Composable
    override fun Content() {
        val koin = getKoin()
        val screen = this


        val navigator = LocalNavigator.currentOrThrow

        val gondiClient = koinScreenModel<GondiClient>()

        val gameState by gondiClient.gameState.collectAsState()
        val players by gondiClient.players.collectAsState()
        val currentPlayer by gondiClient.currentPlayer.collectAsState(
            initial = null,
        )
        val votes by gondiClient.votes.collectAsState()
        val playerState by gondiClient.playerState.collectAsState()

        val onEvent = gondiClient::onEvent

        LaunchedEffect(serverId){
            onEvent(PlayerHandler.Connect(serverId = serverId))
        }


        BackHandler(enabled = true) {
            onEvent(PlayerHandler.ShowLeaveDialog)
        }

        MainPlayerContent(
            playerState = playerState,
            gameState = gameState,
            players = players,
            votes = votes,
            onEvent = onEvent,
            onBack = navigator::pop,
            currentPlayer = currentPlayer,
        )
    }
}

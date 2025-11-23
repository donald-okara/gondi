package ke.don.gondi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ke.don.game_play.moderator.di.GAME_MODERATOR_SCOPE
import ke.don.game_play.moderator.model.GondiHost
import ke.don.game_play.moderator.screens.MainModeratorContent
import org.koin.compose.getKoin
import org.koin.core.qualifier.named
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MainModeratorScreen: Screen {
    @Composable
    override fun Content() {
        val gondiHost = koinScreenModel<GondiHost>()

        val gameState by gondiHost.gameState.collectAsState()
        val players by gondiHost.players.collectAsState()
        val votes by gondiHost.votes.collectAsState()
        val moderatorState by gondiHost.moderatorState.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        val onEvent = gondiHost::onEvent

        MainModeratorContent(
            moderatorState = moderatorState,
            gameState = gameState,
            players = players,
            votes = votes,
            onEvent = onEvent,
            onBack = navigator::pop
        )
    }
}
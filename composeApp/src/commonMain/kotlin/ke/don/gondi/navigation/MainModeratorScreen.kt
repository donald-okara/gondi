package ke.don.gondi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ke.don.game_play.moderator.di.GAME_MODERATOR_SCOPE
import ke.don.game_play.moderator.model.GondiHost
import ke.don.game_play.moderator.model.ModeratorHandler
import ke.don.game_play.moderator.screens.MainModeratorContent
import org.koin.compose.getKoin
import org.koin.core.qualifier.named

class MainModeratorScreen: Screen {
    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val koin = getKoin()
        val screen = this

        val moderatorScope = remember(screen) {
            koin.createScope(
                scopeId = GAME_MODERATOR_SCOPE,
                qualifier = named(GAME_MODERATOR_SCOPE)
            )
        }

        DisposableEffect(screen) {
            onDispose {
                if (moderatorScope.isNotClosed()) {
                    moderatorScope.close()
                }
            }
        }


        val gondiHost = moderatorScope.get<GondiHost>()

        val gameState by gondiHost.gameState.collectAsState()
        val players by gondiHost.players.collectAsState()
        val votes by gondiHost.votes.collectAsState()
        val moderatorState by gondiHost.moderatorState.collectAsState()

        val navigator = LocalNavigator.currentOrThrow

        val onEvent = gondiHost::onEvent


        BackHandler(enabled = false) {
            onEvent(ModeratorHandler.ShowLeaveDialog)
        }

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
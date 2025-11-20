package ke.don.gondi.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ke.don.game_play.model.GondiHost
import ke.don.game_play.screens.MainModeratorContent

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
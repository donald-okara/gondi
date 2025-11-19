package ke.don.gondi.navigation

import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import ke.don.components.list_items.RulesContent
import ke.don.components.scaffold.NavigationIcon
import ke.don.components.scaffold.ScaffoldToken

class RulesScreen: Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        ScaffoldToken(
            title = "Rules",
            scrollState = rememberScrollState(),
            navigationIcon = NavigationIcon.Back{
                navigator.pop()
            }
        ){ RulesContent() }
    }
}
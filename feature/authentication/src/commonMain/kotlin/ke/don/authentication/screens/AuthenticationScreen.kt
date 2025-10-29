package ke.don.authentication.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import ke.don.components.empty_state.EmptyScreen

class AuthenticationScreen: Screen {
    @Composable
    override fun Content() {
        EmptyScreen(
            icon = Icons.Outlined.Lock,
            title = "Authentication",
            description = "Screen is in development"
        )
    }
}
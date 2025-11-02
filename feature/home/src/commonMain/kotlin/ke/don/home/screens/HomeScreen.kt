package ke.don.home.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.empty_state.EmptyScreen
import ke.don.components.helpers.Matcha
import ke.don.components.profile.ProfileImageToken
import ke.don.components.scaffold.ScaffoldToken
import ke.don.domain.model.Avatar
import ke.don.domain.model.Profile

class HomeScreen: Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        ScaffoldToken(
            title = "Gondi",
            actions = {
                ProfileImageToken( // TODO: Temporary until profile is implemented
                    isHero= false,
                    profile = Profile(
                        name = "Donald Isoe",
                        avatar = Avatar.Leo
                    ),
                    on
                )
            }
        ) {
            EmptyScreen(
                title = "Home",
                description = "Screen is in development",
                icon = Icons.Outlined.Home,
            ){
                ButtonToken(
                    buttonType = ComponentType.Inverse,
                    onClick = {
                        Matcha.info(
                            "Sign out"
                        )
                    }
                ){
                    Text("Sign out")
                }
            }
        }
    }
}
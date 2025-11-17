package ke.don.profile.screens

import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ke.don.components.scaffold.NavigationIcon
import ke.don.components.scaffold.ScaffoldToken
import ke.don.profile.model.EditProfileEvent
import ke.don.profile.model.EditProfileState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreenContent(
    modifier: Modifier = Modifier,
    state: EditProfileState,
    onEvent: (EditProfileEvent) -> Unit,
    back: () -> Unit = {}
) {
    ScaffoldToken(
        modifier = modifier,
        title = "Edit Profile",
        navigationIcon = NavigationIcon.Back(back),
        scrollState = rememberScrollState()
    ) {
        EditContent(
            state = state,
            onEvent = onEvent,
            onSave = back
        )
    }
}
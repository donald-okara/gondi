package ke.don.profile.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ke.don.profile.components.SelectAvatarComponent
import ke.don.profile.components.SelectBackgroundComponent
import ke.don.profile.model.EditProfileEvent
import ke.don.profile.model.EditProfileState
import ke.don.resources.Values
import ke.don.resources.isCompact

@Composable
fun EditProfileScreenContent(
    modifier: Modifier = Modifier,
    state: EditProfileState,
    onEvent: (EditProfileEvent) -> Unit,
    isCompact: Boolean = isCompact()
) {
    Column(
        modifier = modifier
            .padding(
                vertical = 16.dp,
                horizontal = if (isCompact) Values.compactScreenPadding else Values.expandedScreenPadding
            )
            .width(720.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
    ) {
        Text(
            "Update your profile",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Welcome, let's get you ready to play",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Choose Your Avatar",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        SelectAvatarComponent(
            selectedAvatar = state.editProfile.avatar,
            selectedBackground = state.editProfile.background,
            onAvatarSelected = {
                onEvent(EditProfileEvent.OnAvatarChanged(it))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Choose Your Background",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        SelectBackgroundComponent(
            selectedBackground = state.editProfile.background,
            onBackgroundSelected = {
                onEvent(EditProfileEvent.OnBackgroundChanged(it))
            }
        )
    }
}
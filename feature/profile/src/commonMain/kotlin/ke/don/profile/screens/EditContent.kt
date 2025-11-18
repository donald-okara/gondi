package ke.don.profile.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.scaffold.NavigationIcon
import ke.don.components.scaffold.ScaffoldToken
import ke.don.components.text_field.TextFieldToken
import ke.don.design.theme.Theme
import ke.don.design.theme.spacing
import ke.don.design.theme.spacingPaddingValues
import ke.don.profile.components.SelectAvatarComponent
import ke.don.profile.components.SelectBackgroundComponent
import ke.don.profile.model.EditProfileEvent
import ke.don.profile.model.EditProfileState
import ke.don.utils.result.isLoading

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContent(
    modifier: Modifier = Modifier,
    state: EditProfileState,
    onEvent: (EditProfileEvent) -> Unit,
    back: (() -> Unit)? = null,
    onSave: () -> Unit = {}
) {
    val maxLength = 12

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.Top)
    ){
        Text(
            text = "Edit your username",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )
        TextFieldToken(
            text = state.editProfile.username,
            label = "Username",
            isRequired = true,
            showLength = true,
            maxLength = maxLength,
            isError = state.editProfile.username.length > maxLength,
            errorMessage = "Username cannot be longer than $maxLength characters",
            onValueChange = {
                onEvent(EditProfileEvent.OnUsernameChanged(it))
            }
        )

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

        Spacer(modifier = Modifier.height(8.dp))
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

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.medium, Alignment.End),
            verticalAlignment = Alignment.CenterVertically
        ) {
            back?.let {
                ButtonToken(
                    buttonType = ComponentType.Neutral,
                    onClick = it
                ) {
                    Text("Back to phases")
                }
            }


            ButtonToken(
                buttonType = ComponentType.Primary,
                loading = state.saveStatus.isLoading,
                enabled = !state.saveStatus.isLoading &&
                        state.editProfile.username.isNotBlank() &&
                        state.editProfile != state.fetchedProfile &&
                        state.editProfile.avatar != null &&
                        state.editProfile.username.length < maxLength
                ,
                onClick = { onEvent(EditProfileEvent.OnSaveProfile(onSave)) }
            ) {
                Text("Finish")
            }
        }
    }

}
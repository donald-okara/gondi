package ke.don.profile.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ke.don.components.indicator.ProgressBar
import ke.don.components.scaffold.NavigationIcon
import ke.don.components.scaffold.ScaffoldToken
import ke.don.design.theme.spacing
import ke.don.profile.model.EditProfileEvent
import ke.don.profile.model.EditProfileState
import ke.don.resources.color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileOnBoardingContent(
    modifier: Modifier = Modifier,
    state: EditProfileState,
    handleEvent: (EditProfileEvent) -> Unit
) {
    var step by remember {
        mutableStateOf(Steps.Profile)
    }

    ScaffoldToken(
        modifier = modifier,
        navigationIcon = NavigationIcon.Back {},
        scrollState = rememberScrollState()
    ) {
        Text(
            step.description,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )

        Text(
            "Welcome, let's get you ready to play",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Step ${step.ordinal + 1} of 2",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        ProgressBar(
            progress = (step.ordinal + 1) / 2f,
            progressColor = if (step == Steps.Profile) state.editProfile.background.color() else MaterialTheme.colorScheme.primaryContainer
        )
        Spacer(modifier = Modifier.height(8.dp))

        AnimatedContent(targetState = step){ currentStep ->
            when(currentStep){
                Steps.Rules -> {}
                Steps.Profile -> EditContent(
                    state = state,
                    onEvent = handleEvent
                )
            }
        }
    }
}

private enum class Steps(
    val description: String
) {
    Profile("Create Your Profile"),
    Rules("Understand The Rules"),
}
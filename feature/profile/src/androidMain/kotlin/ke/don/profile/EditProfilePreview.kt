package ke.don.profile

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import ke.don.components.preview.DeviceFramePreview
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.components.preview.token_previews.ThemeProvider
import ke.don.domain.datastore.Theme
import ke.don.domain.table.Profile
import ke.don.profile.model.EditProfileEvent
import ke.don.profile.model.EditProfileState
import ke.don.profile.screens.EditScreenContent
import ke.don.profile.screens.OnBoardingContent
import ke.don.resources.Resources

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingShowcase(
    modifier: Modifier = Modifier,
    theme: Theme
){
    DevicePreviewContainer(theme) {
        var state by remember {
            mutableStateOf(EditProfileState(
                editProfile = Profile(
                    username = "Timmy"
                )
            ))
        }

        fun handleEvent(event: EditProfileEvent) {
            when (event) {
                is EditProfileEvent.OnAvatarChanged -> {
                    state = state.copy(
                        editProfile = state.editProfile.copy(
                            avatar = event.avatar
                        )
                    )
                }

                is EditProfileEvent.OnBackgroundChanged -> {
                    state = state.copy(
                        editProfile = state.editProfile.copy(
                            background = event.background
                        )
                    )
                }

                is EditProfileEvent.OnSaveProfile -> {}
                is EditProfileEvent.OnUsernameChanged -> {
                    state = state.copy(
                        editProfile = state.editProfile.copy(
                            username = event.username
                        )
                    )
                }
            }
        }

        OnBoardingContent(
            modifier = modifier,
            state = state,
            handleEvent = ::handleEvent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileShowcase(
    modifier: Modifier = Modifier,
    theme: Theme
){
    DevicePreviewContainer(theme) {
        var state by remember {
            mutableStateOf(EditProfileState(
                editProfile = Profile(
                    username = "Timmy"
                )
            ))
        }

        fun handleEvent(event: EditProfileEvent) {
            when (event) {
                is EditProfileEvent.OnAvatarChanged -> {
                    state = state.copy(
                        editProfile = state.editProfile.copy(
                            avatar = event.avatar
                        )
                    )
                }

                is EditProfileEvent.OnBackgroundChanged -> {
                    state = state.copy(
                        editProfile = state.editProfile.copy(
                            background = event.background
                        )
                    )
                }

                is EditProfileEvent.OnSaveProfile -> {}
                is EditProfileEvent.OnUsernameChanged -> {
                    state = state.copy(
                        editProfile = state.editProfile.copy(
                            username = event.username
                        )
                    )
                }
            }
        }

        EditScreenContent(
            modifier = modifier,
            state = state,
            onEvent = ::handleEvent
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
fun OnBoardingPreview(
    @PreviewParameter (ThemeProvider::class) theme: Theme
) {
    OnboardingShowcase(theme = theme)
}


@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
fun EditProfilePreview(
    @PreviewParameter (ThemeProvider::class) theme: Theme
) {
    EditProfileShowcase(theme = theme)
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    name = "Mobile",
    group = "Devices",
    device = "spec:width=411dp,height=890dp,dpi=420", // typical phone
)
@Composable
fun EditProfileMobilePreview(
    @PreviewParameter (ThemeProvider::class) theme: Theme
){
    DeviceFramePreview(
        frame = Resources.Images.MOBILE_FRAME,
    ){
        OnboardingShowcase(theme = theme)
    }
}
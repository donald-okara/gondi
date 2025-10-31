package ke.don.components.preview.token_previews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ke.don.components.preview.DevicePreviewContainer
import ke.don.components.preview.DevicePreviews
import ke.don.components.profile.ProfileImageToken
import ke.don.components.text_field.TextFieldToken
import ke.don.domain.model.Avatar
import ke.don.domain.model.AvatarBackground
import ke.don.domain.model.Profile

val profiles = listOf(
    Profile(
        name = "Christian"
    ),
    Profile(
        name = "Jason"
    )
)

@DevicePreviews
@Composable
fun ProfilePreview(
    @PreviewParameter(DarkModeProvider::class) isDarkTheme: Boolean,
) {
    //<template>
    DevicePreviewContainer(isDarkTheme = isDarkTheme) {

        Column {
            FlowRow(
                modifier = Modifier.width(500.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AvatarBackground.entries.forEach {
                    ProfileImageToken(
                        profile = Profile(
                            name = it.name,
                            background = it
                        ),
                        isHero = true
                    )
                }
            }
            FlowRow(
                modifier = Modifier.width(500.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                AvatarBackground.entries.forEach {
                    ProfileImageToken(
                        profile = Profile(
                            name = it.name,
                            background = it
                        ),
                        isHero = false
                    )
                }
            }
        }

        FlowRow(
            modifier = Modifier.width(500.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AvatarBackground.entries.forEach {
                ProfileImageToken(
                    profile = Profile(
                        avatar = Avatar.Leo,
                        name = it.name,
                        background = it
                    ),
                    isHero = true
                )
            }
        }
        FlowRow(
            modifier = Modifier.width(500.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AvatarBackground.entries.forEach {
                ProfileImageToken(
                    profile = Profile(
                        avatar = Avatar.Leo,
                        name = it.name,
                        background = it
                    ),
                    isHero = false
                )
            }
        }
    }
}




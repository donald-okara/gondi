package ke.don.profile.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.animateBounds
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.LookaheadScope
import androidx.compose.ui.unit.dp
import ke.don.components.profile.ProfileImageToken
import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground
import ke.don.domain.table.Profile
import ke.don.resources.color

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SelectAvatarComponent(
    modifier: Modifier = Modifier,
    onAvatarSelected: (Avatar) -> Unit,
    selectedAvatar: Avatar?,
    selectedBackground: AvatarBackground,
) {
    LookaheadScope{
        FlowRow(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
        ) {
            Avatar.entriesFiltered.forEach { avatar ->
                ProfileImageToken(
                    modifier = Modifier.animateBounds(this@LookaheadScope),
                    profile = Profile(
                        avatar = avatar,
                        background = selectedBackground,
                        username = "Select Avatar",
                    ),
                    onClick = { onAvatarSelected(avatar) },
                    isSelected = avatar == selectedAvatar,
                    isHero = true,
                    heroSize = 64.dp
                )
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SelectBackgroundComponent(
    modifier: Modifier = Modifier,
    selectedBackground: AvatarBackground,
    onBackgroundSelected: (AvatarBackground) -> Unit
) {
    LookaheadScope{
        FlowRow(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom),
        ) {
            AvatarBackground.entriesFiltered.forEach { background ->
                BackgroundItem(
                    modifier = Modifier.animateBounds(this@LookaheadScope),
                    background = background,
                    selected = background == selectedBackground,
                    onClick = { onBackgroundSelected(background) }
                )
            }
        }
    }
}

@Composable
fun BackgroundItem(
    modifier: Modifier = Modifier,
    background: AvatarBackground,
    selected: Boolean,
    onClick: () -> Unit
){
    val baseSize = 24.dp
    val targetSize = if (selected) baseSize * 1.1f else baseSize
    val animatedSize by animateDpAsState(
        targetValue = targetSize,
        label = "animatedSize",
    )
    Box(
        modifier = modifier
            .size(animatedSize)
            .clip(CircleShape)
            .background(background.color())
            .clickable{
                onClick()
            }
    )
}
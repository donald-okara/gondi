package ke.don.design.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data object SpacingDefaults {
    internal const val TINY = 2
    internal const val EXTRA_SMALL = 4
    internal const val SMALL = 8
    internal const val MEDIUM = 16
    internal const val LARGE = 32
    internal const val SCREEN_SIZE = 720
    internal const val DEFAULT = SMALL
}

data class Spacing(
    val default: Dp = SpacingDefaults.DEFAULT.dp,
    val tiny: Dp = SpacingDefaults.TINY.dp,
    val extraSmall: Dp = SpacingDefaults.EXTRA_SMALL.dp,
    val small: Dp = SpacingDefaults.SMALL.dp,
    val medium: Dp = SpacingDefaults.MEDIUM.dp,
    val large: Dp = SpacingDefaults.LARGE.dp,
    val screenSize: Dp = SpacingDefaults.SCREEN_SIZE.dp,
)

enum class SpacingType {
    Tiny,
    ExtraSmall,
    Small,
    Medium,
    Large,
    Default,
}

@Composable
private fun Spacing.resolve(type: SpacingType): Dp = when (type) {
    SpacingType.Tiny -> tiny
    SpacingType.ExtraSmall -> extraSmall
    SpacingType.Small -> small
    SpacingType.Medium -> medium
    SpacingType.Large -> large
    SpacingType.Default -> default
}


val LocalSpacing = compositionLocalOf { Spacing() }

typealias Theme = MaterialTheme

val Theme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current

sealed interface PaddingOption{
    object None : PaddingOption
    object Default : PaddingOption
    class Custom(val padding: Dp) : PaddingOption
}

@Composable
private fun resolvePadding(
    option: PaddingOption,
    default: Dp
): Dp = when (option) {
    PaddingOption.None -> 0.dp
    PaddingOption.Default -> default
    is PaddingOption.Custom -> option.padding
}

@Composable
fun Modifier.spacing(
    type: SpacingType = SpacingType.Default,
    vertical: PaddingOption = PaddingOption.Default,
    horizontal: PaddingOption = PaddingOption.Default
): Modifier {
    val base = Theme.spacing.resolve(type)

    return padding(
        top = resolvePadding(vertical, base),
        bottom = resolvePadding(vertical, base),
        start = resolvePadding(horizontal, base),
        end = resolvePadding(horizontal, base)
    )
}

@Composable
fun spacingPaddingValues(
    type: SpacingType = SpacingType.Default,
    vertical: PaddingOption = PaddingOption.Default,
    horizontal: PaddingOption = PaddingOption.Default
): PaddingValues {
    val base = Theme.spacing.resolve(type)

    return PaddingValues(
        top = resolvePadding(vertical, base),
        bottom = resolvePadding(vertical, base),
        start = resolvePadding(horizontal, base),
        end = resolvePadding(horizontal, base)
    )
}

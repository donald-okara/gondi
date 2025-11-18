package ke.don.components.scaffold

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.OverscrollEffect
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberOverscrollEffect
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import ke.don.components.indicator.FancyRefreshAnimation
import ke.don.design.theme.PaddingOption
import ke.don.design.theme.spacing
import ke.don.design.theme.spacingPaddingValues
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RefreshLazyColumn(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    pullRefreshState: PullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = onRefresh,
    ),
    listOffSet: Int = 0,
    lazyListState: LazyListState = rememberLazyListState(),
    verticalPadding: PaddingOption = PaddingOption.Custom(MaterialTheme.spacing.medium),
    horizontalPadding: PaddingOption = PaddingOption.Custom(MaterialTheme.spacing.small),
    contentAlignment: Alignment = Alignment.TopCenter,
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    overscrollEffect: OverscrollEffect? = rememberOverscrollEffect(),
    content: LazyListScope.() -> Unit
) {
    Box(
        contentAlignment = contentAlignment,
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState)
    ){
        LazyColumn(
            modifier = modifier.offset(
                x = 0.dp,
                y = listOffSet.dp
            ),
            state = lazyListState,
            contentPadding = spacingPaddingValues(
                vertical = verticalPadding,
                horizontal = horizontalPadding
            ),
            reverseLayout = reverseLayout,
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            flingBehavior = flingBehavior,
            userScrollEnabled = userScrollEnabled,
            overscrollEffect = overscrollEffect
        ) {
            content()
        }

        RefreshHeader(
            modifier = Modifier.align(Alignment.TopCenter),
            isRefreshing = isRefreshing,
            state = pullRefreshState
        )
    }

}



@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RefreshHeader(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    state: PullRefreshState
) {
    val animatedOffset by animateDpAsState(
        targetValue = when {
            isRefreshing -> 150.dp
            state.progress in 0f..1f -> (state.progress * 150).dp
            state.progress > 1f -> (150 + (((state.progress - 1f) * .1f) * 150)).dp
            else -> 0.dp
        }, label = "animatedOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .offset(y = (-150).dp)
            .offset { IntOffset(0, animatedOffset.roundToPx()) }
    ) {
        FancyRefreshAnimation(
            modifier = modifier,
            isRefreshing = isRefreshing,
            state = state
        )
    }
}

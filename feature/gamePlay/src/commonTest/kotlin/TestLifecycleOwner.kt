import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.compose.LocalLifecycleOwner

class TestLifecycleOwner : LifecycleOwner {
    private val registry = LifecycleRegistry(this).apply {
        currentState = Lifecycle.State.RESUMED
    }
    override val lifecycle: Lifecycle = registry
}

/**
 * A Composable that provides a [TestLifecycleOwner] to its content.
 *
 * This is useful for testing Composables that rely on a [LifecycleOwner]
 * from [LocalLifecycleOwner]. The provided lifecycle is created in the
 * `RESUMED` state.
 *
 * @param content The Composable content that requires a [LifecycleOwner].
 */
@Composable
fun WithTestLifecycle(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalLifecycleOwner provides TestLifecycleOwner(),
    ) {
        content()
    }
}
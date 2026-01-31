package ke.don.components.steps

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Step(
    val index: Int,
    val label: String,
    val isActive: Boolean = false
)

data class VerticalStep<T>(
    val index: Int,
    val color: Color? = null,
    val icon: ImageVector? = null,
    val label: String,
    val data: T
)


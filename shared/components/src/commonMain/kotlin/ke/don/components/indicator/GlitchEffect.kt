package ke.don.components.indicator

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ke.don.design.theme.Theme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt
import kotlin.random.Random
import kotlin.random.nextInt


@Composable
fun Modifier.glitchEffect(
    visible: Boolean,
    glitchColors: List<Color> = listOf(Theme.colorScheme.primary),
    slices: Int = 20,
): Modifier {

    val end = remember { 20 }
    val graphicsLayer = rememberGraphicsLayer()
    val stepAnimatable = remember { Animatable(if (visible) 0f else end.toFloat()) }
    var step by remember { mutableStateOf(0) }

    LaunchedEffect(visible) {
        stepAnimatable.animateTo(
            targetValue = when (visible) {
                true -> 0f
                false -> end.toFloat()
            },
            animationSpec = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing,
            ),
            block = {
                step = this.value.roundToInt()
            }
        )
    }

    return drawWithContent {
        if (step == 0) {
            drawContent()
            return@drawWithContent
        }
        if (step == end) return@drawWithContent
        graphicsLayer.record { this@drawWithContent.drawContent() }

        val intensity = step / end.toFloat()
        for (i in 0 until slices) {
            if (Random.nextInt(end) < step) continue
            translate(
                left = if (Random.nextInt(5) < step)
                    Random.nextInt(-20..20).toFloat() * intensity
                else
                    0f
            ) {
                scale(
                    scaleY = 1f,
                    scaleX = if (Random.nextInt(10) < step)
                        1f + (1f * Random.nextFloat() * intensity)
                    else
                        1f
                ) {
                    clipRect(
                        top = (i / slices.toFloat()) * size.height,
                        bottom = (((i + 1) / slices.toFloat()) * size.height) + 1f,
                    ) {
                        layer {
                            drawLayer(graphicsLayer)
                            if (Random.nextInt(5, 30) < step) {
                                drawRect(
                                    color = glitchColors.random(),
                                    blendMode = BlendMode.SrcAtop
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GlitchVisibilityImpl() {

    var visible by remember { mutableStateOf(true) }
    var selected by remember { mutableStateOf(false) }
    val interaction = remember { MutableInteractionSource() }
    val isHovered by interaction.collectIsHoveredAsState()
    val color1 = Theme.colorScheme.surface

    LaunchedEffect(visible) {
        if (!visible) {
            delay(2000)
            visible = true
        }
    }

    Box(
        modifier = Modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        visible = false
                    }
                )
            }
            .pointerHoverIcon(PointerIcon.Hand)
            .hoverable(interaction)
            .padding(4.dp)
            .rings(
                ringSpace = if (selected) 6.dp else if (isHovered) 4.dp else 2.dp,
            )
            .background(
                color = color1,
                shape = CutCornerShape(20),
            )
            .clickable(
                interactionSource = interaction,
                onClick = {
                    selected = !selected
                }
            )
            .padding(horizontal = 32.dp, vertical = 16.dp)
    ) {
        Text(
            text = "Tap to Disappear",
            color = Theme.colorScheme.primary
        )
    }

}

@Composable
private fun Modifier.rings(
    ringColor: Color = Theme.colorScheme.primary,
    ringCount: Int = 6,
    ringSpace: Dp = 2.dp
): Modifier {

    val animatedRingSpace by animateDpAsState(
        targetValue = ringSpace,
        animationSpec = tween()
    )

    return (1..ringCount).map { index ->
        Modifier.border(
            width = 1.dp,
            color = ringColor.copy(alpha = index / ringCount.toFloat()),
            shape = CutCornerShape(20),
        )
            .padding(animatedRingSpace)
    }.fold(initial = this) { acc, item -> acc.then(item) }
}

private fun DrawScope.layer(block: DrawScope.() -> Unit) =
    drawIntoCanvas { canvas ->
        canvas.withSaveLayer(
            bounds = size.toRect(),
            paint = Paint(),
        ) { block() }
    }
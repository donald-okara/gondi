package ke.don.components.preview.token_previews

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Gavel
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import ke.don.components.preview.PreviewContainer
import ke.don.components.steps.HorizontalStepper
import ke.don.components.steps.Step
import ke.don.components.steps.VerticalStep
import ke.don.components.steps.verticalSteps
import ke.don.domain.datastore.Theme
import ke.don.domain.table.AvatarBackground
import ke.don.resources.color

@Preview
@Composable
fun HorizontalStepperPreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    PreviewContainer(theme) {
        val steps = listOf(
            Step(0, "Objective"),
            Step(1, "Roles"),
            Step(2, "Night Phase"),
            Step(3, "The Court"),
            Step(4, "Conduct")
        )

        var currentStep by remember { mutableIntStateOf(1) }

        HorizontalStepper(
            steps = steps,
            currentStep = currentStep,
            modifier = Modifier.fillMaxWidth(),
            onStepClick = { clicked ->
                currentStep = clicked
            }
        )

    }
}

@Preview
@Composable
fun VerticalStepperPreview(
    @PreviewParameter(ThemeProvider::class) theme: Theme,
) {
    PreviewContainer(theme) {
        val steps = listOf(
            VerticalStep(
                index = 0,
                icon = Icons.Outlined.Flag,
                color = AvatarBackground.ORANGE_CORAL.color(),
                data = "Learn the basic win conditions for your faction.",
                label = "Objective"
            ),
            VerticalStep(
                index = 1,
                icon = Icons.Outlined.Person,
                color = AvatarBackground.PURPLE_LILAC.color(),
                data = "Understand your character's unique abilities.",
                label = "Roles"
            ),
            VerticalStep(
                index = 2,
                icon = Icons.Outlined.Bedtime,
                color = AvatarBackground.GREEN_EMERALD.color(),
                data = "Actions performed while the town is asleep.",
                label = "Night Phase"
            ),
            VerticalStep(
                index = 3,
                icon = Icons.Outlined.Gavel,
                color = AvatarBackground.PURPLE_ORCHID.color(),
                data = "Discuss and vote during the daylight hours.",
                label = "The Court"
            ),
            VerticalStep(
                index = 4,
                icon = Icons.Outlined.CheckCircle,
                color = AvatarBackground.ORANGE_CORAL.color(),
                data = "Final scoring and game summary.",
                label = "Conclusion"
            )
        )

        LazyColumn {
            verticalSteps(steps) { data ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = data,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

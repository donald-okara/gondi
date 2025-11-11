package ke.don.gondi

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ke.don.authentication.model.AuthState
import ke.don.authentication.model.StartupPhase
import ke.don.authentication.screens.AuthenticationScreenContent
import ke.don.design.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy
import tools.fastlane.screengrab.locale.LocaleTestRule
import java.io.File
import java.io.FileOutputStream

@RunWith(AndroidJUnit4::class)
class AppScreenshotTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val localeTestRule = LocaleTestRule()

    @Before
    fun init() {
        Screengrab
            .setDefaultScreenshotStrategy(
                UiAutomatorScreenshotStrategy()
            )
    }
//
//    @Test
//    fun simpleScreenshot() {
//        composeTestRule.activity.runOnUiThread {
//            composeTestRule.activity.setContentView(
//                ComposeView(composeTestRule.activity).apply {
//                    setContent {
//                        AppTheme {
//                            AuthenticationScreenContent(
//                                startupPhase = StartupPhase.OnBoarding,
//                                state = AuthState(),
//                                onEvent = {}
//                            )
//                        }
//                    }
//                }
//            )
//        }
//
//        // Give Compose time to settle
//        composeTestRule.waitForIdle()
//
//        composeTestRule.takeScreenshot("simpleScreenshot")
//    }
//
//
//    @Test
//    fun screenshotInCard() {
//        composeTestRule.setContent {
//            AppTheme {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(30.dp)
//                        .shadow(18.dp, RoundedCornerShape(10.dp))
//                        .clip(RoundedCornerShape(10.dp))
//                ) {
//                    App()
//                }
//            }
//        }
//        composeTestRule.takeScreenshot("screenshotInCard")
//    }
//
//    @Test
//    fun titleScreenshot() {
//        composeTestRule.setContent {
//            AppTheme {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.White)
//                ) {
//                    DecorativeCircles(top = true)
//
//                    Column(
//                        modifier = Modifier
//                            .verticalScroll(rememberScrollState())
//                            .fillMaxSize()
//                    ) {
//                        Text(
//                            text = "Gondi app",
//                            textAlign = TextAlign.Center,
//                            fontSize = 48.sp,
//                            fontWeight = FontWeight.Black,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 94.dp)
//                        )
//
//                        ScreenshotCard {
//                            App()
//                        }
//                    }
//                }
//            }
//        }
//        composeTestRule.takeScreenshot("title")
//    }
//
//    @Test
//    fun subtitleScreenshot() {
//        composeTestRule.setContent {
//            AppTheme {
//                Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(Color.White)
//                ) {
//                    DecorativeCircles(top = false)
//
//                    Column(
//                        modifier = Modifier
//                            .verticalScroll(rememberScrollState(), reverseScrolling = true)
//                            .fillMaxSize()
//                    ) {
//                        ScreenshotCard(height = 800.dp) {
//                            App()
//                        }
//
//                        Text(
//                            text = "Manage tasks",
//                            textAlign = TextAlign.Center,
//                            fontSize = 48.sp,
//                            fontWeight = FontWeight.Black,
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(vertical = 94.dp)
//                        )
//                    }
//                }
//            }
//        }
//        composeTestRule.takeScreenshot("subtitle")
//    }
//
//    @Test
//    fun themeScreenshot() {
//        composeTestRule.setContent {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.White)
//            ) {
//                DecorativeCircles(top = true)
//
//                Column(modifier = Modifier.fillMaxSize()) {
//                    Text(
//                        text = "Light or Dark",
//                        textAlign = TextAlign.Center,
//                        fontSize = 48.sp,
//                        fontWeight = FontWeight.Black,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(vertical = 94.dp)
//                    )
//
//                    val config = LocalConfiguration.current
//                    val screenWidth = config.screenWidthDp.dp
//
//                    Row(
//                        modifier = Modifier
//                            .requiredWidth(screenWidth * 2)
//                            .weight(1f)
//                    ) {
//                        ScreenshotCard(modifier = Modifier.width(screenWidth)) {
//                            AppTheme(darkTheme = false) { App() }
//                        }
//
//                        ScreenshotCard(modifier = Modifier.width(screenWidth)) {
//                            AppTheme(darkTheme = true) { App() }
//                        }
//                    }
//                }
//            }
//        }
//        composeTestRule.takeScreenshot("theme")
//    }
}

/* ---------- Helper Composables ---------- */

@Composable
private fun ScreenshotCard(
    modifier: Modifier = Modifier,
    height: Dp = 640.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .padding(horizontal = 30.dp)
            .shadow(18.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
    ) {
        content()
    }
}

@Composable
private fun BoxScope.DecorativeCircles(top: Boolean) {
    if (top) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(10.dp, 30.dp)
                .size(200.dp)
                .background(MaterialTheme.colorScheme.secondary, CircleShape)
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopStart)
                .offset(40.dp, 130.dp)
                .size(50.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        )
    } else {
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(10.dp, 30.dp)
                .size(200.dp)
                .background(MaterialTheme.colorScheme.secondary, CircleShape)
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset((-40).dp, (-130).dp)
                .size(50.dp)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        )
    }
}

/* ---------- Helper Extension ---------- */


fun ComposeContentTestRule.takeScreenshot(
    name: String,
    settleDelayMillis: Long = 150L
) {
    runBlocking {
        waitForIdle()
        delay(settleDelayMillis)

        val bitmap = onRoot().captureToImage().asAndroidBitmap()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val screenshotsDir = File(context.getExternalFilesDir(null), "screenshots")
        screenshotsDir.mkdirs()

        val file = File(screenshotsDir, "$name.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        println("📸 Screenshot saved to: ${file.absolutePath}")
    }
}

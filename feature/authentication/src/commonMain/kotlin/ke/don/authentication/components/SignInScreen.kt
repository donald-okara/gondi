/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.authentication.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ke.don.authentication.model.AuthAction
import ke.don.authentication.model.AuthState
import ke.don.components.button.ButtonToken
import ke.don.components.button.ComponentType
import ke.don.components.card.CardToken
import ke.don.components.card.CardType
import ke.don.domain.result.isLoading
import ke.don.resources.LocalSharedScope
import ke.don.resources.LocalVisibilityScope
import ke.don.resources.Resources
import ke.don.resources.Values
import ke.don.resources.isCompact
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    state: AuthState,
    onEvent: (AuthAction) -> Unit,
) {
    val sharedScope = LocalSharedScope.current
    val visibilityScope = LocalVisibilityScope.current

    CardToken(
        cardType = CardType.Outlined,
        modifier = modifier
            .padding(if (isCompact()) Values.compactScreenPadding else Values.expandedScreenPadding)
            .width(420.dp)
            .wrapContentHeight(),
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Logo
            with(sharedScope) {
                with(visibilityScope) {
                    Image(
                        painter = painterResource(Resources.Images.LOGO),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(100.dp)
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(key = "app_logo"),
                                animatedVisibilityScope = visibilityScope,
                            ),
                    )
                }
            }

            // Game Title & Tagline
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = "GONDI",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                    ),
                )

                with(sharedScope) {
                    with(visibilityScope) {
                        Text(
                            text = "Sleep tight… someone won’t wake up.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontStyle = FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .sharedElement(
                                    sharedContentState = rememberSharedContentState(key = "tag_line"),
                                    animatedVisibilityScope = visibilityScope,
                                ),
                        )
                    }
                }

                Text(
                    text = "A game of trust, deceit, and late-night drama.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                    textAlign = TextAlign.Center,
                )
            }

            // Divider for subtle separation
            HorizontalDivider(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .width(200.dp),
            )

            // Sign-in Button
            ButtonToken(
                buttonType = ComponentType.Inverse,
                loading = state.authStatus.isLoading,
                enabled = !state.authStatus.isLoading,
                onClick = {
                    onEvent(AuthAction.SignIn)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .height(52.dp),
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(Resources.Images.GOOGLE_LOGO),
                        contentDescription = "Sign in",
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Sign in with Google",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.resources

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import ke.don.domain.gameplay.Role
import org.jetbrains.compose.resources.DrawableResource

data class RoleInstruction(
    val icon: ImageVector,
    val title: String,
    val description: String,
)

data class VictoryCondition(
    val title: String,
    val description: AnnotatedString,
    val icon: DrawableResource,
    val accentColor: Color,
    val winText: String,
)

val Role.instructions: List<RoleInstruction>
    get() = when (this) {
        Role.GONDI -> listOf(
            RoleInstruction(
                Resources.Images.RoleIcons.NIGHT,
                "Strike at Night",
                "Each night, quietly pick a player to take out.",
            ),
            RoleInstruction(
                Resources.Images.RoleIcons.TEAM,
                "Stick Together",
                "Coordinate with the other Gondi to confuse, mislead, and stay one step ahead of the Villagers.",
            ),
            RoleInstruction(
                Resources.Images.RoleIcons.HIDDEN,
                "Stay Hidden",
                "Act innocent during the day and do whatever it takes to avoid getting voted out.",
            ),
        )

        Role.DOCTOR -> listOf(
            RoleInstruction(
                Resources.Images.RoleIcons.NIGHT,
                "Protect Someone",
                "Each night, choose a player to save from the Gondi’s attack.",
            ),
            RoleInstruction(
                Resources.Images.RoleIcons.SHIELD,
                "Choose Wisely",
                "You can save yourself or others, but you can’t protect the same person two nights in a row.",
            ),
        )

        Role.DETECTIVE -> listOf(
            RoleInstruction(
                Resources.Images.RoleIcons.NIGHT,
                "Investigate",
                "Each night, check one player to learn whether they are Gondi or not.",
            ),
            RoleInstruction(
                Resources.Images.RoleIcons.INFO,
                "Share Carefully",
                "Use what you learn to help the Villagers, but don’t expose yourself too early.",
            ),
            RoleInstruction(
                Resources.Images.RoleIcons.VOTE_GUILTY,
                "Push the Vote",
                "When you’re confident, guide the vote toward a confirmed Gondi.",
            ),
        )

        Role.ACCOMPLICE -> listOf(
            RoleInstruction(
                Resources.Images.RoleIcons.SHIELD,
                "Perfect Cover",
                "You appear as a Villager to the Detective, helping shield the Gondi.",
            ),
            RoleInstruction(
                Resources.Images.RoleIcons.TEAM,
                "Create Chaos",
                "Your job is to make sure at least one Gondi survives—misdirect, distract, and stir doubt.",
            ),
            RoleInstruction(
                Resources.Images.RoleIcons.VOTE_GUILTY,
                "Take the Fall",
                "If needed, draw suspicion onto yourself to keep a Gondi alive.",
            ),
        )

        Role.VILLAGER -> listOf(
            RoleInstruction(
                Resources.Images.RoleIcons.DAY,
                "Talk It Out",
                "During the day, discuss what you’ve noticed and piece together the truth.",
            ),
            RoleInstruction(
                Resources.Images.RoleIcons.VOTE_GUILTY,
                "Vote Smart",
                "Eliminate the player you believe is most likely a Gondi.",
            ),
            RoleInstruction(
                Resources.Images.RoleIcons.TEAM,
                "Win Together",
                "Trust each other, stay alert, and eliminate all Gondi before they outnumber you.",
            ),
        )

        Role.MODERATOR -> listOf(
            RoleInstruction(
                Resources.Images.RoleIcons.GUIDE,
                "Run the Game",
                "Lead players through the night and day phases.",
            ),
            RoleInstruction(
                Resources.Images.RoleIcons.ANNOUNCE,
                "Keep Everyone Informed",
                "Announce what happens each night and the results of every vote.",
            ),
            RoleInstruction(
                Resources.Images.RoleIcons.NEUTRAL,
                "Stay Neutral",
                "Enforce the rules and keep the game fair and flowing.",
            ),
        )
    }

val Role.description: String
    get() = when (this) {
        Role.GONDI -> "Know each other. Kill one Villager every night."
        Role.DOCTOR -> "Saves one player from death each night."
        Role.DETECTIVE -> "Checks one player's role each night."
        Role.ACCOMPLICE -> "Allies with Gondis."
        Role.VILLAGER -> "No special powers. Must use logic to survive."
        Role.MODERATOR -> "Moderate the game."
    }

val Role.icon: DrawableResource
    get() = when (this) {
        Role.GONDI -> Resources.Images.LOGO
        Role.DOCTOR -> Resources.Images.RoleIcons.DOCTOR
        Role.DETECTIVE -> Resources.Images.RoleIcons.DETECTIVE
        Role.ACCOMPLICE -> Resources.Images.RoleIcons.ACCOMPLICE
        Role.VILLAGER -> Resources.Images.RoleIcons.VILLAGER
        Role.MODERATOR -> Resources.Images.RoleIcons.MODERATOR
    }

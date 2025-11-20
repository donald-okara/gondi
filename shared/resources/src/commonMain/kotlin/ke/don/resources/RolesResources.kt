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

import androidx.compose.ui.graphics.vector.ImageVector
import ke.don.domain.gameplay.Role
import org.jetbrains.compose.resources.DrawableResource

data class RoleInstruction(
    val icon: ImageVector,
    val title: String,
    val description: String,
)

val Role.instructions: List<RoleInstruction>
    get() = when (this) {
        Role.GONDI -> listOf(
            RoleInstruction(Resources.Images.RoleIcons.NIGHT, "Night Action", "Each night, choose a player to eliminate."),
            RoleInstruction(Resources.Images.RoleIcons.TEAM, "Teamwork", "Work with fellow Gondi to deceive and outmaneuver the Villagers."),
            RoleInstruction(Resources.Images.RoleIcons.VOTE_GUILTY, "Survival", "Blend in during the day and avoid being voted out."),
        )
        Role.DOCTOR -> listOf(
            RoleInstruction(Resources.Images.RoleIcons.NIGHT, "Night Action", "Each night, choose one player to protect from the Gondi attack."),
            RoleInstruction(Resources.Images.RoleIcons.SHIELD, "Healer", "You can save yourself and others, but not the same person consecutive nights."),
        )
        Role.DETECTIVE -> listOf(
            RoleInstruction(Resources.Images.RoleIcons.NIGHT, "Night Action", "Each night, choose a player to investigate and learn their alignment (Gondi or not)."),
            RoleInstruction(Resources.Images.RoleIcons.INFO, "Share Intel", "Carefully share your findings to guide the Villagers without revealing your identity too soon."),
            RoleInstruction(Resources.Images.RoleIcons.VOTE_GUILTY, "Lead the Charge", "Use your evidence to lead the vote against a confirmed Gondi."),
        )
        Role.ACCOMPLICE -> listOf(
            RoleInstruction(Resources.Images.RoleIcons.SHIELD, "Deception", "Appear as a Villager to the Detective to protect the Gondi."),
            RoleInstruction(Resources.Images.RoleIcons.TEAM, "Support", "Your goal is to ensure at least one Gondi survives. Mislead, distract, and sow chaos."),
            RoleInstruction(Resources.Images.RoleIcons.VOTE_GUILTY, "Sacrifice", "You may need to sacrifice yourself to save a Gondi."),
        )
        Role.VILLAGER -> listOf(
            RoleInstruction(Resources.Images.RoleIcons.DAY, "Day Discussion", "Participate in discussions to analyze behavior and find clues."),
            RoleInstruction(Resources.Images.RoleIcons.VOTE_GUILTY, "Eliminate Suspects", "Vote to eliminate the player you most suspect of being a Gondi."),
            RoleInstruction(Resources.Images.RoleIcons.TEAM, "Trust & Survive", "Work together with other Villagers to outnumber and eliminate all Gondi."),
        )
        Role.MODERATOR -> listOf(
            RoleInstruction(Resources.Images.RoleIcons.GUIDE, "Facilitate", "Guide players through the day and night phases."),
            RoleInstruction(Resources.Images.RoleIcons.ANNOUNCE, "Announce", "Announce the results of night actions and day votes."),
            RoleInstruction(Resources.Images.RoleIcons.NEUTRAL, "Enforce Rules", "Remain a neutral party and ensure the game flows smoothly."),
        )
    }

val Role.description: String
    get() = when (this) {
        Role.GONDI -> "The antagonists. Their goal is to eliminate Villagers each night without being discovered. They know who the other Gondi members are."
        Role.DOCTOR -> "A special Villager. Each night, the Doctor can choose one player to save. The chosen player will survive if targeted by the Gondi. They cannot save the same player twice in a row."
        Role.DETECTIVE -> "A special Villager. Each night, the Detective can choose one player to investigate and learn their role (Gondi or not Gondi)."
        Role.ACCOMPLICE -> "Runs interference. They throw everyone off the real Gondi's trail (Can only be in the game if there is a detective). They win if at least one gondi is left standing."
        Role.VILLAGER -> "The protagonists. They must deduce who the Gondi are and vote to eliminate them during the day. Most players are Villagers."
        Role.MODERATOR -> "The game facilitator. They do not participate as a player but guide the game, announce events, and ensure rules are followed."
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

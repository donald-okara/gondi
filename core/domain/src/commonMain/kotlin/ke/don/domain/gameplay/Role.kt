/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.domain.gameplay

import kotlinx.serialization.Serializable

@Serializable
enum class Role(
    val faction: Faction,
    val canActInSleep: Boolean = false,
    val canAccuse: Boolean = true,
    val canVote: Boolean = true,
) {
    GONDI(
        faction = Faction.GONDI,
        canActInSleep = true,
    ),
    DOCTOR(
        faction = Faction.VILLAGER,
        canActInSleep = true,
    ),
    DETECTIVE(
        faction = Faction.VILLAGER,
        canActInSleep = true,
    ),
    ACCOMPLICE(
        faction = Faction.GONDI,
    ),
    VILLAGER(
        faction = Faction.VILLAGER,
    ),
    MODERATOR(
        faction = Faction.NEUTRAL,
        canAccuse = false,
        canVote = false,
    ),
}

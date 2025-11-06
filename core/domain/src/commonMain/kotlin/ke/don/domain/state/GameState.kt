/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.domain.state

import ke.don.domain.gameplay.Faction
import ke.don.domain.gameplay.PlayerAction
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GameState(
    val id: String,
    val phase: GamePhase,
    val round: Long,
    val winners: Faction? = null,
    @SerialName("pending_kills") val pendingKills: List<String> = emptyList(),
    @SerialName("last_saved_player_id") val lastSavedPlayerId: String? = null,
    @SerialName("accused_player_id") val accusedPlayer: PlayerAction? = null, //Accuser to accused
    @SerialName("second") val second: PlayerAction? = null, //Accuser to accused
    @SerialName("reveal_eliminated_player") val revealEliminatedPlayer: Boolean,
)

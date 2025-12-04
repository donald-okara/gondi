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

import ke.don.domain.gameplay.ActionType
import ke.don.domain.gameplay.Faction
import ke.don.domain.gameplay.PlayerAction
import ke.don.domain.gameplay.Role
import ke.don.domain.gameplay.SelectedPlayer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
data class GameState
@OptIn(ExperimentalUuidApi::class)
constructor(
    val id: String = Uuid.random().toString(),
    val name: String = "",
    val phase: GamePhase = GamePhase.LOBBY,
    val round: Long = 0L,
    val availableSlots: Long = 0L,
    val winners: Faction? = null,
    @SerialName("lock_join") val lockJoin: Boolean = false,
    @SerialName("pending_kills") val pendingKills: List<String> = emptyList(),
    @SerialName("last_saved_player_id") val lastSavedPlayerId: String? = null,
    @SerialName("accused_player_id") val accusedPlayer: PlayerAction? = null, // Accuser to accused
    @SerialName("second") val second: PlayerAction? = null, // Accuser to accused
    @SerialName("reveal_eliminated_player") val revealEliminatedPlayer: Boolean = false,
) {
    /**
     * These are players who have been selected to be killed or saved by Gondis and the Doctor. It is usable in the Moderator's sleep view
     */
    fun selectedPlayersSleep(player: Player): List<SelectedPlayer> {
        val selectedPlayers = when (player.role) {
            Role.GONDI -> {
                this.pendingKills.map { playerId ->
                    SelectedPlayer(playerId, ActionType.KILL)
                }
            }

            Role.DOCTOR -> {
                this.lastSavedPlayerId?.let {
                    listOf(SelectedPlayer(it, ActionType.SAVE))
                } ?: emptyList()
            }

            Role.DETECTIVE -> {
                val lastInvestigated =
                    player.knownIdentities.find { identity -> identity.round == this.round }
                lastInvestigated?.let {
                    listOf(SelectedPlayer(it.playerId, ActionType.INVESTIGATE))
                } ?: emptyList()
            }

            else -> emptyList()
        }

        return selectedPlayers
    }

    /**
     * These are players who have been selected to be killed, investigated or saved by the Gondis, Detective and doctor. It is usable in the Player's sleep view
     */
    fun selectedPlayersSleep(): List<SelectedPlayer> {
        val lastSaved = this.lastSavedPlayerId to ActionType.SAVE
        val pendingKills = this.pendingKills.map { it to ActionType.KILL }

        val selectedPlayers =
            buildList {
                lastSaved.first?.let {
                    add(SelectedPlayer(it, lastSaved.second))
                }
                addAll(pendingKills)
            }

        return selectedPlayers
    }
}

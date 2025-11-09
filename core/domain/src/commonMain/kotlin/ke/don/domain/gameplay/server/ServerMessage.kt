/*
 * Copyright © 2025 Donald O. Isoe (isoedonald@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 */
package ke.don.domain.gameplay.server

import ke.don.domain.gameplay.ModeratorCommand
import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import kotlinx.serialization.Serializable

@Serializable
sealed class ServerMessage {
    @Serializable data class PlayerIntentMsg(val intent: PlayerIntent) : ServerMessage()

    @Serializable data class ModeratorCommandMsg(val gameId: String, val command: ModeratorCommand) : ServerMessage()

    @Serializable object GetGameState : ServerMessage()
}

@Serializable
sealed class ClientMessage {
    data class Intent(val intent: PlayerIntent) : ClientMessage()
    object Ping : ClientMessage()
}

@Serializable
sealed class ServerUpdate {
    /** Sent when the full or partial game state changes. */
    @Serializable
    data class GameStateSnapshot(val state: GameState?) : ServerUpdate()

    /** Sent when the list of players changes (e.g. kill, add, revive). */
    @Serializable
    data class PlayersSnapshot(val players: List<Player>) : ServerUpdate()

    /** Sent when the votes table changes. */
    @Serializable
    data class VotesSnapshot(val votes: List<Vote>) : ServerUpdate()

    /** Sent for general info (phase change, winner declared, etc.). */
    @Serializable
    data class Announcement(val message: String) : ServerUpdate()

    /** Sent for invalid intents or errors. */
    @Serializable
    data class Error(val message: String) : ServerUpdate()
}

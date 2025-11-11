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

import ke.don.domain.gameplay.PlayerIntent
import ke.don.domain.state.GameState
import ke.don.domain.state.Player
import ke.don.domain.state.Vote
import kotlinx.serialization.Serializable

@Serializable
sealed class ClientUpdate {
    /**
     * A message sent from the client to the server representing a player's intended action.
     * This is the primary way a player interacts with the game, such as voting, using abilities, etc.
     *
     * @property intent The specific action the player wants to perform.
     */
    @Serializable data class PlayerIntentMsg(val intent: PlayerIntent) : ClientUpdate()

    /**
     * A message from a client requesting the full current [GameState].
     * The server should respond with a [ServerUpdate.GameStateSnapshot].
     */
    @Serializable object GetGameState : ClientUpdate()

    /**
     * A keep-alive message sent from the client to the server to maintain the connection
     * and measure latency. The server may respond with a [ServerUpdate.LastPing].
     */
    @Serializable object Ping : ClientUpdate()
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

    @Serializable
    data class LastPing(val long: Long) : ServerUpdate()

    /** Sent for errors. */
    @Serializable
    data class Error(val message: String) : ServerUpdate()

    /**
     * A server message indicating that the requested action is not allowed.
     */
    @Serializable
    data class Forbidden(val message: String) : ServerUpdate()
}

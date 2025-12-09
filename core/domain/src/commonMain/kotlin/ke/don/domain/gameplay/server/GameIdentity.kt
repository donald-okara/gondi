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

import ke.don.domain.table.Avatar
import ke.don.domain.table.AvatarBackground

/**
 * Represents the unique identity of a game server instance on the network.
 * This information is typically broadcasted for discovery by clients.
 *
 * @property id A unique identifier for this specific game instance.
 * @property version The version of the game server software.
 * @property serviceHost The network host address (IP or hostname) of the server. Defaults to an empty string, often resolved later.
 * @property serviceType The type of service being advertised, used for network service discovery.
 * @property servicePort The port number on which the game service is running.
 * @property gameName The user-friendly name of the game being hosted.
 * @property moderatorName The name of the player who is moderating or hosting the game.
 * @property moderatorAvatar An optional avatar representing the game moderator.
 * @property moderatorAvatarBackground The background style or color for the moderator's avatar.
 */
data class GameIdentity(
    val id: String,
    val version: String,
    val serviceHost: String = "",
    val serviceType: String = SERVICE_TYPE,
    val servicePort: Int = 8080,
    val gameName: String,
    val moderatorName: String,
    val moderatorAvatar: Avatar? = null,
    val moderatorAvatarBackground: AvatarBackground,
)

/**
 * Enum representing the compatibility level between two semantic versions.
 */
enum class VersionCompatibility {
    /** The versions are fully compatible (major, minor, and patch are the same). */
    COMPATIBLE,

    /** The versions are partially compatible (major versions match, but minor or patch may differ). */
    PARTIALLY_COMPATIBLE,

    /** The versions are not compatible (major versions differ). */
    INCOMPATIBLE,

    /** One or both of the version strings are malformed. */
    INVALID
}


/**
 * Compares this semantic version string (requester) with another (requested) to check for compatibility.
 *
 * This function assumes a standard "x.y.z" format. It does not handle pre-release tags or build metadata.
 *
 * @param requestedVersion The version string to compare against (e.g., "1.2.3").
 * @return [VersionCompatibility] indicating the level of compatibility.
 *         - [COMPATIBLE]: If major, minor, and patch versions all match (e.g., "1.2.3" vs "1.2.3").
 *         - [PARTIALLY_COMPATIBLE]: If only the major version matches (e.g., "1.2.3" vs "1.3.0").
 *         - [INCOMPATIBLE]: If the major versions do not match (e.g., "1.2.3" vs "2.0.0").
 *         - [INVALID]: If either version string is not in a valid "x.y.z" format.
 */
fun String.checkSemanticVersionCompatibility(requestedVersion: String): VersionCompatibility {
    // Strip pre-release tags (like -beta, -rc1) before splitting
    val requesterCleaned = this.split('-').first()
    val requestedCleaned = requestedVersion.split('-').first()

    val requesterParts = requesterCleaned.split('.').mapNotNull { it.toIntOrNull() }
    val requestedParts = requestedCleaned.split('.').mapNotNull { it.toIntOrNull() }

    // A valid semver must have at least 3 parts (major, minor, patch)
    if (requesterParts.size < 3 || requestedParts.size < 3) {
        return VersionCompatibility.INVALID
    }

    val (reqMajor, reqMinor, reqPatch) = requesterParts
    val (resMajor, resMinor, resPatch) = requestedParts

    return when {
        // Incompatible: Major versions differ
        reqMajor != resMajor -> VersionCompatibility.INCOMPATIBLE

        // Fully Compatible: Major, Minor, and Patch are identical
        reqMinor == resMinor && reqPatch == resPatch -> VersionCompatibility.COMPATIBLE

        // Partially Compatible: Major is the same, but minor and/or patch differ
        else -> VersionCompatibility.PARTIALLY_COMPATIBLE
    }
}


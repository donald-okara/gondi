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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.FollowTheSigns
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.ModeNight
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Sports
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.outlined.WbSunny
import gondi.shared.resources.generated.resources.Res
import gondi.shared.resources.generated.resources.accomplice
import gondi.shared.resources.generated.resources.app_icon
import gondi.shared.resources.generated.resources.dead
import gondi.shared.resources.generated.resources.detective
import gondi.shared.resources.generated.resources.doctor
import gondi.shared.resources.generated.resources.google_logo
import gondi.shared.resources.generated.resources.investigate
import gondi.shared.resources.generated.resources.moderator
import gondi.shared.resources.generated.resources.point
import gondi.shared.resources.generated.resources.roboto_regular
import gondi.shared.resources.generated.resources.roboto_semibold
import gondi.shared.resources.generated.resources.s24_frame
import gondi.shared.resources.generated.resources.shield
import gondi.shared.resources.generated.resources.thumbs_down
import gondi.shared.resources.generated.resources.thumbs_up
import gondi.shared.resources.generated.resources.villager

object Resources {
    object Font {
        val REGULAR = Res.font.roboto_regular
        val SEMIBOLD = Res.font.roboto_semibold
    }

    object Images {
        val LOGO = Res.drawable.app_icon
        val GOOGLE_LOGO = Res.drawable.google_logo
        val MOBILE_FRAME = Res.drawable.s24_frame

        object RoleIcons {
            val ACCOMPLICE = Res.drawable.accomplice
            val DOCTOR = Res.drawable.doctor
            val DETECTIVE = Res.drawable.detective
            val MODERATOR = Res.drawable.moderator
            val VILLAGER = Res.drawable.villager

            val SHIELD = Icons.Outlined.Shield
            val DAY = Icons.Outlined.WbSunny
            val NIGHT = Icons.Outlined.ModeNight
            val TEAM = Icons.Outlined.Group
            val VOTE_GUILTY = Icons.Outlined.ThumbDown
            val VOTE_INNOCENT = Icons.Outlined.ThumbUp
            val INFO = Icons.Outlined.Search
            val SELF = Icons.Outlined.Person
            val GUIDE = Icons.AutoMirrored.Outlined.FollowTheSigns
            val ANNOUNCE = Icons.Outlined.Campaign
            val NEUTRAL = Icons.Outlined.Sports
        }

        object ActionIcons {
            val DEAD = Res.drawable.dead
            val ACCUSE = Res.drawable.point
            val INVESTIGATE = Res.drawable.investigate
            val SAVE = Res.drawable.shield
            val VOTE_INNOCENT = Res.drawable.thumbs_up
            val VOTE_GUILTY = Res.drawable.thumbs_down
        }
    }
}

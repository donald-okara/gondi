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
import gondi.shared.resources.generated.resources.*

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

    object Strings {

        object Authentication {
            val APP_NAME = Res.string.app_name
            val TAG_LINE = Res.string.tag_line
            val GAME_DESCRIPTION = Res.string.game_description
            val SIGN_IN_WITH_GOOGLE = Res.string.sign_in_with_google

            val HAVE_A_COFFEE = Res.string.have_a_coffee
        }

        object GamePlay {
            val PLAYER_ROLES_CONFIGURATION = Res.string.player_roles_configuration
            val SET_NUMBER_OF_PLAYERS_FOR_EACH_ROLE = Res.string.set_number_of_players_for_each_role
            fun detectiveAndAccompliceWarning(limit: Int) = "Detective and Accomplice cannot exist in a game with less than $limit players"
            val ONLY_ONE_DETECTIVE_OR_ACCOMPLICE_ALLOWED = Res.string.only_one_detective_or_accomplice_allowed
            fun maxAllowed(max: Int) = "Max allowed: $max"
            val ALREADY_VOTED_THIS_ROUND = Res.string.already_voted_this_round
            fun condemnPlayer(name: String) = "Do you really want to condemn $name?"
            fun playerIsInnocent(name: String) = "So, you believe $name is innocent?"
            val VOTE_INNOCENT = Res.string.vote_innocent
            val VOTE_GUILTY = Res.string.vote_guilty
            val DEAD_MEN_TELL_NO_TALES = Res.string.dead_men_tell_no_tales

            val CONFIRMATION_GONDI = Res.string.confirmation_gondi
            val CONFIRMATION_DOCTOR = Res.string.confirmation_doctor
            val CONFIRMATION_DETECTIVE = Res.string.confirmation_detective
            val DORMANT_TEXT_GONDI = Res.string.dormant_text_gondi
            val DORMANT_TEXT_DOCTOR = Res.string.dormant_text_doctor
            val DORMANT_TEXT_DETECTIVE = Res.string.dormant_text_detective
            val DORMANT_TEXT_DEFAULT = Res.string.dormant_text_default

            val ALREADY_ON_TRIAL = Res.string.already_on_trial
            fun accusePlayerConfirmation(name: String) = "Accuse $name and put them on trial? Are you sure?"

            val GONDI_INSTRUCTION = Res.string.gondi_instruction
            val DOCTOR_INSTRUCTION = Res.string.doctor_instruction
            val DETECTIVE_INSTRUCTION = Res.string.detective_instruction
            val DEFAULT_INSTRUCTION = Res.string.default_instruction

            val READY_TO_BEGIN = Res.string.ready_to_begin
            fun startWithPlayers(count: Int) = "Start with $count players"
            val START_GAME = Res.string.start_game
            val WAITING_FOR_MORE_PLAYERS = Res.string.waiting_for_more_players
            val ROLE_LOCK_WARNING = Res.string.role_lock_warning
            val MODERATOR_PANEL = Res.string.moderator_panel
            val SHOW_RULES = Res.string.show_rules
            val PLAYERS_IN_LOBBY = Res.string.players_in_lobby

            val SESSION_OVER = Res.string.session_over
            val PROCEED = Res.string.proceed
            val GO_TO_COURT = Res.string.go_to_court
            fun noSeconder(name: String) = "$name has no seconder. Would you like to exonerate them?"
            fun exoneratePlayer(name: String) = "Exonerate $name"
            val NO_ACCUSATIONS = Res.string.no_accusations
            fun isPlayerGuilty(name: String) = "Do you think $name is guilty?"
            val VOTE = Res.string.vote

            val PLAY_AGAIN = Res.string.play_again
            val GONDIS_WIN = Res.string.gondis_win
            val VILLAGERS_WIN = Res.string.villagers_win
            val GONDI_WIN_REMARK = Res.string.gondi_win_remark
            val VILLAGER_WIN_REMARK = Res.string.villager_win_remark

            val ASSIGN_ROLE = Res.string.assign_role
            val REMOVE = Res.string.remove
            fun removePlayerConfirmation(name: String) = "Are you sure you want to remove $name from the game?"
            val NEVER_MIND = Res.string.never_mind
            val I_AM_SURE = Res.string.i_am_sure
            val ASSIGN_ROLE_WARNING = Res.string.assign_role_warning
            val NONE = Res.string.none

            val GAME_OBJECTIVE = Res.string.game_objective
            val GAME_OBJECTIVE_DESCRIPTION = Res.string.game_objective_description

            val NIGHT_RESULTS = Res.string.night_results
            val COURT_RULING = Res.string.court_ruling
            val NIGHT_RESULTS_DESCRIPTION = Res.string.night_results_description
            val COURT_RULING_DESCRIPTION = Res.string.court_ruling_description
            val KILLED_PLAYER = Res.string.killed_player
            val SAVED_PLAYER = Res.string.saved_player
            fun eliminatedPlayer(role: String) = "Eliminated — $role"
            fun savedBySaviour(saviour: String) = "Saved by $saviour"
            val THE_DOCTOR = Res.string.the_doctor

            fun secondsAccusation(name: String) = "$name seconds the accusation"
            val WAITING_FOR_SECOND = Res.string.waiting_for_second
            fun accuses(accuser: String, accused: String) = "$accuser accuses $accused"
            val SECOND_THE_ACCUSATION = Res.string.second_the_accusation
        }
    }
}

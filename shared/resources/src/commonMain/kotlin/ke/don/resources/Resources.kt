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
import androidx.compose.material.icons.outlined.Masks
import androidx.compose.material.icons.outlined.ModeNight
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Sports
import androidx.compose.material.icons.outlined.TheaterComedy
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material.icons.outlined.WbSunny
import gondi.shared.resources.generated.resources.*
import gondi.shared.resources.generated.resources.Res

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
            val HIDDEN = Icons.Outlined.TheaterComedy
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
            fun accusePlayerConfirmation(name: String) = "Accuse $name and put them on trial? Are you sure?"
            fun accuses(accuser: String, accused: String) = "$accuser accuses $accused"
            val ALREADY_ON_TRIAL = Res.string.already_on_trial
            val ALREADY_VOTED_THIS_ROUND = Res.string.already_voted_this_round
            val ASSIGN_ROLE = Res.string.assign_role
            val ASSIGN_ROLE_WARNING = Res.string.assign_role_warning

            fun condemnPlayer(name: String) = "Do you really want to condemn $name?"
            val CONFIRMATION_DETECTIVE = Res.string.confirmation_detective
            val CONFIRMATION_DOCTOR = Res.string.confirmation_doctor
            val CONFIRMATION_GONDI = Res.string.confirmation_gondi
            val CONNECTING = Res.string.connecting
            val COURT_RULING = Res.string.court_ruling
            val COURT_RULING_DESCRIPTION = Res.string.court_ruling_description

            val DEAD_MEN_TELL_NO_TALES = Res.string.dead_men_tell_no_tales
            val DEFAULT_INSTRUCTION = Res.string.default_instruction
            fun detectiveAndAccompliceWarning(limit: Int) = "Detective and Accomplice cannot exist in a game with less than $limit players"
            val DETECTIVE_INSTRUCTION = Res.string.detective_instruction
            val DOCTOR_INSTRUCTION = Res.string.doctor_instruction
            val DORMANT_TEXT_DEFAULT = Res.string.dormant_text_default
            val DORMANT_TEXT_DETECTIVE = Res.string.dormant_text_detective
            val DORMANT_TEXT_DOCTOR = Res.string.dormant_text_doctor
            val DORMANT_TEXT_GONDI = Res.string.dormant_text_gondi

            fun eliminatedPlayer(role: String) = "Eliminated — $role"
            fun exoneratePlayer(name: String) = "Exonerate $name"

            fun gameDoesntExist(error: String) = "$error. The game most likely doesn't exist anymore."
            val GAME_OBJECTIVE = Res.string.game_objective
            val GAME_OBJECTIVE_DESCRIPTION = Res.string.game_objective_description
            val GONDI_INSTRUCTION = Res.string.gondi_instruction
            val GONDI_WIN_REMARK = Res.string.gondi_win_remark
            val GONDIS_WIN = Res.string.gondis_win
            val GO_TO_COURT = Res.string.go_to_court

            val I_AM_SURE = Res.string.i_am_sure
            fun isPlayerGuilty(name: String) = "Do you think $name is guilty?"

            val KILLED_PLAYER = Res.string.killed_player

            val LEAVE_GAME = Res.string.leave_game
            val LEAVE_GAME_CHECKLIST_1 = Res.string.leave_game_checklist_1
            val LEAVE_GAME_CHECKLIST_2 = Res.string.leave_game_checklist_2
            val LEAVE_GAME_CHECKLIST_MODERATOR = Res.string.leave_game_checklist_moderator
            val LEAVE_GAME_MESSAGE = Res.string.leave_game_message
            val LEAVE_GAME_TITLE = Res.string.leave_game_title

            fun maxAllowed(max: Int) = "Max allowed: $max"
            val MODERATOR_PANEL = Res.string.moderator_panel

            val NEVER_MIND = Res.string.never_mind
            val NEW_GAME = Res.string.new_game
            val NIGHT_RESULTS = Res.string.night_results
            val NIGHT_RESULTS_DESCRIPTION = Res.string.night_results_description
            val NO_ACCUSATIONS = Res.string.no_accusations
            fun noSeconder(name: String) = "$name has no seconder. Would you like to exonerate them?"
            val NONE = Res.string.none

            val ONLY_ONE_DETECTIVE_OR_ACCOMPLICE_ALLOWED = Res.string.only_one_detective_or_accomplice_allowed

            val PLAY_AGAIN = Res.string.play_again
            fun playerIsInnocent(name: String) = "So, you believe $name is innocent?"
            val PLAYER_ROLES_CONFIGURATION = Res.string.player_roles_configuration
            val PLAYERS_IN_LOBBY = Res.string.players_in_lobby
            val PROCEED = Res.string.proceed
            val PROCEED_TO_TOWN_HALL = Res.string.proceed_to_town_hall

            val READY_TO_BEGIN = Res.string.ready_to_begin
            val REMOVE = Res.string.remove
            fun removePlayerConfirmation(name: String) = "Are you sure you want to remove $name from the game?"
            val ROLE_LOCK_WARNING = Res.string.role_lock_warning

            fun savedBySaviour(saviour: String) = "Saved by $saviour"
            val SAVED_PLAYER = Res.string.saved_player
            val SECOND_THE_ACCUSATION = Res.string.second_the_accusation
            fun secondsAccusation(name: String) = "$name seconds the accusation"
            val SESSION_OVER = Res.string.session_over
            val SET_NUMBER_OF_PLAYERS_FOR_EACH_ROLE = Res.string.set_number_of_players_for_each_role
            val SHOW_RULES = Res.string.show_rules
            val SOMEONE_HAS_NOT_DONE_THEIR_PART_YET = Res.string.someone_has_not_done_their_part_yet
            val SOMETHING_WENT_WRONG = Res.string.something_went_wrong
            val START_GAME = Res.string.start_game
            fun startWithPlayers(count: Int) = "Start with $count players"

            val THE_DOCTOR = Res.string.the_doctor

            val VICTORY_CONDITIONS = Res.string.victory_conditions
            val VILLAGERS_WIN = Res.string.villagers_win
            val VILLAGER_WIN_REMARK = Res.string.villager_win_remark
            val VOTE = Res.string.vote
            val VOTE_GUILTY = Res.string.vote_guilty
            val VOTE_INNOCENT = Res.string.vote_innocent

            val WAITING_FOR_MORE_PLAYERS = Res.string.waiting_for_more_players
            val WAITING_FOR_SECOND = Res.string.waiting_for_second
        }

        object Guide {
            val UNINFORMED_MAJORITY = Res.string.uninformed_majority
            val VILLAGERS_VICTORY_DESCRIPTION = Res.string.villagers_victory_description
            val ELIMINATE_ALL_GONDI = Res.string.eliminate_all_gondi
            val INFORMED_MINORITY = Res.string.informed_minority
            val GONDI_VICTORY_DESCRIPTION = Res.string.gondi_victory_description
            val EQUAL_NUMBERS = Res.string.equal_numbers

            val GAME_PHASES = Res.string.game_phases
            val PHASE_1 = Res.string.phase_1
            val THE_SILENT_NIGHT = Res.string.the_silent_night
            val EVERYONE_CLOSE_YOUR_EYES = Res.string.everyone_close_your_eyes
            val GONDI_AWAKENING = Res.string.gondi_awakening
            val GONDI_AWAKENING_DESCRIPTION = Res.string.gondi_awakening_description
            val THE_DOCTORS_VIGIL = Res.string.the_doctors_vigil
            val THE_DOCTORS_VIGIL_DESCRIPTION = Res.string.the_doctors_vigil_description
            val THE_INVESTIGATION = Res.string.the_investigation
            val THE_INVESTIGATION_DESCRIPTION = Res.string.the_investigation_description
            val PHASE_2 = Res.string.phase_2
            val THE_COURT_DAY = Res.string.the_court_day
            val COURT_DAY_DESCRIPTION = Res.string.court_day_description
            val ACCUSE_TITLE = Res.string.accuse_title
            val ACCUSE_DESCRIPTION = Res.string.accuse_description
            val SECOND_TITLE = Res.string.second_title
            val SECOND_DESCRIPTION = Res.string.second_description
            val VOTE_TITLE = Res.string.vote_title
            val VOTE_DESCRIPTION = Res.string.vote_description
            val PRO_TIP = Res.string.pro_tip
            val PRO_TIP_DESCRIPTION = Res.string.pro_tip_description

            val CODE_OF_CONDUCT = Res.string.code_of_conduct
            val CODE_OF_CONDUCT_DESCRIPTION = Res.string.code_of_conduct_description
            val NO_PEEKING = Res.string.no_peeking
            val NO_PEEKING_DESCRIPTION = Res.string.no_peeking_description
            val KEEP_IT_SECRET = Res.string.keep_it_secret
            val KEEP_IT_SECRET_DESCRIPTION = Res.string.keep_it_secret_description
            val PLAY_NICE = Res.string.play_nice
            val PLAY_NICE_DESCRIPTION = Res.string.play_nice_description

            val THE_GONDIS = Res.string.the_gondis
            val THE_VILLAGERS = Res.string.the_villagers
        }
    }
}

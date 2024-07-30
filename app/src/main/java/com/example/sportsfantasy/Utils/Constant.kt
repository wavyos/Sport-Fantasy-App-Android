package com.example.sportsfantasy.Utils

object Constant {
    const val REQUEST_STATUS_CODE_SUCCESS = 200
    const val REQUEST_STATUS_CODE_FAILED = 400
    const val REQUEST_STATUS_CODE_UNPROCESSABLE  = 422


    object WEB_SERVICES
    {
        const val WS_LOGIN_USER = "api/v1/login"
        const val WS_REGISTER_USER = "api/v1/register"
        const val WS_LOGOUT = "api/v1/logout"
        const val WS_LEAGUE_FIELD = "api/v1/get-leage-fields"

        const val WS_CREATE_LEAGUE = "api/v1/create-league"

        const val WS_LEAGUE_LIST = "api/v1/user-league-list/{id}"

        const val WS_FORGOTPASS = "api/v1/forgot-password"

        const val WS_JOIN_LEAGUE_WITH_CODE = "api/v1/join-league-with-code"

        const val WS_POSITION_LIST_IN_LEAGUE = "api/v1/position-list/{login_user_id}/{league_id}"

        const val WS_TOP_TEN_PLAYER_LIST = "api/v1/ten-position-wise-list/{postioncode}/{filter_code}/{league_start_date}/{code}"

        const val WS_PLAYER_PROFILE = "api/v1/player-profile/{pid}"

        const val WS_SEARCH_PLAYER = "api/v1/search-player-with-position/{searchText}/{postioncode}/{filter_code}/{league_start_date}/{code}"
        //Leaguage Start Date

        const val WS_ADD_TO_DRAFT = "api/v1/draft-player"

        const val WS_REMOVE_TO_DRAFT = "api/v1/draft-player-remove/{draft_id}"

        const val WS_AUTO_DRAFT = "api/v1/auto-draft/{login_user_id}/{league_id}"

        const val WS_ROSTER_DATA = "api/v1/roster-data"

        const val WS_MATCH_UP_DATA = "api/v1/match-up"
        const val WS_PLAY_OFF_DATA = "api/v1/playoff-match-up"
        const val WS_PLAYER_ALL = "api/v1/player-tab"
        const val WS_SEARCH_PLAYER_TAB = "api/v1/search-player-tab"
        const val WS_LEAGUE_SCORECARD = "api/v1/scoreboard-data"
        const val WS_STANDING_LIST = "api/v1/standing-data"
        const val WS_STANDING_GENERATE_CHANNEL_LIST = "api/v1/generate-channels"
        const val WS_GET_MESSAGE_LIST = "api/v1/get-message"
        const val WS_SEND_MESSAGE_LIST = "api/v1/send-message"
        const val WS_ADD_MANAGER = "api/v1/add-manager"
        const val WS_REMOVE_MANAGER = "api/v1/remove-manager"
        const val WS_LEAGUE_TEAMS = "api/v1/league-teams"
        const val WS_UPDATE_LEAGUE = "api/v1/update-league"

        const val UPDATE_DEVICE_TOKEN = "api/v1/updatedevicetoken"
    }

    enum class PLAYER_TYPE{
        star,
        starter,
        small
    }
}
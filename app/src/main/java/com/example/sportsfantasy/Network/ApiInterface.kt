package com.example.sportsfantasy.Network

import com.example.sportsfantasy.Model.PlayerF.playerTabModel
import com.example.sportsfantasy.Model.PlayerF.playerTabResponse
import com.example.sportsfantasy.Model.*
import com.example.sportsfantasy.Model.LeagueTeam.Manager.addManager
import com.example.sportsfantasy.Model.LeagueTeam.Manager.managerResponse
import com.example.sportsfantasy.Model.LeagueTeam.leagueTeamModel
import com.example.sportsfantasy.Model.LeagueTeam.leagueTeamResponse
import com.example.sportsfantasy.Model.MatchUp.MatchUpModel
import com.example.sportsfantasy.Model.MatchUp.MatchUpResponse
import com.example.sportsfantasy.Model.NewTenPos.NewTenPosResponse
import com.example.sportsfantasy.Model.PlayOff.PlayOffModel
import com.example.sportsfantasy.Model.PlayOff.PlayOffResponse
import com.example.sportsfantasy.Model.Rooster.LeagueScoreCardAddModel
import com.example.sportsfantasy.Model.Rooster.RosterResponse
import com.example.sportsfantasy.Model.Rooster.rosterAddModel
import com.example.sportsfantasy.Model.SearchTab.searchTabModel
import com.example.sportsfantasy.Model.SearchTab.searchTabResponse
import com.example.sportsfantasy.Model.UpdateLeague.updateLeagueModel
import com.example.sportsfantasy.Model.UpdateLeague.updateLeagueResponse
import com.example.sportsfantasy.Utils.Constant
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface
{

    //login
    @POST(Constant.WEB_SERVICES.WS_LOGIN_USER)
    fun userLogin(@Body login: Login): Call<LoginResponse>

    //register

    @POST(Constant.WEB_SERVICES.WS_REGISTER_USER)
    fun userRegister(@Body register: Register) : Call<RegisterResponse>

    //logout
    @POST(Constant.WEB_SERVICES.WS_LOGOUT)
    fun LogOut(@Header("Authorization") authHeader:String) : Call<LogoutResponse>


    //LeageFieldsCreateLeage

    @GET(Constant.WEB_SERVICES.WS_LEAGUE_FIELD)
    fun getLeagueFields() : Call<LeageFieldResponse>


    //createLeague

    @POST(Constant.WEB_SERVICES.WS_CREATE_LEAGUE)
    fun createLeague(@Body createLeagueModel: CreateLeagueModel) : Call<CreateLeagueModelResponse>


    @GET(Constant.WEB_SERVICES.WS_LEAGUE_LIST)
    fun getLeagueList(@Path("id") id:String) : Call<UserLeagueResponse>


    @POST(Constant.WEB_SERVICES.WS_FORGOTPASS)
    fun sendForgotPassword(@Body forgotPasswordModel: ForgotPasswordModel) : Call<ForgotPasswordResponse>


    //joinLeague
    @POST(Constant.WEB_SERVICES.WS_JOIN_LEAGUE_WITH_CODE)
    fun joinLeagueWithCode(@Body joinLeagueModel: JoinLeagueModel) : Call<JoinLeagueResponse>

    //positionlist  LeagueActivity
    @GET(Constant.WEB_SERVICES.WS_POSITION_LIST_IN_LEAGUE)
    fun positionList(@Header("Authorization") authorization:String, @Path("login_user_id") login_user_id:String, @Path("league_id") league_id:String) : Call<PositionListResponse>

    @GET(Constant.WEB_SERVICES.WS_TOP_TEN_PLAYER_LIST)
    fun getPlayerList(@Header("Authorization") authorization:String,@Path("postioncode") postioncode:String,@Path("filter_code") filter_code:String
                      ,@Path("league_start_date") league_start_date:String,@Path("code") code:String):
            Call<PlayerListResponse>

    @GET(Constant.WEB_SERVICES.WS_TOP_TEN_PLAYER_LIST)
    fun getNewPlayerList(@Header("Authorization") authorization:String,@Path("postioncode") postioncode:String,@Path("filter_code") filter_code:String
                      ,@Path("league_start_date") league_start_date:String,@Path("code") code:String):
            Call<NewTenPosResponse>

    @GET(Constant.WEB_SERVICES.WS_PLAYER_PROFILE)
    fun getPlayerProfile(@Header("Authorization") authorization:String,@Path("pid") pid:String):
            Call<PlayerProfileResponse>

    @GET(Constant.WEB_SERVICES.WS_SEARCH_PLAYER)
    fun getSearchPlayer(@Header("Authorization") authorization:String,@Path("searchText") searchText:String,@Path("postioncode") postioncode:String,@Path("filter_code") filter_code:String
                        ,@Path("league_start_date") league_start_date:String,@Path("code") code:String):
            Call<PlayerListResponse>

    @POST(Constant.WEB_SERVICES.WS_ADD_TO_DRAFT)
    fun addToDraft(@Header("Authorization") authorization:String, @Body addPlayerDraft: Add_Player_Draft):
            Call<Add_Player_Draft_Response>

    @GET(Constant.WEB_SERVICES.WS_REMOVE_TO_DRAFT)
    fun removeFromDraft(@Header("Authorization") authorization:String, @Path("draft_id") draft_id:String):
            Call<Draft_Remove_Response>


    @GET(Constant.WEB_SERVICES.WS_AUTO_DRAFT)
    fun autoDraft(@Header("Authorization") authorization:String,@Path("login_user_id") login_user_id:String,@Path("league_id") league_id:String):
            Call<AutoDraftResponse>

    @POST(Constant.WEB_SERVICES.WS_ROSTER_DATA)
    fun rosterData(@Header("Authorization") authorization:String,@Body rosterAddModel: rosterAddModel):
            Call<RosterResponse>

    @POST(Constant.WEB_SERVICES.WS_MATCH_UP_DATA)
    fun matchUpDate(@Header("Authorization") authorization:String,@Body matchUpModel: MatchUpModel):
            Call<MatchUpResponse>

    @POST(Constant.WEB_SERVICES.WS_PLAY_OFF_DATA)
    fun playOffDate(@Header("Authorization") authorization:String,@Body matchUpModel: PlayOffModel):
            Call<PlayOffResponse>

    @POST(Constant.WEB_SERVICES.WS_PLAYER_ALL)
    fun playerAll(@Header("Authorization") authorization: String,@Body playerTabModel: playerTabModel):
            Call<playerTabResponse>

    @POST(Constant.WEB_SERVICES.WS_SEARCH_PLAYER_TAB)
    fun searchTabPlayer(@Header("Authorization") authorization: String,@Body searchTabModel: searchTabModel):
            Call<searchTabResponse>

    @POST(Constant.WEB_SERVICES.WS_LEAGUE_SCORECARD)
    fun leagueScoreCard(@Header("Authorization") authorization:String,
                   @Body leagueScoreCardAddModel: LeagueScoreCardAddModel):
            Call<ResponseBody>

    @POST(Constant.WEB_SERVICES.WS_STANDING_LIST)
    fun getStandingList(@Header("Authorization") authorization:String,
                        @Body leagueScoreCardAddModel: StandingAddModel):
            Call<ResponseBody>

    @POST(Constant.WEB_SERVICES.WS_STANDING_GENERATE_CHANNEL_LIST)
    fun getLeagueChannelList(@Header("Authorization") authorization:String,
                        @Body leagueScoreCardAddModel: StandingAddModel):
            Call<ResponseBody>

    @POST(Constant.WEB_SERVICES.WS_GET_MESSAGE_LIST)
    fun getMessageList(@Header("Authorization") authorization:String,
                             @Body leagueScoreCardAddModel: GetMessageAddModel):
            Call<ResponseBody>

    @POST(Constant.WEB_SERVICES.WS_SEND_MESSAGE_LIST)
    fun sendMessage(@Header("Authorization") authorization:String,
                       @Body leagueScoreCardAddModel: SendMessageAddModel):
            Call<ResponseBody>

    @POST(Constant.WEB_SERVICES.WS_ADD_MANAGER)
    fun addManager(@Header("Authorization") authorization: String,@Body addManager: addManager):
            Call<managerResponse>

    @POST(Constant.WEB_SERVICES.WS_REMOVE_MANAGER)
    fun removeManager(@Header("Authorization") authorization: String,@Body addManager: addManager):
            Call<managerResponse>

    @POST(Constant.WEB_SERVICES.WS_LEAGUE_TEAMS)
    fun getLeagueTeam(@Header("Authorization") authorization: String,@Body leagueTeamModel: leagueTeamModel):
            Call<leagueTeamResponse>

    @POST(Constant.WEB_SERVICES.WS_UPDATE_LEAGUE)
    fun updateLeague(@Header("Authorization") authorization: String,@Body updateLeagueModel: updateLeagueModel):
            Call<updateLeagueResponse>

    @POST(Constant.WEB_SERVICES.UPDATE_DEVICE_TOKEN)
    fun updatedevicetoken(@Body deviceToken: DeviceToken): Call<ResponseBody>
}
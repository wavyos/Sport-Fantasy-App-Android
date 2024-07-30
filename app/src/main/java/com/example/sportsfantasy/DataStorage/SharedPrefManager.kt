package com.example.sportsfantasy.DataStorage

import android.content.Context
import com.example.sportsfantasy.Model.*
import com.google.gson.Gson


class SharedPrefManager private constructor(private val mCtx: Context)
{

    companion object {
        private val SHARED_PREF_NAME = "MySharedPref"
        private var mInstance: SharedPrefManager? = null
        @Synchronized
        fun getInstance(mCtx: Context): SharedPrefManager {
            if (mInstance == null) {
                mInstance = SharedPrefManager(mCtx)
            }
            return mInstance as SharedPrefManager
        }
    }

    val ULoggedIn: Boolean
        get()
        {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("u_login", false)
        }

    val RLoggedIn: Boolean
        get()
        {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("r_login", false)
        }

    val isFirstTime: Boolean
        get()
        {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("isFirstTime", false)
        }

    fun SaveLogin(LoginToken: LoginResponse)
    {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("status_code", LoginToken.status_code)
        editor.putString("message",LoginToken.message)
        editor.putString("access_token", LoginToken.access_token)
        editor.putString("created_at", LoginToken.userDetails.created_at)
        editor.putString("deleted_at", LoginToken.userDetails.deleted_at)
        editor.putString("email", LoginToken.userDetails.email)
        editor.putString("email_verified_at", LoginToken.userDetails.email_verified_at)
        editor.putInt("id", LoginToken.userDetails.id)
        editor.putString("name", LoginToken.userDetails.name)
        editor.putString("updated_at", LoginToken.userDetails.updated_at)
        editor.putBoolean("u_login", true);
        editor.putBoolean("isFirstTime", true)
        editor.apply()
    }

    fun setLanguage(appLanguage: String)
    {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("language",appLanguage)
        editor.apply()
    }

    val getLanguage:String
        get()
        {
            val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE)
            return sharedPreferences.getString("language","").toString()
        }

    fun SaveRegister(registerResponse: RegisterResponse)
    {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("status_code", registerResponse.status_code)
        editor.putString("message",registerResponse.message)
        editor.putString("access_token", registerResponse.access_token)
        editor.putString("created_at", registerResponse.userDetails.created_at)
        editor.putString("deleted_at", registerResponse.userDetails.deleted_at)
        editor.putString("email", registerResponse.userDetails.email)
        editor.putString("email_verified_at", registerResponse.userDetails.email_verified_at)
        editor.putInt("id", registerResponse.userDetails.id)
        editor.putString("name", registerResponse.userDetails.name)
        editor.putString("updated_at", registerResponse.userDetails.updated_at)
        editor.putBoolean("r_login", true);
        editor.apply()
    }

    fun SaveDate(date:String)
    {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("s_date", date)
        editor.apply()
    }

    val getSDate:String
    get()
    {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE)
        return sharedPreferences.getString("s_date","").toString()
    }




    fun saveLeague(userLeague: UserLeagueResponse.UserLeague)
    {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putString("created_at",userLeague.createdAt)
        editor.putString("deleted_at",userLeague.deletedAt)
        editor.putString("entry_fee", userLeague.entryFee.toString())
        editor.putString("league_id", userLeague.id.toString())
        editor.putString("invitation_code",userLeague.invitationCode)
        editor.putString("invitation_link",userLeague.invitationLink)
        editor.putString("is_entry_fee",userLeague.isEntryFee)
        editor.putString("is_league_admin",userLeague.isLeagueAdmin)
        editor.putString("league_name",userLeague.leagueName)
        editor.putString("is_postseason",userLeague.isPostseason)
        editor.putString("league_size", userLeague.leagueSize.toString())
        editor.putString("league_type",userLeague.leagueType)
        editor.putString("league_week", userLeague.leagueWeek.toString())
        editor.putString("league_start_date", userLeague.leagueStartDate.toString())
        editor.putString("remaining_members", userLeague.remainingMembers.toString())
        editor.putString("tid", userLeague.tid.toString())
        editor.putString("updated_at",userLeague.updatedAt)
        editor.putString("user_id", userLeague.userId.toString())



        editor.commit()
        editor.apply()
    }

    val getLeague:UserLeague
    get(){
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return UserLeague(
            created_at = sharedPreferences.getString("created_at", "").toString(),
            deleted_at = sharedPreferences.getString("deleted_at", "").toString(),
            entry_fee = sharedPreferences.getString("entry_fee", "").toString(),
            id = sharedPreferences.getString("league_id", "").toString(),
            invitation_code = sharedPreferences.getString("invitation_code", "").toString(),
            invitation_link = sharedPreferences.getString("invitation_link", "").toString(),
            is_entry_fee = sharedPreferences.getString("is_entry_fee", "").toString(),
            is_league_admin = sharedPreferences.getString("is_league_admin", "").toString(),
            is_postseason = sharedPreferences.getString("is_postseason", "").toString(),
            league_name = sharedPreferences.getString("league_name", "").toString(),
            league_size = sharedPreferences.getString("league_size", "").toString(),
            league_type = sharedPreferences.getString("league_type", "").toString(),
            league_week = sharedPreferences.getString("league_week", "").toString(),
            league_start_date = sharedPreferences.getString("league_start_date", "").toString(),
            remaining_members = sharedPreferences.getString("remaining_members", "").toString(),
            tid = sharedPreferences.getString("tid", "").toString(),
            updated_at = sharedPreferences.getString("updated_at", "").toString(),
            user_id = sharedPreferences.getString("user_id", "").toString())
    }


    val getLogin:LoginResponse
    get() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return LoginResponse(sharedPreferences.getInt("status_code", -1),
            sharedPreferences.getString("access_token","").toString(),
            sharedPreferences.getString("message","").toString(),
            UserDetails
                      (
                        sharedPreferences.getString("created_at",null).toString(),
                        sharedPreferences.getString("deleted_at",null).toString(),
                        sharedPreferences.getString("email",null).toString(),
                        sharedPreferences.getString("email_verified_at",null).toString(),
                        sharedPreferences.getInt("id",-1),
                        sharedPreferences.getString("name",null).toString(),
                        sharedPreferences.getString("updated_at",null).toString()
            ))
    }

    val getRegister:RegisterResponse
    get() {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        return RegisterResponse(
            sharedPreferences.getInt("status_code", -1),
            sharedPreferences.getString("access_token", null).toString(),
            sharedPreferences.getString("message","").toString(),
        UserDetails(
            sharedPreferences.getString("created_at",null).toString(),
            sharedPreferences.getString("deleted_at",null).toString(),
            sharedPreferences.getString("email",null).toString(),
            sharedPreferences.getString("email_verified_at",null).toString(),
            sharedPreferences.getInt("id",-1),
            sharedPreferences.getString("name",null).toString(),
            sharedPreferences.getString("updated_at",null).toString()
            ))
    }



    fun clear()
    {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }

    fun Logout()
    {
        val sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("access_token")
        editor.remove("message")
        editor.remove("access_token")
        editor.remove("created_at")
        editor.remove("email")
        editor.remove("email_verified_at")
        editor.remove("id")
        editor.remove("name")
        editor.remove("updated_at")
        editor.putBoolean("u_login",false)
        editor.putBoolean("r_login",false)
        editor.clear()
        editor.apply()
    }



}
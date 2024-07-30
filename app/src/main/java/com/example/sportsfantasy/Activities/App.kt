package com.example.sportsfantasy.Activities

import android.app.Application
import android.preference.PreferenceManager
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import java.util.*

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        var change = ""
        SharedPrefManager.getInstance(applicationContext).setLanguage("zh")
        val language = SharedPrefManager.getInstance(applicationContext).getLanguage
        /*if (language == "Turkish") {
            change="tr"
        } else if (language=="English" ) {
            change = "en"
        }else {
            change =""
        } */

        BaseActivity.dLocale = Locale(language) //set any locale you want here
    }
}
package com.example.sportsfantasy.Activities

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import java.util.*

open class BaseActivity : AppCompatActivity() {

    companion object {
        public var dLocale: Locale? = null
    }

    init {
        updateConfig(this)
    }

    fun updateConfig(wrapper: BaseActivity) {
        if(dLocale==Locale("") ) // Do nothing if dLocale is null
            return

        Locale.setDefault(dLocale)
        val configuration = Configuration()
        configuration.setLocale(dLocale)
        wrapper.applyOverrideConfiguration(configuration)
    }

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPrefManager.getInstance(applicationContext).setLanguage("zh")
        val language = SharedPrefManager.getInstance(applicationContext).getLanguage
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()

        config.setLocale(locale)
        applicationContext.resources.updateConfiguration(
            config,
            applicationContext.resources.displayMetrics
        )
    }*/
}

/*val locale = Locale(StoreUserData(activity).getString(Constants.USER_LANGUAGE))
        Locale.setDefault(locale)
        val config = Configuration()

        config.setLocale(locale)
        activity.baseContext.resources.updateConfiguration(
            config,
            activity.baseContext.resources.displayMetrics
        )*/
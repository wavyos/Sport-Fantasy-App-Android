package com.example.sportsfantasy.Activities

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.R
import java.net.URI
import java.util.*
import kotlin.system.exitProcess


class SplashScreenActivity : BaseActivity()
{
    var context: Context? = null
    lateinit var locale: Locale
    private var currentLanguage = "zn-rCN"
    private var currentLang: String? = null
    lateinit var uri:URI
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_splash_screen)

        val uri = intent.data

        if (uri!=null)
        {
            val path:String = uri.toString()
        }


        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(getColor(R.color.white))

        if (SharedPrefManager.getInstance(applicationContext).ULoggedIn)
        {
            Handler(Looper.getMainLooper()).postDelayed(Runnable
            {
                val intent = Intent(this@SplashScreenActivity, BottomNavigationActivity::class.java)
                this@SplashScreenActivity.startActivity(intent)
                finish()
            }, 2000)

        }
        else if (SharedPrefManager.getInstance(applicationContext).RLoggedIn)
        {
            Handler(Looper.getMainLooper()).postDelayed(Runnable
            {
                val intent = Intent(this@SplashScreenActivity, BottomNavigationActivity::class.java)
                this@SplashScreenActivity.startActivity(intent)
                finish()
            }, 2000)
        }

        else
        {
            Handler(Looper.getMainLooper()).postDelayed(Runnable
            {
                val intent = Intent(this@SplashScreenActivity, ChoiceActivity::class.java)
                this@SplashScreenActivity.startActivity(intent)
                finish()
            }, 3000)
        }

    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        finish()
        exitProcess(0)
    }
}
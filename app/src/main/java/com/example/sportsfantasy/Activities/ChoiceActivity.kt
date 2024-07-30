package com.example.sportsfantasy.Activities

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.exoplayer.ExoPlayer
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.ActivityChoiceBinding


class ChoiceActivity : BaseActivity()
{
    lateinit var binding: ActivityChoiceBinding
    var stime = 0
    var ltime = 0
    var later = 0

    //exoplayer
    private var player: ExoPlayer? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = window
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//  set status text dark
        window.statusBarColor = getColor(R.color.black)
        setUpVideoPlayer()
        
        binding.tvSignup.setOnClickListener{
            releasePlayer()
            stime = stime +1
            if (stime==1)
            {
                stime++
                !binding.tvSignup.isFocusable
                binding.tvSignup.isEnabled = false

                binding.tvSignup.postDelayed(Runnable {
                    startActivity(Intent(applicationContext,RegisterActivity::class.java))
                    binding.tvSignup.isFocusable
                    binding.tvSignup.isEnabled = true
                }, 200)

            }
            else
            {
                stime = 0
            }

        }



        binding.btnLogin.setOnClickListener{
            releasePlayer()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }


        binding.tvSignupLater.setOnClickListener{
            releasePlayer()
            val intent = Intent(applicationContext, OnboardingActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            /*Toast.makeText(this@ChoiceActivity, getString(R.string.str_work_in_progress), Toast.LENGTH_SHORT).show()*/
        }



//        binding.ivBack.setOnClickListener {
//
//            AlertDialog.Builder(this)
//                .setMessage(getString(R.string.areyousure))
//                .setCancelable(false)
//                .setPositiveButton(getString(R.string.yes),
//                    DialogInterface.OnClickListener {
//                            dialog, id -> this@ChoiceActivity.finish()
//                        dialog.cancel()
//                    })
//                .setNegativeButton(getString(R.string.no), null)
//                .show()
//
//
//        }

    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            exoPlayer.stop()
            exoPlayer.release()
        }
        player = null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (player != null){
            player!!.playWhenReady = false
            releasePlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (player != null){
            player!!.playWhenReady = false
            releasePlayer()
        }
    }


    public override fun onStop() {
        super.onStop()
        if (player != null){
            player!!.playWhenReady = false
            releasePlayer()
        }
    }

    private fun setUpVideoPlayer() {
        try {
            val tempUri = Uri.parse("android.resource://" + packageName + "/"+R.raw.new_intro)
            player = ExoPlayer.Builder(this)
                .build()
                .also { exoPlayer ->
                    binding.homeVideoView.player = exoPlayer
                }
                .also {
                    val mediaItem = MediaItem.fromUri(tempUri)
                    it.setMediaItem(mediaItem)
                    it.playWhenReady = true
                    it.repeatMode = REPEAT_MODE_ALL
                    it.prepare()
                }
            //To handle live play/pause events,use below code
            player!!.addListener(
                object : androidx.media3.common.Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        binding.imgMainThumb.isVisible= !isPlaying
                    }
                }
            )

        }catch (er: Exception){
            Toast.makeText(this, "${er.message}", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onBackPressed()
    {

        AlertDialog.Builder(this)
            .setMessage(getString(R.string.areyousure))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no), null)
            .setPositiveButton(getString(R.string.yes),
                object : DialogInterface.OnClickListener
                {
                    override fun onClick(arg0: DialogInterface?, arg1: Int)
                    {
                        finishAffinity()
                    }
                }).create().show()
    }


    override fun onStart()
    {
        super.onStart()
        binding.btnLogin.isClickable = true
        binding.btnLogin.isFocusable = true
        if (player == null){
            setUpVideoPlayer()
        }
    }

    override fun onResume()
    {
        super.onResume()
        if (player == null) {
            setUpVideoPlayer()
        }
    }



}
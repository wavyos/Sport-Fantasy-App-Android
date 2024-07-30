package com.example.sportsfantasy.Activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager.widget.ViewPager
import com.example.sportsfantasy.Adapter.OnboardingAdapter
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.ActivityChoiceBinding
import com.example.sportsfantasy.databinding.ActivityOnboardingBinding

class OnboardingActivity : BaseActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var adapter: OnboardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val window: Window = window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = getColor(R.color.white)
        }

        setUpScreens()
    }

    private fun setUpScreens() {
        val layouts = intArrayOf(
            R.layout.onboard_screen1,
            R.layout.onboard_screen2,
            R.layout.onboard_screen3,
            R.layout.onboard_screen4
        )

        adapter = OnboardingAdapter(this, layouts)
        binding.viewPager.adapter = adapter
        binding.wormDotsIndicator.setViewPager(binding.viewPager)
        binding.btnNext.setOnClickListener {
            val current = binding.viewPager.currentItem + 1
            if (current < layouts.size) {
                binding.viewPager.currentItem = current
            } else {
                val intent = Intent(applicationContext, BottomNavigationActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }

        binding.tvSkip.setOnClickListener {
            val intent = Intent(applicationContext, BottomNavigationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

    }
}
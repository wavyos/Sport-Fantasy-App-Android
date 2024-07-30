package com.example.sportsfantasy.Activities

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.sportsfantasy.databinding.ActivityDemoBinding


class DemoActivity : AppCompatActivity() {
    lateinit var binding: ActivityDemoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDemoBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val firstColumnCard = CardView(this)
//        myButton.layoutParams = LinearLayout.LayoutParams(
//            LinearLayout.LayoutParams.WRAP_CONTENT,
//            LinearLayout.LayoutParams.WRAP_CONTENT
//        )
//        myButton.text = "Hello"
//        binding.demoFrameLayout.addView(myButton)
    }
}
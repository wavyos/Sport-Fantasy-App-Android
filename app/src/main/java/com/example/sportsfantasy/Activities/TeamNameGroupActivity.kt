package com.example.sportsfantasy.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.sportsfantasy.databinding.ActivityTeamNameGroupBinding

class TeamNameGroupActivity : BaseActivity() {

    private lateinit var binding : ActivityTeamNameGroupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTeamNameGroupBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
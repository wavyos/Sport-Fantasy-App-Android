package com.example.sportsfantasy.Activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.sportsfantasy.Fragment.PlayerDataFragment
import com.example.sportsfantasy.Fragment.PlayerFragment
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.ActivityTeamNameGroupBinding
import com.example.sportsfantasy.databinding.ActivityTestBinding

class TestActvity:BaseActivity()
{
    lateinit var binding:ActivityTestBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        LoadFragmentPlayer(PlayerFragment())
        LoadFragmentPlayerData(PlayerDataFragment())
    }

    private fun LoadFragmentPlayerData(fragment: Fragment)
    {
        var transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_player_details,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun LoadFragmentPlayer(fragment: Fragment)
    {
        var transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_player,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
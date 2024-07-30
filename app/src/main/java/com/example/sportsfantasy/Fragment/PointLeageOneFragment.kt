package com.example.sportsfantasy.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sportsfantasy.Activities.TeamActivity
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.FragmentPointLeageOneBinding

class PointLeageOneFragment : Fragment()
{
    lateinit var binding:FragmentPointLeageOneBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPointLeageOneBinding.inflate(layoutInflater)

        binding.tvMyTeam.setOnClickListener{
            val intent = Intent(activity,TeamActivity::class.java)
            intent.putExtra("ChooseTab","MyTeam");
            startActivity(intent)
        }

        binding.tvMatchup.setOnClickListener{
            val intent = Intent(activity,TeamActivity::class.java)
            intent.putExtra("ChooseTab","Matchup");
            startActivity(intent)
        }


        return binding.root
    }

}
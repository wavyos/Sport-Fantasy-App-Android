package com.example.sportsfantasy.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sportsfantasy.Activities.PgUserActivity
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.FragmentTopPlayerBinding

class TopPlayerFragment : Fragment()
{
    lateinit var binding:FragmentTopPlayerBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentTopPlayerBinding.inflate(layoutInflater)


        binding.civPlayer.setOnClickListener{
            val intent = Intent(requireContext(),PgUserActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

}
package com.example.sportsfantasy.Adapter

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sportsfantasy.Activities.BottomNavigationActivity
import com.example.sportsfantasy.Fragment.*
import com.example.sportsfantasy.Fragment.newFlow.CommunityFragment


class TeamViewPagerAdapter(fragmentActivity: FragmentActivity, private var totalCount: Int,
    private var isPlayOff: Boolean?
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return totalCount
    }

    override fun createFragment(position: Int): Fragment
    {
        /*if(totalCount == 2){
            return when (position)
            {
                0 -> CommunityFragment()
                1 -> NewsFragment()
                else -> {
                    CommunityFragment()
                }
            }
        }*/
        try {
            val f = T_LeagueFragment()
            // Supply index input as an argument.
            // Supply index input as an argument.
            val args = Bundle()
            args.putBoolean("isPlayoff", isPlayOff!!)
            f.arguments = args

            val frag = T_PlayoffFragment()
            val bundle = Bundle()
            bundle.putBoolean("isHeader", false)
            frag.arguments = bundle

            if(isPlayOff!!){
                return when (position)
                {

                    0 -> T_RoosterFragment()
                    1 -> T_MatchupFragment()
                    2 -> frag
                    3 -> T_PlayerFragment()
                    4 -> f
                    else ->
                    {
                        T_RoosterFragment()
                    }
                }
            } else {
                return when (position)
                {

                    0 -> T_RoosterFragment()
                    1 -> T_MatchupFragment()
                    2 -> T_PlayerFragment()
                    3 -> f
                    else ->
                    {
                        T_RoosterFragment()
                    }
                }
            }
        }catch (er: Exception){
            Log.d("TabChangeError: ", er.message.toString())
            return T_RoosterFragment()
        }
    }
}
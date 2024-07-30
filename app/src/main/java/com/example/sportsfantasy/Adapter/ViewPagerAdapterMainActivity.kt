package com.example.sportsfantasy.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.sportsfantasy.Fragment.NewsFragment
import com.example.sportsfantasy.Fragment.TopPlayerFragment

class ViewPagerAdapterMainActivity(fragmentActivity: FragmentActivity, private var totalCount: Int) :
    FragmentStateAdapter(fragmentActivity)
{
    override fun getItemCount(): Int {
        return totalCount
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewsFragment()
            1 -> TopPlayerFragment()
            else -> NewsFragment()
        }
    }
}
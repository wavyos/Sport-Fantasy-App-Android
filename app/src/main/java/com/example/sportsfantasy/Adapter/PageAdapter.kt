package com.example.sportsfantasy.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.sportsfantasy.Fragment.NewsFragment
import com.example.sportsfantasy.Fragment.TopPlayerFragment

class PageAdapter (fm:FragmentManager) : FragmentPagerAdapter(fm)
{
    override fun getCount(): Int
    {
        return 2;
    }

    override fun getItem(position: Int): Fragment
    {
        when(position) {
            0 -> {
                return NewsFragment()
            }
            1 -> {
                return TopPlayerFragment()
            }

            else -> {
                return NewsFragment()
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "NEWS"
            }
            1 -> {
                return "TOP PLAYER"
            }

        }
        return super.getPageTitle(position)
    }

}
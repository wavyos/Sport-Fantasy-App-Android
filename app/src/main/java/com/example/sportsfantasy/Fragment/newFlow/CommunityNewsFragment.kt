package com.example.sportsfantasy.Fragment.newFlow

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.sportsfantasy.Adapter.TeamViewPagerAdapter
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.FragmentCommunityNewsBinding
import com.google.android.material.tabs.TabLayoutMediator

class CommunityNewsFragment : Fragment() {

    private lateinit var binding : FragmentCommunityNewsBinding
    private lateinit var iv_back: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCommunityNewsBinding.inflate(layoutInflater)

        iv_back = binding.root.findViewById(R.id.iv_back)
        iv_back.setOnClickListener{
            requireActivity().onBackPressed()
        }

        setupViewPager()
        setupTabLayout()

        return binding.root
    }

    private fun setupTabLayout() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager)
        { tab, index ->
            tab.text =
                    when (index) {
                        0 -> {
                            resources.getString(R.string.community_tab_title)
                        }
                        1 -> {
                            resources.getString(R.string.news_tab_title)
                        }
                        else -> {
                            throw Resources.NotFoundException("Position Not Found")
                        }
                    }

        }.attach()

        val args = arguments
        var Choose = ""
        if (args != null) {
            Choose = args.getString("from").toString() // Replace "key" with the parameter name you used
            // Now, you can use the value as needed
        }
        if (Choose == "main") {
            iv_back.visibility = View.VISIBLE
            binding.tabLayout.getTabAt(1)!!.select();
        } else {
            iv_back.visibility = View.GONE
            binding.tabLayout.getTabAt(0)!!.select();
        }
    }

    private fun setupViewPager() {
        val adapter = TeamViewPagerAdapter(requireActivity(), 2,false)
        binding.viewPager.adapter = adapter
    }
}
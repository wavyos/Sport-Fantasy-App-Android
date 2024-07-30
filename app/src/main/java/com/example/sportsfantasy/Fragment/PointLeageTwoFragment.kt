package com.example.sportsfantasy.Fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Activities.LeagueActivity

import com.example.sportsfantasy.Adapter.MA_ViewPagerAdapter
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Model.CreatedLeague
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.FragmentPointLeageTwoBinding


class PointLeageTwoFragment : Fragment()
{
    lateinit var binding: FragmentPointLeageTwoBinding

    lateinit var apiInterface: ApiInterface

    var userid:String = ""

    lateinit var leaguelist: ArrayList<CreatedLeague>
    lateinit var adapter: MA_ViewPagerAdapter
 //   lateinit var ladapter: LeagueListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentPointLeageTwoBinding.inflate(layoutInflater)

        //leaguelist = arrayListOf<UserLeague>()


        if (SharedPrefManager.getInstance(requireContext()).ULoggedIn)
        {
            userid = SharedPrefManager.getInstance(requireContext()).getLogin.userDetails.id.toString()

        }
        else if (SharedPrefManager.getInstance(requireContext()).RLoggedIn)
        {
            userid = SharedPrefManager.getInstance(requireContext()).getLogin.userDetails.id.toString()
        }
        else
        {
            userid = ""
        }


        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecyclerLeague.layoutManager = layoutManager
        binding.rvRecyclerLeague.hasFixedSize()

        binding.tvDraft.setOnClickListener{
            val intent = Intent(activity,LeagueActivity::class.java)
            startActivity(intent)
        }


        binding.inviteMember.setOnClickListener{
            val dialogebind = layoutInflater.inflate(R.layout.invite_dialogebox,null)
            val myDialoge = Dialog(binding.root.context)
            myDialoge.setContentView(dialogebind)


            val nobtn = myDialoge!!.findViewById(R.id.iv_closed) as ImageView
            val Invite_Code = myDialoge.findViewById(R.id.tv_invite_code) as TextView
            val Invite_Link = myDialoge.findViewById(R.id.tv_ivite_link) as TextView
            val Share_WeChat = myDialoge.findViewById(R.id.tv_share_wechat) as TextView

            Invite_Code.setOnClickListener{

            }
            Invite_Link.setOnClickListener{

            }
            Share_WeChat.setOnClickListener{

            }


            nobtn.setOnClickListener{
                myDialoge.cancel()
            }

            myDialoge.setCancelable(false)
            myDialoge.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            myDialoge.show()
        }

        return binding.root
    }



}
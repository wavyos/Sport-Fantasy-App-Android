package com.example.sportsfantasy.Fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsfantasy.Activities.BottomNavigationActivity
import com.example.sportsfantasy.Activities.ChatActivity
import com.example.sportsfantasy.Activities.LeagueDetailsActivity
import com.example.sportsfantasy.Activities.LeagueManagerToolActivity
import com.example.sportsfantasy.Activities.MessageBoardActivity
import com.example.sportsfantasy.Activities.StandingActivity
import com.example.sportsfantasy.Adapter.*
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Fragment.newFlow.SettingsFragment
import com.example.sportsfantasy.Fragment.newFlow.TeamFragment
import com.example.sportsfantasy.Interface.ListItemClick
import com.example.sportsfantasy.Model.GenerateChannelListResModel
import com.example.sportsfantasy.Model.LeagueScoreCardResModel
import com.example.sportsfantasy.Model.Rooster.LeagueScoreCardAddModel
import com.example.sportsfantasy.Model.StandingAddModel
import com.example.sportsfantasy.Model.StandingResModel
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.DialogMatchupBinding
import com.example.sportsfantasy.databinding.FragmentTLeagueBinding
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class T_LeagueFragment : Fragment()
{
    lateinit var binding : FragmentTLeagueBinding
    lateinit var sharedPrefManager: SharedPrefManager
    lateinit var apiInterface: ApiInterface
    var HeaderToken=""
    var User_id = ""
    var LeagueId = ""
    var admin = ""
    var UserName = ""
    var leagueName = ""
    lateinit var progressDialogue: Dialog

    var lastSelectedPosition = 0;

    private var leagueMatchUpList: List<LeagueScoreCardResModel.Matchup> = java.util.ArrayList()
    var leagueMatchUpDateList: List<LeagueScoreCardResModel.Week> = java.util.ArrayList()
    lateinit var leagueMatchUpListAdapter: LeagueMatchUpListAdapter
    lateinit var leagueMatchUpDateListAdapter: LeagueMatchUpDateListAdapter

    private var standingList: List<StandingResModel.StandingsData.EastTeam> = java.util.ArrayList()
    lateinit var standingAdapter: StandingsAdapter

    private var playerList: List<StandingResModel.StandingsData.EastTeam> = java.util.ArrayList()
    lateinit var playerAdapter: StandingPlayerAdapter

    private var standingWestList: List<StandingResModel.StandingsData.WestTeam> = java.util.ArrayList()
    lateinit var standingWestAdapter: StandingWestAdapter

    private var westPlayerList: List<StandingResModel.StandingsData.WestTeam> = java.util.ArrayList()
    lateinit var westPlayerAdapter: StandingWestPlayerAdapter

    private var leagueChannelList: List<GenerateChannelListResModel.Conv> = java.util.ArrayList()
    lateinit var leagueChannelAdapter: LeagueChannelAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        binding = FragmentTLeagueBinding.inflate(layoutInflater)
        Log.d("VRAJAN","No 4 fragment T_LeagueFragment")
        AllocateMemory()
        getArgumentsData()
        setEvents()
        return binding.root
        scrollableEventHandle()
    }

    private fun scrollableEventHandle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            binding.hvAllPosition.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                binding.hvScrollHeader.smoothScrollTo(oldScrollX, oldScrollY)
                binding.hvScrollHeader.scrollTo(oldScrollX, oldScrollY)

            }
        }
    }

    private fun getArgumentsData() {
        leagueName = sharedPrefManager.getLeague.league_name
        binding.tvLeagueName.text = leagueName
    }

    private fun AllocateMemory() {
        sharedPrefManager = SharedPrefManager.getInstance(requireContext())
        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)

        LeagueId = sharedPrefManager.getLeague.id
        admin = sharedPrefManager.getLeague.is_league_admin

        if(admin == "1"){
            binding.tvLeageManageTool.visibility = View.VISIBLE
            binding.viewManageTool.visibility = View.VISIBLE
        }else{
            binding.tvLeageManageTool.visibility = View.GONE
            binding.viewManageTool.visibility = View.GONE
        }

        Arguments()
    }

    private fun Arguments() {

        when {
            sharedPrefManager.ULoggedIn -> {
                HeaderToken = sharedPrefManager.getLogin.access_token
                User_id = sharedPrefManager.getLogin.userDetails.id.toString()
                UserName = sharedPrefManager.getLogin.userDetails.name.toString()
            }
            sharedPrefManager.RLoggedIn -> {
                HeaderToken = sharedPrefManager.getRegister.access_token
                User_id = sharedPrefManager.getRegister.userDetails.id.toString()
                UserName = sharedPrefManager.getRegister.userDetails.name.toString()
            }
            else -> {
                HeaderToken = ""
                User_id = ""
                UserName =""
            }
        }

        LeagueId = sharedPrefManager.getLeague.id
        leagueName = sharedPrefManager.getLeague.league_name

        val args = arguments
        val isPlayOff = args!!.getBoolean("isPlayoff", false)
        if(isPlayOff){
            binding.cardPlayoffBracket.visibility = View.VISIBLE
        } else {
            binding.cardPlayoffBracket.visibility = View.GONE
        }

        binding.cardPlayoffBracket.setOnClickListener {
            try {
                val act = requireActivity() as BottomNavigationActivity
                val frag = T_PlayoffFragment()
                val bundle = Bundle()
                bundle.putBoolean("isHeader", true)
                frag.arguments = bundle
                act.addFragment(frag)
            }catch (er:Exception){
                Log.d("Bracket:", ""+er.message)
            }
        }

        binding.cardSettings.setOnClickListener {
            try {
                val act = requireActivity() as BottomNavigationActivity
                val frag = SettingsFragment()
                act.addFragment(frag)
            }catch (er:Exception){
                Log.d("Settings:", ""+er.message)
            }
        }
    }


    private fun getStandingChannelList() {

        apiInterface.getLeagueChannelList(
            "Bearer $HeaderToken",
            StandingAddModel(User_id.toInt(), LeagueId.toInt())
        ).enqueue(object :
            Callback<ResponseBody> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val standingResponse = Gson().fromJson(
                    response.body()!!.string(),
                    GenerateChannelListResModel::class.java
                )
                if (standingResponse != null) {
                    if (standingResponse.success!!) {
                        if (standingResponse.convList!!.isNotEmpty()) {
                            leagueChannelList =
                                standingResponse.convList as ArrayList<GenerateChannelListResModel.Conv>
                            binding.rvStandingChannel.layoutManager =
                                LinearLayoutManager(requireActivity())
                            leagueChannelAdapter = LeagueChannelAdapter(requireActivity(),
                                UserName,
                                leagueChannelList as ArrayList<GenerateChannelListResModel.Conv>,
                                object : ListItemClick {
                                    override fun onItemClick(position: Int, data: Any?) {

                                        val i = Intent(requireActivity(), ChatActivity::class.java)
                                        i.putExtra("senderId", User_id)
                                        i.putExtra("receiver_id", leagueChannelList[position].userId.toString())
                                        i.putExtra("channel_id", leagueChannelList[position].channelId.toString())
                                        i.putExtra("title", leagueChannelList[position].title.toString())
                                        i.putExtra("leagueId", LeagueId)
                                        startActivity(i)
                                    }

                                })
                            binding.rvStandingChannel.adapter = leagueChannelAdapter
                        }
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                call.cancel()
            }
        })
    }

    private fun setEvents()
    {
        binding.tvScoreBoard.setOnClickListener{
            var i = Intent(context, LeagueDetailsActivity::class.java)
            startActivity(i)
            activity?.overridePendingTransition(0,0)
        }

        binding.tvStanding.setOnClickListener{
            var i = Intent(context, StandingActivity::class.java)
            startActivity(i)
            activity?.overridePendingTransition(0,0)

        }
        binding.tvMessageBoard.setOnClickListener{
            var i = Intent(context, MessageBoardActivity::class.java)
            startActivity(i)
            activity?.overridePendingTransition(0,0)
        }
        binding.tvLeageManageTool.setOnClickListener{
            val i = Intent(context, LeagueManagerToolActivity::class.java)
            startActivity(i)
            activity?.overridePendingTransition(0,0)
        }
        binding.llDate.setOnClickListener {
            showCityPickDialog(leagueMatchUpDateList)
        }



        getLeagueList(1)
        getStandingChannelList()
        getStandingList()
    }

    private fun getLeagueList(weekCode: Int) {
        progressDialoge()
        apiInterface.leagueScoreCard("Bearer $HeaderToken", LeagueScoreCardAddModel(User_id.toInt(),LeagueId.toInt(),weekCode)).enqueue(object :
            Callback<ResponseBody> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
            {
                progressDialogue.dismiss()
                val rosterResponse = Gson().fromJson(response.body()!!.string(), LeagueScoreCardResModel::class.java)

                Log.d("API RESPONSE", "rosterResponse:---- $rosterResponse")
                if(rosterResponse.success){
                    leagueMatchUpList = rosterResponse.matchupList as ArrayList<LeagueScoreCardResModel.Matchup>
                    leagueMatchUpDateList = rosterResponse.weekList as ArrayList<LeagueScoreCardResModel.Week>
                    if(weekCode == 1){
                        binding.tvSelectDate.text = leagueMatchUpDateList[0].value
                    }
                    binding.rvMatchUp.layoutManager = LinearLayoutManager(context)
                    leagueMatchUpListAdapter = LeagueMatchUpListAdapter(requireContext(),
                        rosterResponse.matchupList, object :
                        ListItemClick {
                        override fun onItemClick(position: Int, data: Any?) {

                            Log.d("TAG", "onItemClick: ")
                            leagueMatchUpList[position]
                        }
                    })
                    binding.rvMatchUp.adapter = leagueMatchUpListAdapter
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialogue.dismiss()
                call.cancel()
            }
        })
    }
    private fun getStandingList() {
        //progressDialoge()
        apiInterface.getStandingList("Bearer $HeaderToken", StandingAddModel(User_id.toInt(),LeagueId.toInt())).enqueue(object :
            Callback<ResponseBody> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
            {
                //progressDialogue.dismiss()
                val standingResponse = Gson().fromJson(response.body()!!.string(), StandingResModel::class.java)
                Log.d("API RESPONSE", "rosterResponse:---- "+standingResponse)
                if(standingResponse.success!!){
                    standingList = standingResponse.standingsData?.eastTeams as List<StandingResModel.StandingsData.EastTeam>
                    val templist = standingList.sortedByDescending { it.win!!.toFloat() }
                    //east team player details
                    binding.rvPlayersDetails.layoutManager = LinearLayoutManager(context)
                    standingAdapter = StandingsAdapter(requireContext(),templist)
                    binding.rvPlayersDetails.adapter = standingAdapter


                    //east team player name
                    binding.rvPlayersName.layoutManager = LinearLayoutManager(context)
                    playerAdapter = StandingPlayerAdapter(requireContext(),templist)
                    binding.rvPlayersName.adapter = playerAdapter



                    standingWestList = standingResponse.standingsData?.westTeams as List<StandingResModel.StandingsData.WestTeam>

                    val tempWestlist = standingWestList.sortedByDescending { it.win!!.toFloat() }
                    //west team player name
                    binding.rvCPlayer.layoutManager = LinearLayoutManager(context)
                    westPlayerAdapter = StandingWestPlayerAdapter(requireContext(), tempWestlist)
                    binding.rvCPlayer.adapter = westPlayerAdapter

                    //west team player details
                    binding.rvCPlayerDetails.layoutManager = LinearLayoutManager(context)
                    standingWestAdapter = StandingWestAdapter(requireContext(),tempWestlist)
                    binding.rvCPlayerDetails.adapter = standingWestAdapter
                }

            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                //progressDialogue.dismiss()
                call.cancel()
            }
        })
    }

    private fun progressDialoge()
    {
        val progressDialogeBind = layoutInflater.inflate(R.layout.custom_dialogebox,null)
        progressDialogue = Dialog(binding.root.context)
        progressDialogue.setContentView(progressDialogeBind)

        val civ_progress = progressDialogeBind.findViewById<CircleImageView>(R.id.civ_progress)
        // civ_progress.animate().rotation(360f).setDuration(1000).start()

        val rotate = RotateAnimation(
            0F, 360F,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )

        rotate.duration = 900
        rotate.repeatCount = Animation.INFINITE
        civ_progress.startAnimation(rotate)

        progressDialogue.setCancelable(false)
        progressDialogue.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialogue.show()

    }

    private fun showCityPickDialog(leagueMatchUpDateList: List<LeagueScoreCardResModel.Week>) {
        val dialog = Dialog(requireContext(), R.style.transDialog)

        var dialogMatchupBinding: DialogMatchupBinding = DataBindingUtil.inflate(
            LayoutInflater.from(requireContext()),
            R.layout.dialog_matchup,
            null,
            false
        )

        dialog.setContentView(dialogMatchupBinding.root)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(true)

        requireActivity().runOnUiThread {

            leagueMatchUpDateList[lastSelectedPosition].isSelected = true

            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            dialogMatchupBinding.recMatchUpDate.layoutManager = layoutManager
            leagueMatchUpDateListAdapter = LeagueMatchUpDateListAdapter(requireContext(),
                leagueMatchUpDateList as ArrayList<LeagueScoreCardResModel.Week>, object : ListItemClick {
                override fun onItemClick(position: Int, data:Any?) {
                    lastSelectedPosition = position
                    dialog.dismiss()
                    binding.tvSelectDate.text = leagueMatchUpDateList[position].value
                    getLeagueList(leagueMatchUpDateList[position].weekCode)
                }

            })
            fun onItemClick(position: Int, data:Any?) {
                dialog.dismiss()
                binding.tvSelectDate.text = leagueMatchUpDateList[position].value
                getLeagueList(leagueMatchUpDateList[position].weekCode)
            }
            dialogMatchupBinding.recMatchUpDate.adapter = leagueMatchUpDateListAdapter
        }

        dialog.show()
    }

    /*override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            AllocateMemory()
            getArgumentsData()
            setEvents()
            scrollableEventHandle()
        }
    }*/
}
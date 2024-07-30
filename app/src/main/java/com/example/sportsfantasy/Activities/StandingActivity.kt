package com.example.sportsfantasy.Activities

import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsfantasy.Adapter.StandingPlayerAdapter
import com.example.sportsfantasy.Adapter.StandingWestAdapter
import com.example.sportsfantasy.Adapter.StandingWestPlayerAdapter
import com.example.sportsfantasy.Adapter.StandingsAdapter
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Model.StandingAddModel
import com.example.sportsfantasy.Model.StandingResModel
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.ActivityStandingBinding
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.animation.Animation
import android.view.animation.RotateAnimation


class StandingActivity : BaseActivity() {

    lateinit var binding: ActivityStandingBinding

    lateinit var sharedPrefManager: SharedPrefManager
    var HeaderToken=""
    var User_id = ""
    var LeagueId = ""
    var UserName = ""
    var leagueName = ""
    lateinit var apiInterface: ApiInterface

    lateinit var progressDialogue: Dialog

    private var standingList: List<StandingResModel.StandingsData.EastTeam> = java.util.ArrayList()
    lateinit var standingAdapter: StandingsAdapter

    private var playerList: List<StandingResModel.StandingsData.EastTeam> = java.util.ArrayList()
    lateinit var playerAdapter: StandingPlayerAdapter

    private var standingWestList: List<StandingResModel.StandingsData.WestTeam> = java.util.ArrayList()
    lateinit var standingWestAdapter: StandingWestAdapter

    private var westPlayerList: List<StandingResModel.StandingsData.WestTeam> = java.util.ArrayList()
    lateinit var westPlayerAdapter: StandingWestPlayerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStandingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPrefManager = SharedPrefManager.getInstance(this)
        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)

        val layoutManager = LinearLayoutManager(this)
        binding.rvPlayersDetails.layoutManager = LinearLayoutManager(this)
        binding.rvPlayersName.layoutManager = LinearLayoutManager(this)
        binding.rvCPlayer.layoutManager = LinearLayoutManager(this)
        binding.rvCPlayerDetails.layoutManager = LinearLayoutManager(this)
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
        LeagueId = sharedPrefManager.getLeague.id

        val iv_back: ImageView = findViewById(R.id.iv_back)

        iv_back.setOnClickListener {
            onBackPressed()
        }
        getStandingList()
        progressDialoge()
    }

    private fun progressDialoge() {

            val progressDialogeBind = layoutInflater.inflate(R.layout.custom_dialogebox, null)
            progressDialogue = Dialog(binding.root.context)
            progressDialogue.setContentView(progressDialogeBind)

            val civ_progress = progressDialogeBind.findViewById<CircleImageView>(R.id.civ_progress)

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


    private fun getStandingList() {
        //progressDialoge()
        apiInterface.getStandingList("Bearer $HeaderToken", StandingAddModel(User_id.toInt(),LeagueId.toInt())).enqueue(object:
        Callback<ResponseBody>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>)
            {

                progressDialogue.dismiss()
                val standingResponse = Gson().fromJson(response.body()!!.string(), StandingResModel::class.java)
                Log.d("API RESPONSE", "rosterResponse:---- "+standingResponse)
                if(standingResponse.success!!){
                    standingList = standingResponse.standingsData?.eastTeams as ArrayList<StandingResModel.StandingsData.EastTeam>

                    //east team player details
                    standingAdapter = StandingsAdapter(this@StandingActivity,standingList as ArrayList<StandingResModel.StandingsData.EastTeam>)
                    binding.rvPlayersDetails.adapter = standingAdapter

                    //east team player name
                    playerAdapter = StandingPlayerAdapter(this@StandingActivity,standingList as ArrayList<StandingResModel.StandingsData.EastTeam>)
                    binding.rvPlayersName.adapter = playerAdapter

                    standingWestList = standingResponse.standingsData?.westTeams as ArrayList<StandingResModel.StandingsData.WestTeam>
                    //west team player name
                    westPlayerAdapter = StandingWestPlayerAdapter(this@StandingActivity, standingWestList as ArrayList<StandingResModel.StandingsData.WestTeam>)
                    binding.rvCPlayer.adapter = westPlayerAdapter

                    //west team player details
                    standingWestAdapter = StandingWestAdapter(this@StandingActivity,standingWestList as ArrayList<StandingResModel.StandingsData.WestTeam>)
                    binding.rvCPlayerDetails.adapter = standingWestAdapter
                }
        }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialogue.dismiss()
                call.cancel()
            }
        })
}}

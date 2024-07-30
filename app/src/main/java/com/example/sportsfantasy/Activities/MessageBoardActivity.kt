package com.example.sportsfantasy.Activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsfantasy.Adapter.LeagueChannelAdapter
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Interface.ListItemClick
import com.example.sportsfantasy.Model.GenerateChannelListResModel
import com.example.sportsfantasy.Model.StandingAddModel
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.ActivityMessageBoardBinding
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//23
class MessageBoardActivity : BaseActivity() {

    lateinit var binding: ActivityMessageBoardBinding

    lateinit var sharedPrefManager: SharedPrefManager
    lateinit var apiInterface: ApiInterface
    var HeaderToken = ""
    var User_id = ""
    var LeagueId = ""
    var UserName = ""
    var leagueName = ""
    lateinit var progressDialogue: Dialog

    private var leagueChannelList: List<GenerateChannelListResModel.Conv> = java.util.ArrayList()
    lateinit var leagueChannelAdapter: LeagueChannelAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMessageBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefManager = SharedPrefManager.getInstance(this)
        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)

        LeagueId = sharedPrefManager.getLeague.id

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
                UserName = ""
            }
        }

        LeagueId = sharedPrefManager.getLeague.id
        leagueName = sharedPrefManager.getLeague.league_name

        getStandingChannelList("")

        val iv_backnew: ImageView = findViewById(R.id.iv_back)

        iv_backnew.setOnClickListener{
            onBackPressed()

        }
    }

    private fun getStandingChannelList(s: String) {
        if (s == "") {
            progressDialoge()
        }

        apiInterface.getLeagueChannelList(
            "Bearer $HeaderToken",
            StandingAddModel(User_id.toInt(), LeagueId.toInt())
        ).enqueue(object :
            Callback<ResponseBody> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressDialogue.dismiss()
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
                                LinearLayoutManager(this@MessageBoardActivity)
                            leagueChannelAdapter = LeagueChannelAdapter(this@MessageBoardActivity,
                                UserName,
                                leagueChannelList as ArrayList<GenerateChannelListResModel.Conv>,
                                object : ListItemClick {
                                    override fun onItemClick(position: Int, data: Any?) {

                                        Log.d("TAG", "onItemClick: ")
                                        var i = Intent(this@MessageBoardActivity, ChatActivity::class.java)
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
                progressDialogue.dismiss()
                call.cancel()
            }
        })
    }

    private fun progressDialoge() {
        val progressDialogeBind = layoutInflater.inflate(R.layout.custom_dialogebox, null)
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

    override fun onResume() {
        super.onResume()

        getStandingChannelList("onResume")
    }

}
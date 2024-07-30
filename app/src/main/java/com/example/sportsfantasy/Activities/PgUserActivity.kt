package com.example.sportsfantasy.Activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportsfantasy.Adapter.*
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Model.*
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.ActivityPgUserBinding
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PgUserActivity : BaseActivity()
{
    lateinit var iv_menu: ImageView
    lateinit var iv_back: ImageView
    lateinit var tv_name_app: TextView

    lateinit var HeaderToken:String
    lateinit var apiInterface: ApiInterface
    lateinit var sharedPrefManager: SharedPrefManager
    lateinit var progressDialogue: Dialog

    lateinit var playerProfileAdapter: ProfileAdapter
    lateinit var PlayerProfileList: ArrayList<PlayerProfile>

    lateinit var seasonStateListAdapter:SeasonStateListAdapter
    lateinit var seasonStateList: ArrayList<SeasonState>

    lateinit var gameLogAdapter: GameLogAdapter
    lateinit var gameLogList: ArrayList<GameLog>

    lateinit var pid:String

     var avg_points: String = ""
     var fullname: String = ""
     var hashNumber: String  = ""
     var min: String = ""
     var playedteamname: String = ""
     var pos_rank: String = ""
     var positiontype: String = ""
     var primaryposition: String = ""
     var rostered: String = ""

    lateinit var binding:ActivityPgUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPgUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AllocateMemory()
        GetData()
        getHeader()
        setEvents()

    }

    private fun GetData()
    {
        pid = intent.getStringExtra("pid").toString()
        //binding.tvLeagueName.text = "Select $Select_Pg"

        if (sharedPrefManager.ULoggedIn)
        {
            HeaderToken = sharedPrefManager.getLogin.access_token
        }
        else if (sharedPrefManager.RLoggedIn)
        {
            HeaderToken = sharedPrefManager.getRegister.access_token
        }
        else
        {
            HeaderToken = ""
        }
    }

    private fun AllocateMemory()
    {
        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(applicationContext)

        PlayerProfileList = ArrayList<PlayerProfile>()
        seasonStateList = ArrayList<SeasonState>()
        gameLogList = ArrayList<GameLog>()

    }

    private fun setEvents()
    {


        binding.rvSeasonStat.hasFixedSize()
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, true)
        binding.rvSeasonStat.layoutManager = layoutManager

        binding.rvGameLog.hasFixedSize()
        val layoutManager2: RecyclerView.LayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, true)
        binding.rvGameLog.layoutManager = layoutManager2

        progressDialoge()
        apiInterface.getPlayerProfile("Bearer $HeaderToken",pid).enqueue(object :
        Callback<PlayerProfileResponse>{ override fun onResponse(call: Call<PlayerProfileResponse>, response: Response<PlayerProfileResponse>)
            {
                if (response.isSuccessful)
                {
                    if(response.body()!!.success)
                    {
                        binding.civProgress.visibility = View.GONE
                        binding.shimmer.stopShimmer()
                        binding.shimmer.visibility = View.GONE
                        binding.llMain.visibility = View.VISIBLE
                        val playerProfileResponse = response.body()

                        if (playerProfileResponse != null)
                        {
                            fullname = playerProfileResponse.player_profile.fullname
                        }
                        if (playerProfileResponse != null)
                        {
                            positiontype = playerProfileResponse.player_profile.positiontype
                        }
                        if (playerProfileResponse != null)
                        {
                            pos_rank = playerProfileResponse.player_profile.pos_rank
                        }
                        if (playerProfileResponse != null) {
                            playedteamname = playerProfileResponse.player_profile.playedteamname
                        }
                        if (playerProfileResponse != null) {
                            hashNumber = playerProfileResponse.player_profile.hashNumber
                        }
                        if (playerProfileResponse != null) {
                            avg_points = playerProfileResponse.player_profile.avg_points
                        }
                        if (playerProfileResponse != null) {
                            min = playerProfileResponse.player_profile.min
                        }
                        if (playerProfileResponse != null) {
                            rostered = playerProfileResponse.player_profile.rostered
                        }
                        if(playerProfileResponse != null)
                        {
                            primaryposition = playerProfileResponse.player_profile.primaryposition
                            Glide.with(this@PgUserActivity)
                                .load(intent.getStringExtra("headshot").toString())
                                .placeholder(ContextCompat.getDrawable(this@PgUserActivity, R.drawable.player_placeholder_img))
                                .into(binding.ivUser)
                        }


                        val positionText = "<b><font color='#f87c46'>"+positiontype+"</font></b>"
                        val PlayedText= "<b><font color='#f87c46'>"+playedteamname+"</font></b>"
                        val PrimaryPositionText= "<b><font color='#f87c46'>"+primaryposition+"</font></b>"

                        binding.tvPlayedTeamName.text = playedteamname
                        binding.tvPosition.text = primaryposition
//                        binding.tvStatus.setText(Html.fromHtml(PrimaryPositionText), TextView.BufferType.SPANNABLE)

                        binding.tvPlayerName.text = fullname
//                        binding.tvHashRank.text = hashNumber
                        binding.tvPosRank.text = pos_rank
                        binding.tvAvgPoint.text = avg_points
//                        binding.tvMin.text = min
//                        binding.tvRostered.text = rostered

                        seasonStateList.addAll(response.body()!!.season_state)
                        seasonStateListAdapter = SeasonStateListAdapter(applicationContext,seasonStateList)
                        binding.rvSeasonStat.adapter = seasonStateListAdapter
                        seasonStateListAdapter.notifyDataSetChanged()


                        gameLogList.addAll(response.body()!!.game_log)
                        gameLogAdapter = GameLogAdapter(applicationContext,gameLogList)
                        binding.rvGameLog.adapter = gameLogAdapter
                        gameLogAdapter.notifyDataSetChanged()

                        progressDialogue.dismiss()


                    }
                    else
                    {
                        binding.rvSeasonStat.adapter = null
                        binding.rvGameLog.adapter = null
                        progressDialogue.dismiss()
                        //    binding.rvPlayerProfile.adapter = null
                    }
                }
                else
                {
                    binding.rvSeasonStat.adapter = null
                    binding.rvGameLog.adapter = null
                    progressDialogue.dismiss()
                    //binding.rvPlayerProfile.adapter = null
                }
            }
            override fun onFailure(call: Call<PlayerProfileResponse>, t: Throwable)
            {
                call.cancel()
            }

        })


    }

    private fun progressDialoge() {
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

    private fun getHeader() {
        iv_menu = findViewById(R.id.iv_menu)
        iv_back = findViewById(R.id.iv_back)
        tv_name_app = findViewById(R.id.tv_name_app)

        iv_menu.visibility = View.GONE

        iv_back.setOnClickListener{
            onBackPressed()
        }
    }

    override fun onBackPressed()
    {
        super.onBackPressed()
    }
}
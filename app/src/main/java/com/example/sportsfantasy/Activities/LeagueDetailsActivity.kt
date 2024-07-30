package com.example.sportsfantasy.Activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsfantasy.Adapter.LeagueMatchUpDateListAdapter
import com.example.sportsfantasy.Adapter.LeagueMatchUpListAdapter
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Interface.ListItemClick
import com.example.sportsfantasy.Model.LeagueScoreCardResModel
import com.example.sportsfantasy.Model.Rooster.LeagueScoreCardAddModel
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.ActivityLeagueDetailsBinding
import com.example.sportsfantasy.databinding.DialogMatchupBinding
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LeagueDetailsActivity : BaseActivity()
{
    lateinit var binding:ActivityLeagueDetailsBinding

    lateinit var sharedPrefManager: SharedPrefManager
    lateinit var apiInterface: ApiInterface
    var HeaderToken=""
    var User_id = ""
    var LeagueId = ""
    var UserName = ""
    var leagueName = ""
    var lastSelectedItem = 0;
    lateinit var progressDialogue: Dialog
    private var leagueMatchUpList: List<LeagueScoreCardResModel.Matchup> = java.util.ArrayList()
    var leagueMatchUpDateList: List<LeagueScoreCardResModel.Week> = java.util.ArrayList()
    lateinit var leagueMatchUpListAdapter: LeagueMatchUpListAdapter

    lateinit var leagueMatchUpDateListAdapter: LeagueMatchUpDateListAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityLeagueDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefManager = SharedPrefManager.getInstance(this)
        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)


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
            //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            onBackPressed()
        }

        binding.llDate.setOnClickListener {
            showCityPickDialog(leagueMatchUpDateList)
        }

        getLeagueList(1)
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
                Log.d("API RESPONSE", "rosterResponse:---- "+rosterResponse)
                if(rosterResponse.success){
                    leagueMatchUpList = rosterResponse.matchupList as ArrayList<LeagueScoreCardResModel.Matchup>
                    leagueMatchUpDateList = rosterResponse.weekList as ArrayList<LeagueScoreCardResModel.Week>
                    if(weekCode == 1){
                        binding.tvSelectDate.text = leagueMatchUpDateList[0].value
                    }

                    binding.rvMatchUp.layoutManager = LinearLayoutManager(this@LeagueDetailsActivity)
                    leagueMatchUpListAdapter = LeagueMatchUpListAdapter(this@LeagueDetailsActivity,
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
        val dialog = Dialog(this, R.style.transDialog)

        var dialogMatchupBinding: DialogMatchupBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.dialog_matchup,
            null,
            false
        )

        dialog.setContentView(dialogMatchupBinding.root)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(true)

        runOnUiThread {

            leagueMatchUpDateList[lastSelectedItem].isSelected = true
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            dialogMatchupBinding.recMatchUpDate.layoutManager = layoutManager
            leagueMatchUpDateListAdapter = LeagueMatchUpDateListAdapter(this,
                leagueMatchUpDateList as ArrayList<LeagueScoreCardResModel.Week>, object : ListItemClick {
                    override fun onItemClick(position: Int, data:Any?) {
                        lastSelectedItem = position
                        dialog.dismiss()
                        binding.tvSelectDate.text = leagueMatchUpDateList[position].value
                        getLeagueList(leagueMatchUpDateList[position].weekCode)
                    }
                })
            dialogMatchupBinding.recMatchUpDate.adapter = leagueMatchUpDateListAdapter
        }
        dialog.show()
    }

    override fun onBackPressed()
    {
        super.onBackPressed()
       // overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        overridePendingTransition(0,0);
    }
}
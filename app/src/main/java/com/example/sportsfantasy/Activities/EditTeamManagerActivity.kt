package com.example.sportsfantasy.Activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidadvance.topsnackbar.TSnackbar
import com.example.sportsfantasy.Adapter.LeagueTeam.LeagueTeamAdapter
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Interface.leagueTeamListner
//import com.example.sportsfantasy.Model.LeagueTab.Standing.EastTeam
import com.example.sportsfantasy.Model.LeagueTeam.Manager.addManager
import com.example.sportsfantasy.Model.LeagueTeam.Manager.managerResponse
import com.example.sportsfantasy.Model.LeagueTeam.Team
import com.example.sportsfantasy.Model.LeagueTeam.leagueTeamModel
import com.example.sportsfantasy.Model.LeagueTeam.leagueTeamResponse
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.ActivityEditTeamManagerBinding
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditTeamManagerActivity : BaseActivity(), leagueTeamListner
{
    lateinit var binding:ActivityEditTeamManagerBinding
    private lateinit var apiInterface: ApiInterface
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var leagueTeamAdapter: LeagueTeamAdapter
    private lateinit var progressDialogue: Dialog

    var HeaderToken=""
    var User_id = ""
    var LeagueId = ""
    var UserName = ""
    var LeagueName = ""
    var admin = ""
    var arrTeamList = ArrayList<Team>()


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityEditTeamManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CustHeader()
        AllocateMemory()
        getArgumentsData()
        setEvents()
    }

    private fun setEvents() {
        progressDialoge()
        apiInterface.getLeagueTeam("Bearer $HeaderToken", leagueTeamModel(LeagueId.toInt(),User_id.toInt())).
        enqueue(object :Callback<leagueTeamResponse>{
            override fun onResponse(call: Call<leagueTeamResponse>, response: Response<leagueTeamResponse>)
            {
                val teamRespo = response.body()
                if (teamRespo != null)
                {
                    if (teamRespo.success)
                    {
                        arrTeamList.clear()
                        arrTeamList.addAll(teamRespo.teams)
                        leagueTeamAdapter = LeagueTeamAdapter(applicationContext,arrTeamList,User_id,this@EditTeamManagerActivity)
                        binding.rvEditTeamManager.adapter = leagueTeamAdapter
                        leagueTeamAdapter.notifyDataSetChanged()
                        progressDialogue.dismiss()
                    }
                    else
                    {
                        progressDialogue.dismiss()
                    }
                }
                else
                {
                    progressDialogue.dismiss()
                }

            }
            override fun onFailure(call: Call<leagueTeamResponse>, t: Throwable) {
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

    private fun getArgumentsData() {

        if (sharedPrefManager.ULoggedIn) {
            HeaderToken = sharedPrefManager.getLogin.access_token
            User_id = sharedPrefManager.getLogin.userDetails.id.toString()
            UserName = sharedPrefManager.getLogin.userDetails.name.toString()
        } else if (sharedPrefManager.RLoggedIn) {
            HeaderToken = sharedPrefManager.getRegister.access_token
            User_id = sharedPrefManager.getRegister.userDetails.id.toString()
            UserName = sharedPrefManager.getRegister.userDetails.name.toString()
        } else {
            HeaderToken = ""
            User_id = ""
            UserName =""
        }

        LeagueId = sharedPrefManager.getLeague.id
        LeagueName = sharedPrefManager.getLeague.league_name
        admin = sharedPrefManager.getLeague.user_id.toString()


    }

    private fun AllocateMemory() {

        arrTeamList = ArrayList()
        sharedPrefManager = SharedPrefManager.getInstance(applicationContext)
        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)

        binding.rvEditTeamManager.layoutManager = LinearLayoutManager(this)
        binding.rvEditTeamManager.setHasFixedSize(false)
    }

    private fun CustHeader() {
        val iv_menu: ImageView = findViewById(R.id.iv_menu)
        val iv_back: ImageView = findViewById(R.id.iv_back)
        val tv_name_app: TextView = findViewById(R.id.tv_name_app)


        iv_back.setOnClickListener{
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun removeManager(position: Int, team: Team) {
        progressDialoge()
        apiInterface.removeManager("Bearer $HeaderToken",addManager(LeagueId.toInt(),User_id.toInt())).enqueue(object : Callback<managerResponse>{
            override fun onResponse(call: Call<managerResponse>, response: Response<managerResponse>)
            {
                val manRespo = response.body()
                if (manRespo != null) {
                    if (manRespo.success)
                    {
                        MessageForValid("Successfully remove manager.")
                        progressDialogue.dismiss()
                    } else {
                        progressDialogue.dismiss()
                    }
                }
                else
                {
                    progressDialogue.dismiss()
                }
            }
            override fun onFailure(call: Call<managerResponse>, t: Throwable) {
                call.cancel()
            }

        })
    }

    override fun removeUser(position: Int, team: Team) {
        Log.e("Remove user", "removeUser: "+position )
        Log.e("Remove User", "removeUser: "+team )
    }

    override fun makeManager(position: Int, team: Team) {
        progressDialoge()
        apiInterface.addManager("Bearer $HeaderToken", addManager(LeagueId.toInt(),User_id.toInt())).enqueue(object :Callback<managerResponse>{
            override fun onResponse(call: Call<managerResponse>, response: Response<managerResponse>)
            {
                val manResp = response.body();

                if (manResp != null)
                {
                    if (manResp.success)
                    {
                        MessageForValid("Manager make success")
                        progressDialogue.dismiss()
                    }
                    else
                    {
                        MessageForValid("Failed...")
                        progressDialogue.dismiss()
                    }
                }
                else
                {
                    progressDialogue.dismiss()
                }
            }

            override fun onFailure(call: Call<managerResponse>, t: Throwable) {
                call.cancel()
                progressDialogue.dismiss()
            }

        })
    }

    private fun MessageForValid(s: String)
    {
        val snackbar = TSnackbar.make(findViewById(android.R.id.content), s, TSnackbar.LENGTH_SHORT)
        // snackbar.setActionTextColor(Color.WHITE)
        val snackbarView = snackbar.view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackbarView.setBackgroundColor(getColor(R.color.bg))
        }
        snackbar.setIconLeft(R.drawable.ic_warning, 18F)
        snackbar.setIconPadding(10)
        // snackbar.setIconPadding(5)

        val textView = snackbarView.findViewById<View>(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        // textView.compoundDrawablePadding = 5
        textView.textSize = 12F
        snackbar.show()
    }
}

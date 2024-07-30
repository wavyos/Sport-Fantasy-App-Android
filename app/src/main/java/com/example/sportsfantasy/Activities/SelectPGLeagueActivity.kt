package com.example.sportsfantasy.Activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidadvance.topsnackbar.TSnackbar
import com.example.sportsfantasy.Adapter.PlayerListAdapter
import com.example.sportsfantasy.Adapter.PlayerListColumnAdapter
import com.example.sportsfantasy.DataStorage.DbSqliteHelper
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Interface.PlayerListner
import com.example.sportsfantasy.Model.*
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.Utils.Constant
import com.example.sportsfantasy.databinding.ActivitySelectLeaguePgBinding
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


@SuppressLint("SetTextI18n")
class SelectPGLeagueActivity : BaseActivity(), PlayerListner, OnItemSelectedListener
{
    lateinit var binding: ActivitySelectLeaguePgBinding
    lateinit var iv_menu: ImageView
    lateinit var iv_back: ImageView
    lateinit var tv_name_app: TextView
    //lateinit var tv_nav_username_: TextView

    var postingCodeLeague: String = ""
    var League_id_Position: String = ""
    var Position_id: String = ""
    var User_id: String = ""
    var salary: String = ""
    var filterCode: String = "0"
    var remainSalary = 0

    var totalSmall = 0
    var totalStarter = 0
    var totalStar = 0


    lateinit var HeaderToken: String
    lateinit var apiInterface: ApiInterface
    lateinit var sharedPrefManager: SharedPrefManager

    var leagueStartDate: String = ""

    lateinit var playerListColumnAdapter: PlayerListColumnAdapter
    lateinit var playerListAdapter: PlayerListAdapter


    lateinit var arrPlayerList: ArrayList<Player>

    lateinit var progressDialogue: Dialog


    lateinit var dbSqliteHelper: DbSqliteHelper

    var status: Long = 0

    var positionListId = 0

    var getPlayers: ArrayList<Player> = ArrayList()
    var getAllPlayers: ArrayList<Player> = ArrayList()
    lateinit var playerAdd: Player

    var arrStatus = arrayOf<String?>("This Season", "Last 7 Games",
        "Last 3 Games", "Last Game")

    lateinit var playerLinerLayoutManager:LinearLayoutManager
    lateinit var playerColumnLinerLayoutManager:LinearLayoutManager

    var page = 1
    var isLoading = false
    val limit = 10

    val tenElement:ArrayList<Player> = ArrayList()


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectLeaguePgBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.e("krunal", "onCreate: ")
        var change = ""
        SharedPrefManager.getInstance(applicationContext).setLanguage("zh")
        val language = SharedPrefManager.getInstance(applicationContext).getLanguage


        dLocale = Locale(language)

        allocateMemory()

        argumentsData()
        CustomHeader()

        searchPlayerList()

        getStatusData()

        binding.rvPlayers.addOnScrollListener(object :RecyclerView.OnScrollListener(){
        })

        binding.scrollNested.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->


            if (scrollY == (binding.scrollNested.getChildAt(0).measuredHeight - binding.scrollNested.measuredHeight))
            {
                playerLinerLayoutManager = binding.rvPlayers.layoutManager as LinearLayoutManager

                Log.e("TAG", "BOTTOM SCROLL")

                if (playerLinerLayoutManager != null && playerLinerLayoutManager.findLastCompletelyVisibleItemPosition() == tenElement.size - 1)
                {
                    isLoading = true

                    GlobalScope.launch {

                        getAllPlayers = dbSqliteHelper.getPlayersAllData(positionListId,filterCode.toInt())
                        playerListColumnAdapter = PlayerListColumnAdapter(applicationContext, getAllPlayers, this@SelectPGLeagueActivity)
                        playerListAdapter = PlayerListAdapter(applicationContext, getAllPlayers, this@SelectPGLeagueActivity)

                        binding.rvPlayersColumn.adapter = playerListColumnAdapter
                        binding.rvPlayers.adapter = playerListAdapter

                        playerListColumnAdapter.notifyDataSetChanged()
                        playerListAdapter.notifyDataSetChanged()

                    }

                }


            }
        }

    }

    private fun getStatusData() {
        binding.actStatus.onItemSelectedListener = this
        val statusAdaper: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, R.layout.color_spinner_layout, arrStatus)
        statusAdaper.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        binding.actStatus.adapter = statusAdaper

    }


    @SuppressLint("NotifyDataSetChanged")
    private fun showData()
    {
        CheckingAllData()
        playerListColumnAdapter = PlayerListColumnAdapter(applicationContext, tenElement, this@SelectPGLeagueActivity)
        playerListAdapter = PlayerListAdapter(applicationContext, tenElement, this)

        binding.rvPlayersColumn.adapter = playerListColumnAdapter
        playerListColumnAdapter.notifyDataSetChanged()

        binding.rvPlayers.adapter = playerListAdapter
        playerListAdapter.notifyDataSetChanged()
        binding.pbBar.visibility = View.GONE

        Handler(Looper.getMainLooper()).postDelayed({
           // isLoading = false //
            progressDialogue.dismiss()
        }, 1000)

        isLoading = false

    }


    private fun showDataSqlite()
    {
        progressDialoge()
        Log.e("xxxx", "showData: call function" )

        playerListColumnAdapter = PlayerListColumnAdapter(applicationContext, getPlayers, this@SelectPGLeagueActivity)
        playerListAdapter = PlayerListAdapter(applicationContext, getPlayers, this)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.rvPlayersColumn.adapter = playerListColumnAdapter
            binding.rvPlayers.adapter = playerListAdapter
            Log.e("vr", "showDataSqlite: handler adapter" )
        },500)


        playerListColumnAdapter.notifyDataSetChanged()
        playerListAdapter.notifyDataSetChanged()

        CheckingAllData()

        Handler(Looper.getMainLooper()).postDelayed({
            progressDialogue.dismiss()
            Log.e("vr", "showDataSqlite: dismiss" )
        },2000)

        getNewData()

        isLoading = false

    }

    private fun getNewData() {


    }


    private fun CheckingAllData()
    {
        for (player in tenElement)
        {
            val strSalary = player.salary
            var playerSalary = 0


            if (strSalary.equals("-") || strSalary.equals("0"))
            {
                playerSalary = 0
            }
            else
            {
                playerSalary = strSalary.toInt()
            }

            val playerType: Constant.PLAYER_TYPE =
                if (playerSalary <= 15) Constant.PLAYER_TYPE.small else if (playerSalary in 16..20) Constant.PLAYER_TYPE.starter else Constant.PLAYER_TYPE.star


            when (playerType) {
                Constant.PLAYER_TYPE.small -> player.isEnable =
                    totalSmall < 1 && playerSalary <= remainSalary
                Constant.PLAYER_TYPE.starter -> player.isEnable =
                    totalStarter < 2 && playerSalary <= remainSalary
                Constant.PLAYER_TYPE.star -> player.isEnable =
                    totalStar < 2 && playerSalary <= remainSalary
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getPlayerList(filterCode: Int)
    {

        getPlayers = dbSqliteHelper.getPlayersData(positionListId,filterCode)
        if (getPlayers.isNotEmpty())
        {
            showDataSqlite()
        }
        else
        {
            progressDialoge()
            //binding.pbBar.visibility = View.VISIBLE
            apiInterface.getPlayerList("Bearer $HeaderToken", postingCodeLeague,  ""+ filterCode,leagueStartDate,"0")
                .enqueue(object : Callback<PlayerListResponse> {
                    override fun onResponse(
                        call: Call<PlayerListResponse>,
                        response: Response<PlayerListResponse>,
                    ) {
                        if (response.isSuccessful) {
                            if (response.body()!!.success)
                            {
                                val arr = response.body()!!.playerList
                                tenElement.addAll(arr)

                                if (arr.isNotEmpty())
                                {
                                    for(i in arr)
                                    {
                                        playerAdd = Player(
                                            player_id = i.player_id,
                                            pid = i.pid,
                                            fullname = i.fullname,
                                            positiontype = i.positiontype,
                                            primaryposition = i.primaryposition,
                                            salary = i.salary,
                                            salary_percentage = i.salary_percentage,
                                            FGM = i.FGM,
                                            FG = i.FG,
                                            FT = i.FT,
                                            Reb = i.Reb,
                                            Ast = i.Ast,
                                            BLK = i.BLK,
                                            STL = i.STL,
                                            TOV = i.TOV,
                                            points = i.points,
                                            false,
                                            ""
                                        )

                                        if(arr.containsAll(getPlayers))
                                        {
                                            Log.e("TAG", "onResponse: same data" )

                                            dbSqliteHelper.updatePlayer(playerAdd, positionListId.toString(), filterCode)
                                        }
                                        else
                                        {
                                            status = dbSqliteHelper.insterPlayer(playerAdd,filterCode,positionListId.toString())
                                        }


                                        getPlayers.add(playerAdd)
                                        binding.pbBar.visibility = View.GONE

                                    }

                                }


                                showData()

                            } else {
                                progressDialogue.dismiss()
                                binding.pbBar.visibility = View.GONE
                                MessageForValid("" + response.message().toString())
                            }
                        } else {
                            progressDialogue.dismiss()
                            binding.pbBar.visibility = View.GONE
                            val errorResponse = Gson().fromJson(response.errorBody()!!.toString(),
                                LoginErrorResponse::class.java)
                            MessageForValid("" + errorResponse.message)
                        }
                    }

                    override fun onFailure(call: Call<PlayerListResponse>, t: Throwable) {
                        call.cancel()
                    }

                })

        }


    }

    private fun searchPlayerList() {

        binding.actSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
            {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {

            }
            override fun afterTextChanged(s: Editable?) {



                if (s != null)
                {
                    if (binding.actSearch.text.toString() == "")
                    {
                        showData()
                        //getServices()
                    }
                    else
                    {
                        searchButtonClick()
                    }
                }

            }

        })

    }

    private fun searchButtonClick()
    {

        binding.ivSearch.setOnClickListener{
            if (binding.actSearch.text.toString() == "" || binding.actSearch.text.isEmpty())
            {
                //getServices()
                showData()
                //MessageForValid("Can not be blank.")
            }
            else
            {
                getSearchPlayers(filterCode.toInt(),binding.actSearch.text.toString())
            }
        }

        binding.actSearch.setOnKeyListener { v, keyCode, event ->
            when {

                //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {

                    if (binding.actSearch.text.toString() == "" || binding.actSearch.text.isEmpty())
                    {
                        showData()
                      //  getServices()
                    }
                    else
                    {
                        getSearchPlayers(filterCode.toInt(),binding.actSearch.text.toString())
                    }

                    return@setOnKeyListener true
                }
                else -> false
            }
        }
    }

    private fun getSearchPlayers(filter: Int, searchText: String)
    {
       progressDialoge()
       apiInterface.getSearchPlayer("Bearer $HeaderToken",searchText, ""+postingCodeLeague,filter.toString(),leagueStartDate, "0").enqueue(object : Callback<PlayerListResponse>{
            override fun onResponse(
                call: Call<PlayerListResponse>,
                response: Response<PlayerListResponse>,
            )
            {
                val searchResponse = response.body()
                if (searchResponse != null)
                {
                    if (searchResponse.success)
                    {

                        tenElement.clear()
                        tenElement.addAll(searchResponse.playerList)

                        CheckingAllData()
                        showData()

                        binding.rvPlayersColumn.scrollToPosition(1)
                        binding.rvPlayers.scrollToPosition(1)
                        binding.scrollNested.scrollTo(0, 0)

                        progressDialogue.dismiss()

                    }
                    else
                    {
                        MessageForValid(""+response.message())
                        Log.d("SearchResponse",""+response.message())
                        progressDialogue.dismiss()
                    }
                }
                else
                {
                    MessageForValid(""+response.message())
                    Log.d("SearchResponse",""+response.message())
                    progressDialogue.dismiss()
                }

            }
            override fun onFailure(call: Call<PlayerListResponse>, t: Throwable) {
                call.cancel()
            }
        })
    }

    private fun MessageForValid(s: String) {
        val snackbar = TSnackbar.make(findViewById(android.R.id.content), s, TSnackbar.LENGTH_SHORT)
        // snackbar.setActionTextColor(Color.WHITE)
        val snackbarView = snackbar.view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackbarView.setBackgroundColor(getColor(R.color.bg))
        }
        snackbar.setIconLeft(R.drawable.ic_warning, 18F)
        snackbar.setIconPadding(10)
        // snackbar.setIconPadding(5)

        val textView =
            snackbarView.findViewById<View>(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        // textView.compoundDrawablePadding = 5
        textView.textSize = 12F
        snackbar.show()
    }

    @SuppressLint("InflateParams")
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

    private fun allocateMemory()
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            binding.rvNestedHorizontal.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

                binding.hvScrollHeader.smoothScrollTo(oldScrollX, oldScrollY)
                binding.hvScrollHeader.scrollTo(oldScrollX, oldScrollY)

            }
        }


        dbSqliteHelper = DbSqliteHelper(applicationContext)

        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(applicationContext)
        arrPlayerList = ArrayList<Player>()

        playerLinerLayoutManager =  LinearLayoutManager(this)
//        playerLinerLayoutManager = LinearLayoutManager(this)
        playerColumnLinerLayoutManager = LinearLayoutManager(this)

        leagueStartDate = sharedPrefManager.getLeague.league_start_date

        binding.rvPlayers.layoutManager = playerLinerLayoutManager
        binding.rvPlayersColumn.layoutManager = playerColumnLinerLayoutManager

        (binding.rvPlayers.layoutManager as LinearLayoutManager).isAutoMeasureEnabled
        (binding.rvPlayersColumn.layoutManager as LinearLayoutManager).isAutoMeasureEnabled

    }

    private fun argumentsData()
    {
        postingCodeLeague = intent.getStringExtra("PG").toString()
        League_id_Position = intent.getStringExtra("league_id").toString()
        Position_id = intent.getStringExtra("position_id").toString()
        positionListId = intent.getIntExtra("positionListId", 0)

        binding.tvLeagueName.text = postingCodeLeague

        if (sharedPrefManager.ULoggedIn) {
            HeaderToken = sharedPrefManager.getLogin.access_token
            User_id = sharedPrefManager.getLogin.userDetails.id.toString()
        } else if (sharedPrefManager.RLoggedIn) {
            HeaderToken = sharedPrefManager.getRegister.access_token
            User_id = sharedPrefManager.getLogin.userDetails.id.toString()
        } else {
            HeaderToken = ""
            User_id = ""
        }

        totalSmall = intent.getIntExtra("totalSmall", 0)
        totalStarter = intent.getIntExtra("totalStarter", 0)
        totalStar = intent.getIntExtra("totalStar", 0)
        remainSalary = intent.getIntExtra("remainSalary", 0)

    }

    private fun CustomHeader() {
        iv_menu = findViewById(R.id.iv_menu)
        iv_back = findViewById(R.id.iv_back)
        tv_name_app = findViewById(R.id.tv_name_app)
        iv_menu.visibility = View.GONE

        iv_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onPlayerClick(position: Int, player: Player) {
        val intent = Intent(applicationContext, PgUserActivity::class.java)
        intent.putExtra("pid", "" + player.pid)
        startActivity(intent)
    }

    override fun OnAddPlayerClick(position: Int, player: Player) {
        val isEnable = player.isEnable ?: false
        if (!isEnable) {
            return
        }


        val strSalary = player.salary
        var playerSalary: Int

        if (strSalary.equals("-") || strSalary.equals("0")) {
            playerSalary = 0
        } else {
            playerSalary = strSalary.toInt()
        }
        progressDialoge()
        apiInterface.addToDraft("Bearer $HeaderToken",
            Add_Player_Draft(League_id_Position,
                player.pid.toString(),
                player.player_id,
                Position_id,
                User_id,
                playerSalary)).enqueue(object : Callback<Add_Player_Draft_Response> {
            override fun onResponse(
                call: Call<Add_Player_Draft_Response>,
                response: Response<Add_Player_Draft_Response>,
            ) {
                if (response.isSuccessful) {
                    Log.d("krunal", " legaue code sending" + postingCodeLeague)
                    if (response.body()!!.success) {
                        progressDialogue.dismiss()
                        Log.d("krunal", "  if response " + response.body())
                        MessageForValid("" + response.message().toString())
                        val intent = Intent(applicationContext, LeagueActivity::class.java)
                        intent.putExtra("league_name", "" + postingCodeLeague)
                        intent.putExtra("league_id", "" + League_id_Position)
                        startActivity(intent)

                    } else {
                        progressDialogue.dismiss()
                        binding.pbBar.visibility = View.GONE
                        Log.d("krunal", " else response " + response.body())
                        val errorResponse = Gson().fromJson(response.errorBody()!!.charStream(),
                            LoginErrorResponse::class.java)
                        MessageForValid("" + errorResponse.message)
                    }
                } else {
                    progressDialogue.dismiss()
                    binding.pbBar.visibility = View.GONE
                    Log.d("krunal", " else main response " + response.body())
                    MessageForValid("" + response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<Add_Player_Draft_Response>, t: Throwable) {
                call.cancel()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null)
        {
            val item = arrStatus[position]
            if (item == "This Season")
            {
                Handler(Looper.getMainLooper()).postDelayed(Runnable
                {
                    getPlayerList(0)
                }, 500)

            }
            else if (item == "Last 7 Games")
            {

                Handler(Looper.getMainLooper()).postDelayed(Runnable
                {
                    getPlayerList(1)
                }, 500)
            }
            else if (item == "Last 3 Games")
            {
                filterCode = "2"
                Handler(Looper.getMainLooper()).postDelayed(Runnable
                {
                    getPlayerList(2)
                }, 500)
            }
            else if (item == "Last Game")
            {
                filterCode = "3"
                Handler(Looper.getMainLooper()).postDelayed(Runnable
                {
                    getPlayerList(3)
                }, 500)

            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?)
    {

    }


}
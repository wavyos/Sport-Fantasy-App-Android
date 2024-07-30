package com.example.sportsfantasy.Activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidadvance.topsnackbar.TSnackbar
import com.example.sportsfantasy.Adapter.NewPlayerListAdapter
import com.example.sportsfantasy.DataStorage.DbSqliteHelper
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Model.*
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.Utils.Constant
import com.example.sportsfantasy.databinding.ActivitySelectPlayerBinding
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SelectPlayerActivity : BaseActivity() , ClickPickListener
{

    private lateinit var newPLayerListAdapter : NewPlayerListAdapter

    var arrPlayerListApi = ArrayList<Player>()
    var arrPlayerListSQL = ArrayList<Player>()

    var arrSearchPlayerList = ArrayList<Player>()
    var arrAllPlayerList = ArrayList<Player>()

    lateinit var apiInterface: ApiInterface
    lateinit var dbSqliteHelper: DbSqliteHelper
    lateinit var sharedPrefManager: SharedPrefManager
    lateinit var binding:ActivitySelectPlayerBinding
    lateinit var progressDialogue: Dialog

    var playersList = ArrayList<Player>()
    var playerDetailsListAll = ArrayList<ArrayList<String?>>()
    var playerDetailsList = ArrayList<ArrayList<String?>>()
    var playerDetailsSQL = ArrayList<ArrayList<String?>>()
    var playerDetailsSearch = ArrayList<ArrayList<String?>>()

    var arrStatus = ArrayList<String>()
    var filtercode:Int = 0
    var positionListId = 0
    var postingCodeLeague: String = ""
    lateinit var HeaderToken: String
    var League_id_Position: String = ""
    var leagueStartDate: String = ""
    var Position_id: String = ""
    var User_id: String = ""

    var allowAdd = true

    var totalSmall = 0
    var totalStarter = 0
    var totalStar = 0
    var remainSalary = 0
    var draftID = 0

    var status: Long = 0
    var nextEnd = ""

    lateinit var iv_menu: ImageView
    lateinit var iv_back: ImageView
    lateinit var tv_name_app: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AllocateMemory()
        CustomHeader()


        arrStatus.add(getString(R.string.str_this_season))
        arrStatus.add(getString(R.string.str_last_7_games))
        arrStatus.add(getString(R.string.str_last_3_games))
        arrStatus.add(getString(R.string.str_last_game))

        getStatusData()
        getSearchPlayer()

        nextEnd = if(allowAdd){
            "0"
        } else {
            "1"
        }

        getDataFromApiBackground(filtercode,positionListId)

    }

    private fun getAllPlayerAPi(postingCodeLeague: String, filtercode: Int)
    {
        progressDialoge()
        apiInterface.getPlayerList("Bearer $HeaderToken", postingCodeLeague, filtercode.toString(),leagueStartDate, nextEnd).enqueue(object :Callback<PlayerListResponse>{
            override fun onResponse(call: Call<PlayerListResponse>, response: Response<PlayerListResponse>)
            {
                val playerResponse = response.body()
                if (playerResponse != null)
                {
                    if (playerResponse.success)
                    {
                        Log.e("List", "onResponse: "+playerResponse.playerList )
                        arrAllPlayerList.clear()
                        if (playerResponse.playerList.isNotEmpty())
                        {
                            arrAllPlayerList.addAll(playerResponse.playerList)
                            showAllPlayer(arrAllPlayerList)
                        }

                        progressDialogue.dismiss()
                    }
                    else
                    {
                        progressDialogue.dismiss()
                        Log.e("TAG", "onResponse: "+response.message() );
                    }
                }
            }
            override fun onFailure(call: Call<PlayerListResponse>, t: Throwable) {
                progressDialogue.dismiss()
                call.cancel()
            }

        })

    }

    private fun showAllPlayer(arrListPlayer: ArrayList<Player>)
    {
        for (player in arrListPlayer)
        {
            val strSalary = player.salary
            var playerSalary = 0;

            if (strSalary == "-" || strSalary == "0")
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

        playerDetailsListAll.clear()
        for (i in arrListPlayer)
        {
                val playerDetails = ArrayList<String?>()
                val points = i.points
                val salary = i.salary
                val FGM = i.FGM
                val FG = i.FG
                val FT = i.FT
                val Reb = i.Reb
                val Ast = i.Ast
                val BLK = i.BLK
                val STL = i.STL
                val TOV = i.TOV
//            val rFG = if (FG.equals("-")) "-" else FG.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
                playerDetails.add("")
                val rSalary = if(salary.equals("-")|| salary.isEmpty()) "$ 0" else "$ $salary"
                val rPoints = if (points.equals("-")) "-" else "%.2f".format(points.toDouble())
                val rFGM = if (FGM.equals("-")) "-" else "%.2f".format(FGM.toDouble())
                val rFG = if (FG.equals("-")) "-"  else "%.2f".format(FG.toDouble())
                val rFT = if (FT.equals("-")) "-"  else "%.2f".format(FT.toDouble())
                val rReb =if (Reb.equals("-")) "-"  else "%.2f".format(Reb.toDouble())
                val rAst = if (Ast.equals("-")) "-" else "%.2f".format(Ast.toDouble())
                val rBLK = if (BLK.equals("-")) "-" else "%.2f".format(BLK.toDouble())
                val rSTL = if (STL.equals("-")) "-" else "%.2f".format(STL.toDouble())
                val rTOV = if (TOV.equals("-")) "-" else "%.2f".format(TOV.toDouble())

                playerDetails.add(rPoints)
                playerDetails.add(rSalary)
                playerDetails.add(rFGM)
                playerDetails.add(rFG)
                playerDetails.add(rReb)
                playerDetails.add(rFT)
                playerDetails.add(rBLK)
                playerDetails.add(rAst)
                playerDetails.add(rSTL)
                playerDetails.add(rTOV)
                playerDetailsListAll.add(playerDetails)
        }

        Log.d("MainActivity", "Data prepared " + playerDetailsListAll)

        //new flow for add new design
        SetRecyclerViewData(arrListPlayer, remainSalary)

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

    private fun removePLayerFromDraft(){
        progressDialoge()
        apiInterface.removeFromDraft("Bearer $HeaderToken",""+draftID).enqueue(object :Callback<Draft_Remove_Response>{

            override fun onResponse(call: Call<Draft_Remove_Response>, response: Response<Draft_Remove_Response>)
            {
                if (response.isSuccessful)
                {
                    progressDialogue.dismiss()
                    if (response.body()!!.success==true)
                    {
                        ShowDataFromApi(filtercode,positionListId)
                    }
                    else
                    {
                        MessageForValid(""+ response.body()!!.message.toString())
                    }
                }
                else
                {
                    progressDialogue.dismiss()
                    val errorResponse = Gson().fromJson(response.errorBody()!!.charStream(), LoginErrorResponse::class.java)
                    MessageForValid(""+ errorResponse.message.toString())
                }
            }
            override fun onFailure(call: Call<Draft_Remove_Response>, t: Throwable) {
                progressDialogue.dismiss()
                call.cancel()
            }

        })
    }

    private fun getSearchPlayer()
    {
        binding.actSearch.addTextChangedListener(object : TextWatcher {
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
                        MessageForValid("Text Can not be empty")
                        ShowDataFromApi(filtercode, positionListId)
                    }
                    else
                    {
                        getSearchApi(filtercode,binding.actSearch.text.toString().trim())
                    }
                }

            }

        })

        binding.actSearch.setOnKeyListener { v, keyCode, event ->
            when {

                //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN) && (event.action == KeyEvent.KEYCODE_SEARCH)) ->
                {

                    if (binding.actSearch.text.toString() == "" || binding.actSearch.text.isEmpty())
                    {
                        MessageForValid("Text Can not be empty")
                        ShowDataFromApi(filtercode, positionListId)
                        //  getServices()
                    }
                    else
                    {
                        getSearchApi(filtercode,binding.actSearch.text.toString().trim())
                    }

                    return@setOnKeyListener true
                }
                else -> false
            }
        }

        binding.ivSearch.setOnClickListener {
            val searchText = binding.actSearch.text.trim()
            if (searchText == "") {
                MessageForValid("Text Can not be empty")
            }
            else {
                getSearchApi(filtercode,binding.actSearch.text.toString().trim())
            }
        }
    }

    private fun getSearchApi(filtercode: Int, searchtext: String)
    {

        val newFilteredList = arrPlayerListSQL.filter { itm -> itm.fullname!!.contains(searchtext, true) } as ArrayList<Player>
//        newPLayerListAdapter.updateData(newFilteredList)
        showSearchPlayer(newFilteredList)
        /*apiInterface.getSearchPlayer("Bearer $HeaderToken",searchtext, ""+postingCodeLeague,filtercode.toString(),leagueStartDate, nextEnd)
            .enqueue(object : Callback<PlayerListResponse>{
                override fun onResponse(call: Call<PlayerListResponse>, response: Response<PlayerListResponse>)
                {
                    val searchResponse = response.body()
                    if (searchResponse != null)
                    {
                        if (searchResponse.success)
                        {
                            arrSearchPlayerList.clear()
                            if (searchResponse.playerList.isNotEmpty())
                            {
                                arrSearchPlayerList.addAll(searchResponse.playerList)
                                showSearchPlayer(arrSearchPlayerList)
                            }

                            progressDialogue.dismiss()
                        }
                        else
                        {
                            MessageForValid(""+response.message())
                            progressDialogue.dismiss()
                        }
                    }
                    else
                    {
                        progressDialogue.dismiss()
                    }
                }

                override fun onFailure(call: Call<PlayerListResponse>, t: Throwable) {
                    progressDialogue.dismiss()
                    call.cancel()
                }
            })*/
    }

    private fun showSearchPlayer(arrSearchList: ArrayList<Player>)
    {

        for (player in arrSearchList)
        {
            val strSalary = player.salary
            var playerSalary = 0;

            playerSalary = if (strSalary == "-" || strSalary == "0")
            {
                0
            } else {
                strSalary.toInt()
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

        for (i in arrSearchList)
        {

            if (arrSearchList.isNotEmpty())
            {
                val playerDetails = ArrayList<String?>()
               val points = i.points
               val salary = i.salary
                val FGM = i.FGM
                 val FG = i.FG
                 val FT = i.FT
                val Reb = i.Reb
                val Ast = i.Ast
                val BLK = i.BLK
                val STL = i.STL
                val TOV = i.TOV
//            val rFG = if (FG.equals("-")) "-" else FG.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
                playerDetails.add("")
                val rSalary = if(salary.equals("-")|| salary.isEmpty()) "$ 0" else "$ $salary"
                val rFGM = if (FGM.equals("-")) "-" else "%.2f".format(FGM.toDouble())
                val rFG = if (FG.equals("-")) "-"  else "%.2f".format(FG.toDouble())
                val rFT = if (FT.equals("-")) "-"  else "%.2f".format(FT.toDouble())
                val rReb =if (Reb.equals("-")) "-"  else "%.2f".format(Reb.toDouble())
                val rAst = if (Ast.equals("-")) "-" else "%.2f".format(Ast.toDouble())
                val rBLK = if (BLK.equals("-")) "-" else "%.2f".format(BLK.toDouble())
                val rSTL = if (STL.equals("-")) "-" else "%.2f".format(STL.toDouble())
                val rTOV = if (TOV.equals("-")) "-" else "%.2f".format(TOV.toDouble())

                playerDetails.add(points)
                playerDetails.add(rSalary)
                playerDetails.add(rFGM)
                playerDetails.add(rFG)
                playerDetails.add(rFT)
                playerDetails.add(rReb)
                playerDetails.add(rAst)
                playerDetails.add(rBLK)
                playerDetails.add(rSTL)
                playerDetails.add(rTOV)
                playerDetailsSearch.add(playerDetails)
            }

        }

        Log.d("MainActivity", "Data prepared "+ playerDetailsSearch)

        //new flow for add new design
        SetRecyclerViewData(arrSearchList, remainSalary)
    }

    private fun AllocateMemory() {
        playersList = ArrayList<Player>()
        playerDetailsList = ArrayList()
        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
        dbSqliteHelper = DbSqliteHelper(applicationContext)
        sharedPrefManager = SharedPrefManager.getInstance(applicationContext)

        postingCodeLeague = intent.getStringExtra("PG").toString()
        League_id_Position = intent.getStringExtra("league_id").toString()
        Position_id = intent.getStringExtra("position_id").toString()
        positionListId = intent.getIntExtra("positionListId", 0)
        allowAdd = intent.getBooleanExtra("allowAdd",true)

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        leagueStartDate = currentDate

        when(postingCodeLeague){
            "C"->{
                binding.tvLeagueName.text = getString(R.string.select_c)
            }
            "PF"->{
                binding.tvLeagueName.text = getString(R.string.select_pf)
            }
            "PG"->{
                binding.tvLeagueName.text = getString(R.string.select_pg)
            }
            "SF"->{
                binding.tvLeagueName.text = getString(R.string.select_sf)
            }
            "SG"->{
                binding.tvLeagueName.text = getString(R.string.select_sg)
            }
            else->{
                binding.tvLeagueName.text = getString(R.string.select) + "ALL"
            }
        }


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
        if (intent.hasExtra("draft_id")){
            draftID = intent.getIntExtra("draft_id", 0)
        }

        binding.tvSalaryCap.text = "Salary Cap : $$remainSalary"

    }

    private fun getStatusData()
    {
        val statusAdaper: ArrayAdapter<*> =
            ArrayAdapter<Any?>(this, R.layout.color_spinner_layout, arrStatus as List<Any?>)
        statusAdaper.setDropDownViewResource(R.layout.spinner_dropdown_layout)
        binding.actStatus.adapter = statusAdaper

        binding.actStatus.onItemSelectedListener = object :OnItemSelectedListener,
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                filtercode = position

                if (postingCodeLeague == "all")
                {
                    Log.e("TAG", "onCreate: $postingCodeLeague")
                    getAllPlayerAPi(postingCodeLeague,filtercode)
                }
                else
                {
                    SqliteShowDataFilter(position,positionListId)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                TODO("Not yet implemented")
            }

        }
    }


    private fun SqliteShowDataFilter(position: Int, positionListId: Int)
    {
        Log.e("SQL", "SqliteShowDataFilter: position "+position )
        Log.e("SQL", "SqliteShowDataFilter:  positionListId "+positionListId)
        arrPlayerListSQL = dbSqliteHelper.getPlayersAllData(positionListId,filtercode)
        if (arrPlayerListSQL.isEmpty())
        {
            //call api
            Log.e("API", "SqliteShowDataFilter: postion "+position )
            Log.e("API", "SqliteShowDataFilter: positionlist id "+positionListId )
            getDataFromApi(position,positionListId)
            //Toast.makeText(applicationContext,"Empty"+arrPlayerListSQL.size,Toast.LENGTH_SHORT).show()
        }
        else
        {
            playerDetailsSQL.clear()
            playersList.addAll(arrPlayerListSQL)
            CheckingAllData(arrPlayerListSQL)
            for (i in arrPlayerListSQL)
            {
                val playerDetails = ArrayList<String?>()
              //  Log.e("APL", "getPlayersData: $i")
//                val points = if (i.points != "-") "%.2f".format(i.points.toDouble()) else "0.0"

//                val points = "%.2f".format(i.points.toDouble())
                val points = if (i.points.equals("-")) "-" else "%.2f".format(i.points.toDouble())
                val salary = i.salary
                val FGM = i.FGM
                val FG = i.FG
                val FT = i.FT
                val Reb = i.Reb
                val Ast = i.Ast
                val BLK = i.BLK
                val STL = i.STL
                val TOV = i.TOV
//            val rFG = if (FG.equals("-")) "-" else FG.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
                playerDetails.add("")
                val rSalary = if(salary.equals("-")|| salary.isEmpty()) "$ 0" else "$ $salary"
                val rFGM = if (FGM.equals("-")) "-" else "%.2f".format(FGM.toDouble())
                val rFG = if (FG.equals("-")) "-"  else "%.2f".format(FG.toDouble())
                val rFT = if (FT.equals("-")) "-"  else "%.2f".format(FT.toDouble())
                val rReb =if (Reb.equals("-")) "-"  else "%.2f".format(Reb.toDouble())
                val rAst = if (Ast.equals("-")) "-" else "%.2f".format(Ast.toDouble())
                val rBLK = if (BLK.equals("-")) "-" else "%.2f".format(BLK.toDouble())
                val rSTL = if (STL.equals("-")) "-" else "%.2f".format(STL.toDouble())
                val rTOV = if (TOV.equals("-")) "-" else "%.2f".format(TOV.toDouble())

                playerDetails.add(points)
                playerDetails.add(rSalary)
                playerDetails.add(rFGM)
                playerDetails.add(rFG)
                playerDetails.add(rReb)
                playerDetails.add(rFT)
                playerDetails.add(rBLK)
                playerDetails.add(rAst)
                playerDetails.add(rSTL)
                playerDetails.add(rTOV)
                playerDetailsSQL.add(playerDetails)
            }

            Log.d("MainActivity", "Data prepared "+playerDetailsSQL)

            //new flow for add new design
            SetRecyclerViewData(arrPlayerListSQL, remainSalary)
        }
    }

    private fun getDataFromApi(position: Int, positionListId: Int)
    {
        progressDialoge()
            apiInterface.getPlayerList("Bearer $HeaderToken", postingCodeLeague, position.toString(),leagueStartDate, nextEnd).enqueue(object :Callback<PlayerListResponse>{
            override fun onResponse(call: Call<PlayerListResponse>, response: Response<PlayerListResponse>)
            {
                val playerResponse = response.body()
                if (playerResponse != null)
                {
                    if (playerResponse.success)
                    {
                        val arrResponseList = playerResponse.playerList

                        try {
                            for (i in arrResponseList)
                            {
                                var playerAdd = Player(
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

                                dbSqliteHelper.insterPlayer(playerAdd,position,positionListId.toString())
                                Log.e("filterCode", "onResponse: "+position )
                            }

                            Log.e("lc", "onResponse: "+response.message() )
                            progressDialogue.dismiss()

                            Log.e("Krunal", "onResponse: "+filtercode )
                            Log.e("Krunal", "onResponse: "+positionListId )
                            ShowDataFromApi(filtercode,positionListId)
                        }catch (er:Exception){
                            progressDialogue.dismiss()
                        }

                    }
                    else
                    {
                        progressDialogue.dismiss()
                        Log.e("TAG", "onResponse: "+response.message() );
                    }
                }
            }
            override fun onFailure(call: Call<PlayerListResponse>, t: Throwable) {
                progressDialogue.dismiss()
                call.cancel()
            }

        })

    }


    private fun getDataFromApiBackground(position: Int, positionListId: Int)
    {
        apiInterface.getPlayerList("Bearer $HeaderToken", postingCodeLeague, position.toString(),leagueStartDate,nextEnd).enqueue(object :Callback<PlayerListResponse>{
            override fun onResponse(call: Call<PlayerListResponse>, response: Response<PlayerListResponse>)
            {
                val playerResponse = response.body()
                if (playerResponse != null)
                {
                    if (playerResponse.success)
                    {
                        val arrResponseList = playerResponse.playerList

                        try {
                            for (i in arrResponseList)
                            {
                                var playerAdd: Player = Player(
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
                            }
                        }catch (er:Exception){
                        }
                    }
                    else
                    {
                        Log.e("TAG", "onResponse: "+response.message() );
                    }
                }
            }
            override fun onFailure(call: Call<PlayerListResponse>, t: Throwable) {
                call.cancel()
            }
        })
    }

    private fun ShowDataFromApi(filtercode: Int, positionListId: Int)
    {
        playersList.clear()
        arrPlayerListApi = dbSqliteHelper.getPlayersAllData(positionListId,filtercode)
        playersList.addAll(arrPlayerListApi)
        CheckingAllData(arrPlayerListApi)
        playerDetailsList.clear()
        for (i in arrPlayerListApi)
        {
            val playerDetails = ArrayList<String?>()
          //  Log.e("APL", "getPlayersData: $i")
            val points = if (i.points.equals("-")) "-" else "%.2f".format(i.points.toDouble())
            val salary = i.salary
            val FGM = i.FGM
            val FG = i.FG
            val FT = i.FT
            val Reb = i.Reb
            val Ast = i.Ast
            val BLK = i.BLK
            val STL = i.STL
            val TOV = i.TOV
//            val rFG = if (FG.equals("-")) "-" else FG.toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
            playerDetails.add("")
            val rSalary = if(salary.equals("-")|| salary.isEmpty()) "$ 0" else "$ $salary"
            val rFGM = if (FGM.equals("-")) "-" else "%.2f".format(FGM.toDouble())
            val rFG = if (FG.equals("-")) "-"  else "%.2f".format(FG.toDouble())
            val rFT = if (FT.equals("-")) "-"  else "%.2f".format(FT.toDouble())
            val rReb =if (Reb.equals("-")) "-"  else "%.2f".format(Reb.toDouble())
            val rAst = if (Ast.equals("-")) "-" else "%.2f".format(Ast.toDouble())
            val rBLK = if (BLK.equals("-")) "-" else "%.2f".format(BLK.toDouble())
            val rSTL = if (STL.equals("-")) "-" else "%.2f".format(STL.toDouble())
            val rTOV = if (TOV.equals("-")) "-" else "%.2f".format(TOV.toDouble())

            playerDetails.add(points)
            playerDetails.add(rSalary)
            playerDetails.add(rFGM)
            playerDetails.add(rFG)
            playerDetails.add(rFT)
            playerDetails.add(rReb)
            playerDetails.add(rAst)
            playerDetails.add(rBLK)
            playerDetails.add(rSTL)
            playerDetails.add(rTOV)

            playerDetailsList.add(playerDetails)
        }

        Log.d("MainActivity", "Data prepared"+playerDetailsList)

        //new flow for add new design
        SetRecyclerViewData(playersList, remainSalary)
    }

    private fun SetRecyclerViewData(
        arrPlayerListApi: ArrayList<Player>,
        remainSalary: Int
    ) {
        binding.rvNewPlayerList.invalidate()
        binding.rvNewPlayerList.layoutManager = LinearLayoutManager(this)
        newPLayerListAdapter = NewPlayerListAdapter(
            this,
            arrPlayerListApi,
            remainSalary,
            this,
            ""
        )
        binding.rvNewPlayerList.adapter = newPLayerListAdapter
        newPLayerListAdapter.notifyDataSetChanged()
    }

    private fun CheckingAllData(arrayListCheck: ArrayList<Player>) {

        for (player in arrayListCheck)
        {
            val strSalary = player.salary
            var playerSalary = 0;


            if (strSalary == "-" || strSalary == "0")
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
                Constant.PLAYER_TYPE.small -> player.isEnable = totalSmall < 1 && playerSalary <= remainSalary
                Constant.PLAYER_TYPE.starter -> player.isEnable = totalStarter < 2 && playerSalary <= remainSalary
                Constant.PLAYER_TYPE.star -> player.isEnable = totalStar < 2 && playerSalary <= remainSalary
            }

        }
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

        val textView =
            snackbarView.findViewById<View>(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        // textView.compoundDrawablePadding = 5
        textView.textSize = 12F
        snackbar.show()
    }

    override fun pickUnpickClick(data: Player, isUnpick: Boolean) {
        if(isUnpick){
            removePLayerFromDraft()
        } else {
            progressDialoge()
            apiInterface.addToDraft("Bearer $HeaderToken",
                Add_Player_Draft(League_id_Position,
                    data.pid.toString(),
                    data.player_id,
                    Position_id,
                    User_id,
                    data.salary.toInt())
            ).enqueue(object : Callback<Add_Player_Draft_Response> {
                override fun onResponse(
                    call: Call<Add_Player_Draft_Response>,
                    response: Response<Add_Player_Draft_Response>,
                ) {
                    if (response.isSuccessful) {
                        if (response.body()!!.success) {
                            MessageForValid("" + response.message().toString())
//                            val intent = Intent(applicationContext, LeagueActivity::class.java)
//                            intent.putExtra("league_name", "" + postingCodeLeague);
//                            intent.putExtra("league_id", "" + League_id_Position);
//                            startActivity(intent)
                            progressDialogue.dismiss()

                        } else {
                            progressDialogue.dismiss()
                            val errorResponse = Gson().fromJson(response.errorBody()!!.charStream(),
                                LoginErrorResponse::class.java)
                            MessageForValid("" + errorResponse.message)
                        }
                    } else {
                        progressDialogue.dismiss()
                        MessageForValid("" + response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<Add_Player_Draft_Response>, t: Throwable) {
                    progressDialogue.dismiss()
                    call.cancel()
                }

            })
        }
    }

}
interface ClickPickListener{
    fun pickUnpickClick(data:Player, isUnpick: Boolean)
}
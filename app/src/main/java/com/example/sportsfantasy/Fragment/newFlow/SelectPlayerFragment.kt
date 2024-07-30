package com.example.sportsfantasy.Fragment.newFlow

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidadvance.topsnackbar.TSnackbar
import com.example.sportsfantasy.Activities.ClickPickListener
import com.example.sportsfantasy.Adapter.NewPlayerListAdapter
import com.example.sportsfantasy.DataStorage.DbSqliteHelper
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Interface.BottomSheetItemClickListener
import com.example.sportsfantasy.Model.Add_Player_Draft
import com.example.sportsfantasy.Model.Add_Player_Draft_Response
import com.example.sportsfantasy.Model.Draft_Remove_Response
import com.example.sportsfantasy.Model.LoginErrorResponse
import com.example.sportsfantasy.Model.Player
import com.example.sportsfantasy.Model.PlayerListResponse
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.Utils.Constant
import com.example.sportsfantasy.Utils.GlobalDialogHelper
import com.example.sportsfantasy.databinding.FragmentSelectPlayerBinding
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date


class SelectPlayerFragment : Fragment(), ClickPickListener, BottomSheetItemClickListener {
    private lateinit var binding: FragmentSelectPlayerBinding

    private lateinit var newPLayerListAdapter : NewPlayerListAdapter
//    var arrPlayerListApi = ArrayList<Player>()
//    var arrPlayerListSQL = ArrayList<Player>()
//
//    var arrSearchPlayerList = ArrayList<Player>()
    var arrAllPlayerList = ArrayList<Player>()
    var playersList = ArrayList<Player>()

    lateinit var apiInterface: ApiInterface
    lateinit var dbSqliteHelper: DbSqliteHelper
    lateinit var sharedPrefManager: SharedPrefManager

    lateinit var progressDialogue: Dialog

    var playerDetailsListAll = ArrayList<ArrayList<String?>>()
    var playerDetailsList = ArrayList<ArrayList<String?>>()
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
    var p_id: String = "0"

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

    lateinit var gdb : GlobalDialogHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSelectPlayerBinding.inflate(layoutInflater)

        gdb = GlobalDialogHelper(requireActivity(), this)
        gdb.create()
        AllocateMemory()
        nextEnd = if(allowAdd){
            "0"
        } else {
            "1"
        }
        CustomHeader()


        arrStatus.add(getString(R.string.str_this_season))
        arrStatus.add(getString(R.string.str_last_7_games))
        arrStatus.add(getString(R.string.str_last_3_games))
        arrStatus.add(getString(R.string.str_last_game))
        getStatusData()
        getSearchPlayer()

        return binding.root
    }

    private fun getAllPlayerAPi(postingCodeLeague: String, filtercode: Int)
    {
        try {
            progressDialoge()
            apiInterface.getPlayerList("Bearer $HeaderToken", postingCodeLeague, filtercode.toString(),leagueStartDate, nextEnd).enqueue(object :
                Callback<PlayerListResponse> {
                override fun onResponse(call: Call<PlayerListResponse>, response: Response<PlayerListResponse>)
                {
                    val playerResponse = response.body()
                    if (playerResponse != null)
                    {
                        if (playerResponse.success)
                        {
                            Log.e("List", "onResponse: "+playerResponse.playerList )
                            if (playerResponse.playerList.isNotEmpty())
                            {
                                progressDialogue.dismiss()
                                binding.rvNewPlayerList.invalidate()
                                arrAllPlayerList.clear()
                                arrAllPlayerList.addAll(playerResponse.playerList)
//                                showAllPlayer(arrAllPlayerList)
                                ShowDataFromApi()
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
        }catch (er: Exception){
            progressDialogue.dismiss()
            Log.d("getAllPlayerAPi: ", ""+er.message.toString())
        }

    }

    private fun CustomHeader() {
        iv_menu = binding.root.findViewById(R.id.iv_menu)
        iv_back = binding.root.findViewById(R.id.iv_back)
        tv_name_app = binding.root.findViewById(R.id.tv_name_app)
        iv_menu.visibility = View.GONE

        when(postingCodeLeague){
            "C"->{
                tv_name_app.text = getString(R.string.select_c)
            }
            "PF"->{
                tv_name_app.text = getString(R.string.select_pf)
            }
            "PG"->{
                tv_name_app.text = getString(R.string.select_pg)
            }
            "SF"->{
                tv_name_app.text = getString(R.string.select_sf)
            }
            "SG"->{
                tv_name_app.text = getString(R.string.select_sg)
            }
            else->{
                tv_name_app.text = getString(R.string.select) + "ALL"
            }
        }
        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun removePLayerFromDraft(isUnpick: Boolean) {
//        progressDialoge()
        apiInterface.removeFromDraft("Bearer $HeaderToken",""+draftID).enqueue(object :
            Callback<Draft_Remove_Response> {
            override fun onResponse(call: Call<Draft_Remove_Response>, response: Response<Draft_Remove_Response>)
            {
                if (response.isSuccessful)
                {
//                    progressDialogue.dismiss()
                    if (response.body()!!.success)
                    {
//                        requireActivity().onBackPressed()
                        if(isUnpick){
                            getStatusData()
                        }
                    }
                    else
                    {
                        MessageForValid(""+ response.body()!!.message.toString())
                    }
                }
                else
                {
//                    progressDialogue.dismiss()
                    val errorResponse = Gson().fromJson(response.errorBody()!!.charStream(), LoginErrorResponse::class.java)
                    MessageForValid(""+ errorResponse.message.toString())
                }
            }
            override fun onFailure(call: Call<Draft_Remove_Response>, t: Throwable) {
//                progressDialogue.dismiss()
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
                        ShowDataFromApi()
                    }
                    else
                    {
                        getSearchApi(binding.actSearch.text.toString().trim())
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
                        ShowDataFromApi()
                        //  getServices()
                    }
                    else
                    {
                        getSearchApi(binding.actSearch.text.toString().trim())
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
                getSearchApi(binding.actSearch.text.toString().trim())
            }
        }
    }

    private fun getSearchApi(searchtext: String)
    {
        try {
            val newFilteredList = arrAllPlayerList.filter { itm -> itm.fullname != null && itm.fullname.contains(searchtext, true) } as ArrayList<Player>
            showSearchPlayer(newFilteredList)
        }catch (er:Exception){
            Log.d("getSearchApi: ", ""+er.message.toString())
        }
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
        dbSqliteHelper = DbSqliteHelper(requireActivity().applicationContext)
        sharedPrefManager = SharedPrefManager.getInstance(requireActivity().applicationContext)

        val args = arguments
        if (args != null) {
            postingCodeLeague = args.getString("PG").toString()
            League_id_Position = args.getString("league_id").toString()
            Position_id = args.getString("position_id").toString()
            positionListId = args.getInt("positionListId", 0)
            allowAdd = args.getBoolean("allowAdd",true)

            totalSmall = args.getInt("totalSmall", 0)
            totalStarter = args.getInt("totalStarter", 0)
            totalStar = args.getInt("totalStar", 0)
            remainSalary = args.getInt("remainSalary", 0)

            if (args.containsKey("draft_id")){
                draftID = args.getInt("draft_id", 0)
            }
            if (args.containsKey("pid")){
                p_id = args.getString("pid", "0")
            }
        }

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

//        totalSmall = intent.getIntExtra("totalSmall", 0)
//        totalStarter = intent.getIntExtra("totalStarter", 0)
//        totalStar = intent.getIntExtra("totalStar", 0)
//        remainSalary = intent.getIntExtra("remainSalary", 0)

        val styledText = "Salary Cap: <b><font color='#EB5902'>$remainSalary</font></b>"
        binding.tvSalaryCap.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE)

        binding.btnContinueDrafting.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun getStatusData()
    {
        getAllPlayerAPi(postingCodeLeague,filtercode)
    }

    private fun ShowDataFromApi()
    {
        binding.rvNewPlayerList.invalidate()
        playersList.clear()
        playersList.addAll(arrAllPlayerList)
        CheckingAllData(arrAllPlayerList)
        playerDetailsList.clear()
        for (i in arrAllPlayerList)
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
        binding.rvNewPlayerList.layoutManager = LinearLayoutManager(requireActivity())
        newPLayerListAdapter = NewPlayerListAdapter(requireActivity(), arrPlayerListApi, remainSalary, this, p_id)
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
                Constant.PLAYER_TYPE.small -> player.isEnable =
                    totalSmall < 1 && playerSalary <= remainSalary
                Constant.PLAYER_TYPE.starter -> player.isEnable =
                    totalStarter < 2 && playerSalary <= remainSalary
                Constant.PLAYER_TYPE.star -> player.isEnable =
                    totalStar < 2 && playerSalary <= remainSalary
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
        try {
            val snackbar = TSnackbar.make(binding.root.findViewById(android.R.id.content), s, TSnackbar.LENGTH_SHORT)
            // snackbar.setActionTextColor(Color.WHITE)
            val snackbarView = snackbar.view
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                snackbarView.setBackgroundColor(ContextCompat.getColor(requireActivity(),R.color.bg))
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
        }catch (er:Exception){

        }
    }

    override fun pickUnpickClick(data: Player, isUnpick: Boolean) {
        if(data.pid == p_id.toInt()){
            gdb.setDialogData(getString(R.string.app_name), "Are you sure you want to remove this player?", true)
        } else {
            if(p_id != "0"){
                p_id = "0"
                removePLayerFromDraft(false)
            }
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
                            p_id = "0"
                            p_id = data.pid.toString()
                            getStatusData()

                        } else {
                            val errorResponse = Gson().fromJson(response.errorBody()!!.charStream(),
                                LoginErrorResponse::class.java)
                            MessageForValid("" + errorResponse.message)
                        }
                    } else {
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

    override fun onBtnSheetItemClicked(buttonText: String) {
        gdb.dismiss()
        if(buttonText == "ok"){
            p_id = "0"
            removePLayerFromDraft(true)
        }
    }
}
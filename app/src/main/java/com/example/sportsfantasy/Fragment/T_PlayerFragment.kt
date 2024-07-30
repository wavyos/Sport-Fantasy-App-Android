package com.example.sportsfantasy.Fragment

import android.app.Dialog
import android.content.res.ColorStateList
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsfantasy.Activities.BottomNavigationActivity
import com.example.sportsfantasy.Adapter.PlayerTab.*
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Interface.BottomSheetItemClickListener
import com.example.sportsfantasy.Model.Player
import com.example.sportsfantasy.Model.PlayerF.*
import com.example.sportsfantasy.Model.PlayerListResponse
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.Utils.GlobalDialogHelperTwo
import com.example.sportsfantasy.databinding.FragmentTPlayerBinding
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date


class T_PlayerFragment : Fragment(), BottomSheetItemClickListener
{
    lateinit var binding:FragmentTPlayerBinding
    lateinit var apiInterface: ApiInterface
    lateinit var sharedPrefManager: SharedPrefManager
    lateinit var progressDialogue2: Dialog
    lateinit var custFilterDialoge: Dialog

    var playerAllData = ArrayList<Player>()

    var cPlayerData = ArrayList<PositionC>()
    var pfPlayerData = ArrayList<PositionPF>()
    var pgPlayerData = ArrayList<PositionPG>()
    var sfPlayerData = ArrayList<PositionSF>()
    var sgPlayerData = ArrayList<PositionSG>()

    var searchTabPlayerList = ArrayList<com.example.sportsfantasy.Model.SearchTab.Data>()

    var arrStatus = ArrayList<String>()

    var filterCode = 0

    var HeaderToken=""
    var User_id = ""
    var LeagueId = ""
    var UserName = ""
    var LeagueName = ""
    var currentSelection = ""

    lateinit var allPlayerNameAdapter: AllPlayerNameAdapter
    lateinit var allPlayerDetailsAdapter: AllPlayerDetailsAdapter

    //c player
    lateinit var cPlayerNameAdapter: cPlayerNameAdapter
    lateinit var cPlayerDetailsAdapter: cPlayerDetailsAdapter

    //pf player
    lateinit var pfPlayerNameAdapter: pfPlayerNameAdapter
    lateinit var pfPlayerDetailsAdapter: pfPlayerDetailsAdapter

    //pg player
    lateinit var pgPlayerNameAdapter:pgPlayerNameAdapter
    lateinit var pgPlayerDetailsAdapter:pgPlayerDetailsAdapter

    //sf player
    lateinit var sfPlayerNameAdapter:sfPlayerNameAdapter
    lateinit var sfPlayerDetailsAdapter:sfPlayerDetailsAdapter

    //sg player
    lateinit var sgPlayerNameAdapter:sgPlayerNameAdapter
    lateinit var sgPlayerDetailsAdapter:sgPlayerDetailsAdapter


    //search player
    lateinit var searchPlayerTabAdapter:searchPlayerTabAdapter
    val selectionArray = arrayOf(true, false, false, false, false, false)
    var leagueStartDate: String = ""

    private lateinit var dialogHelperTwo: GlobalDialogHelperTwo
    private val handler = Handler(Looper.getMainLooper())


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        binding = FragmentTPlayerBinding.inflate(layoutInflater)
        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(requireContext())
        Log.d("VRAJAN","No 3 fragment T_PlayerFragment")

        val sdf = SimpleDateFormat("yyyy-MM-dd")
        val currentDate = sdf.format(Date())
        leagueStartDate = currentDate


        arrStatus.add((requireActivity() as BottomNavigationActivity).resources.getString(R.string.str_this_season))
        arrStatus.add((requireActivity() as BottomNavigationActivity).resources.getString(R.string.str_last_7_games))
        arrStatus.add((requireActivity() as BottomNavigationActivity).resources.getString(R.string.str_last_3_games))
        arrStatus.add((requireActivity() as BottomNavigationActivity).resources.getString(R.string.str_last_game))

        allocateMemory()

        Arguments()

            setupPositionFilter()


//        getPlayerData(filterCode)
        FilterCodeDialoge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            HandlerScrollEvents()
        }
        MorePlayerEvents()
        searchButtonClick()

        binding.rvPlayersName
            .viewTreeObserver
            .addOnGlobalLayoutListener(
                object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        // At this point the layout is complete and the
                        // dimensions of recyclerView and any child views
                        progressDialogue2.dismiss()
                        // are known.
                        binding.rvPlayersName
                            .viewTreeObserver
                            .removeOnGlobalLayoutListener(this)
                    }
                })

        binding.rvPlayersDetails
            .viewTreeObserver
            .addOnGlobalLayoutListener(
                object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        // At this point the layout is complete and the
                        // dimensions of recyclerView and any child views
                        // are known.
                        progressDialogue2.dismiss()
                        binding.rvPlayersDetails
                            .viewTreeObserver
                            .removeOnGlobalLayoutListener(this)
                    }
                })

        return binding.root
    }

    private fun setupPositionFilter() {
        currentSelection = "all"
        setCurrentSelection(0)
        getAllPlayerAPi("all", filterCode)

        binding.tvAll.setOnClickListener {
            currentSelection = "all"
            setCurrentSelection(0)
            getAllPlayerAPi("all", filterCode)
        }
        binding.tvPG.setOnClickListener {
            currentSelection = "PG"
            setCurrentSelection(1)
            getAllPlayerAPi("PG", filterCode)
        }
        binding.tvC.setOnClickListener {
            currentSelection = "C"
            setCurrentSelection(5)
            getAllPlayerAPi("C", filterCode)
        }
        binding.tvPF.setOnClickListener {
            currentSelection = "PF"
            setCurrentSelection(4)
            getAllPlayerAPi("PF", filterCode)
        }
        binding.tvSF.setOnClickListener {
            currentSelection = "SF"
            setCurrentSelection(3)
            getAllPlayerAPi("SF", filterCode)
        }
        binding.tvSG.setOnClickListener {
            currentSelection = "SG"
            setCurrentSelection(2)
            getAllPlayerAPi("SG", filterCode)
        }
    }

    private fun setCurrentSelection(i: Int) {
        when(i){
            0 -> {
                binding.tvAll.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.bg))
                binding.tvAll.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

                binding.tvPG.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvPG.backgroundTintList = null
                binding.tvPG.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvSG.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvSG.backgroundTintList = null
                binding.tvSG.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvSF.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvSF.backgroundTintList = null
                binding.tvSF.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvPF.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvPF.backgroundTintList = null
                binding.tvPF.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvC.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvC.backgroundTintList = null
                binding.tvC.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))
            }
            1 -> {
                binding.tvAll.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvAll.backgroundTintList = null
                binding.tvAll.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvPG.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.bg))
                binding.tvPG.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

                binding.tvSG.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvSG.backgroundTintList = null
                binding.tvSG.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvSF.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvSF.backgroundTintList = null
                binding.tvSF.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvPF.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvPF.backgroundTintList = null
                binding.tvPF.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvC.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvC.backgroundTintList = null
                binding.tvC.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))
            }
            2 -> {
                binding.tvAll.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvAll.backgroundTintList = null
                binding.tvAll.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvPG.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvPG.backgroundTintList = null
                binding.tvPG.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvSG.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.bg))
                binding.tvSG.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

                binding.tvSF.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvSF.backgroundTintList = null
                binding.tvSF.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvPF.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvPF.backgroundTintList = null
                binding.tvPF.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvC.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvC.backgroundTintList = null
                binding.tvC.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))
            }
            3 -> {
                binding.tvAll.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvAll.backgroundTintList = null
                binding.tvAll.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvPG.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvPG.backgroundTintList = null
                binding.tvPG.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvSG.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvSG.backgroundTintList = null
                binding.tvSG.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvSF.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.bg))
                binding.tvSF.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

                binding.tvPF.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvPF.backgroundTintList = null
                binding.tvPF.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvC.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvC.backgroundTintList = null
                binding.tvC.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))
            }
            4 -> {
                binding.tvAll.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvAll.backgroundTintList = null
                binding.tvAll.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvPG.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvPG.backgroundTintList = null
                binding.tvPG.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvSG.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvSG.backgroundTintList = null
                binding.tvSG.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvSF.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvSF.backgroundTintList = null
                binding.tvSF.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvPF.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.bg))
                binding.tvPF.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

                binding.tvC.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvC.backgroundTintList = null
                binding.tvC.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))
            }
            5 -> {
                binding.tvAll.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvAll.backgroundTintList = null
                binding.tvAll.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvPG.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvPG.backgroundTintList = null
                binding.tvPG.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvSG.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvSG.backgroundTintList = null
                binding.tvSG.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvSF.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvSF.backgroundTintList = null
                binding.tvSF.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvPF.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvPF.backgroundTintList = null
                binding.tvPF.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvC.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.bg))
                binding.tvC.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            }
            else -> {
                binding.tvAll.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.bg))
                binding.tvAll.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))

                binding.tvPG.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvPG.backgroundTintList = null
                binding.tvPG.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvSG.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvSG.backgroundTintList = null
                binding.tvSG.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvSF.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvSF.backgroundTintList = null
                binding.tvSF.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvPF.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvPF.backgroundTintList = null
                binding.tvPF.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))

                binding.tvC.background = ContextCompat.getDrawable(requireContext(),R.drawable.orange_rounded_border)
                binding.tvC.backgroundTintList = null
                binding.tvC.setTextColor(ContextCompat.getColor(requireContext(),R.color.bg))
            }

        }
    }

    private fun searchButtonClick() {
        binding.actSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (binding.actSearch.text.toString() == "" || binding.actSearch.text.isEmpty()) {
                        binding.NSVMain.visibility = View.VISIBLE
                        binding.rvSearchPlayer.visibility = View.GONE
                        getAllPlayerAPi(currentSelection, filterCode)
                    } else {
                        SearchPlayerList(binding.actSearch.text.toString().trim())
                    }
                }
            }
        })

        binding.ivSearch.setOnClickListener{
            if (binding.actSearch.text.toString() == "" || binding.actSearch.text.isEmpty()) {
                //getServices()
                binding.NSVMain.visibility = View.VISIBLE
                binding.rvSearchPlayer.visibility = View.GONE
                //MessageForValid("Can not be blank.")
            } else {
                SearchPlayerList(binding.actSearch.text.toString().trim())
            }
        }

        binding.actSearch.setOnKeyListener { v, keyCode, event ->
            when {

                //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {

                    if (binding.actSearch.text.toString() == "" || binding.actSearch.text.isEmpty())
                    {
                        binding.NSVMain.visibility = View.VISIBLE
                        //  getServices()
                    }
                    else
                    {
                        SearchPlayerList(binding.actSearch.text.toString().trim())
                    }

                    return@setOnKeyListener true
                }
                else -> false
            }
        }
    }

    private fun SearchPlayerList(search_text: String)
    {
        progressDialoge2()

        try {
            val newFilteredList = playerAllData.filter { itm -> itm.fullname != null && itm.fullname.contains(search_text, true) }
            if(newFilteredList.isEmpty()){
                Toast.makeText(requireContext(), "No record found.", Toast.LENGTH_SHORT).show()
                setRecyclerViewData(playerAllData)
            } else {
                setRecyclerViewData(newFilteredList)
            }
        }catch (er:Exception){
            Log.d("SearchPlayerList: ", "${er.message}")
        }
//        (newFilteredList)

        /*apiInterface.searchTabPlayer("Bearer $HeaderToken", searchTabModel(search_text)).enqueue(object :Callback<searchTabResponse>{
            override fun onResponse(call: Call<searchTabResponse>, response: Response<searchTabResponse>)
            {
                val STResponse = response.body();
                if (STResponse != null)
                {
                    if (STResponse.success)
                    {
                        searchTabPlayerList.clear()
                        searchTabPlayerList.addAll(STResponse.data)
                        binding.NSVMain.visibility = View.GONE
                        binding.rvSearchPlayer.visibility = View.VISIBLE

                        searchPlayerTabAdapter = searchPlayerTabAdapter(requireContext(),searchTabPlayerList)
                        binding.rvSearchPlayer.adapter = searchPlayerTabAdapter
                        searchPlayerTabAdapter.notifyDataSetChanged()
                        progressDialogue2.dismiss()
                    }
                    else
                    {
                        progressDialogue2.dismiss()
                    }
                }
                else
                {
                    progressDialogue2.dismiss()
                }
            }
            override fun onFailure(call: Call<searchTabResponse>, t: Throwable) {
                call.cancel()
            }

        })*/
    }

    private fun FilterCodeDialoge() {
        binding.ivFilter.setOnClickListener {
//            checkStatus()
            dialogHelperTwo = GlobalDialogHelperTwo(requireActivity(), this)
            dialogHelperTwo.create()
            dialogHelperTwo.setDialogData(requireActivity().getString(R.string.today_stats),arrStatus)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun HandlerScrollEvents()
    {

        binding.hvAllPosition.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            binding.hvScrollHeader.smoothScrollTo(oldScrollX,oldScrollY)
            binding.hvScrollHeader.scrollTo(oldScrollX,oldScrollY)
        }

        /*binding.hvCPosition.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            binding.hvCPositionHeader.smoothScrollTo(oldScrollX,oldScrollY)
            binding.hvCPositionHeader.scrollTo(oldScrollX,oldScrollY)
        }

        binding.hvPfPosition.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            binding.hvPfPositionHeader.smoothScrollTo(oldScrollX,oldScrollY)
            binding.hvPfPositionHeader.scrollTo(oldScrollX,oldScrollY)
        }


        binding.hvPgPosition.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            binding.hvPgPositionHeader.smoothScrollTo(oldScrollX,oldScrollY)
            binding.hvPgPositionHeader.scrollTo(oldScrollX,oldScrollY)
        }

        binding.hvSfPosition.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            binding.hvSfPositionHeader.smoothScrollTo(oldScrollX,oldScrollY)
            binding.hvSfPositionHeader.scrollTo(oldScrollX,oldScrollY)
        }

        binding.hvSgPosition.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            binding.hvSgPositionHeader.smoothScrollTo(oldScrollX,oldScrollY)
            binding.hvSgPositionHeader.scrollTo(oldScrollX,oldScrollY)
        }*/
    }

    private fun MorePlayerEvents() {
//        binding.tvMore.setOnClickListener {
//            val more = Intent(requireContext(),SelectPlayerActivity::class.java)
//            more.putExtra("PG","all")
//            more.putExtra("positionListId",0)
//            more.putExtra("allowAdd",false)
//            startActivity(more)
//        }

        /*binding.tvMoreC.setOnClickListener {
            val more = Intent(requireContext(),SelectPlayerActivity::class.java)
            more.putExtra("PG","C")
            more.putExtra("positionListId",1)
            more.putExtra("allowAdd",false)
            startActivity(more)
        }

        binding.tvMorePf.setOnClickListener {
            val more = Intent(requireContext(),SelectPlayerActivity::class.java)
            more.putExtra("PG","PF")
            more.putExtra("positionListId",2)
            more.putExtra("allowAdd",false)
            startActivity(more)
        }

        binding.tvMorePg.setOnClickListener {
            val more = Intent(requireContext(),SelectPlayerActivity::class.java)
            more.putExtra("PG","PG")
            more.putExtra("positionListId",3)
            more.putExtra("allowAdd",false)
            startActivity(more)
        }

        binding.tvMoreSf.setOnClickListener {
            val more = Intent(requireContext(),SelectPlayerActivity::class.java)
            more.putExtra("PG","SF")
            more.putExtra("positionListId",4)
            more.putExtra("allowAdd",false)
            startActivity(more)
        }

        binding.tvMoreSg.setOnClickListener {
            val more = Intent(requireContext(),SelectPlayerActivity::class.java)
            more.putExtra("PG","SG")
            more.putExtra("positionListId",5)
            more.putExtra("allowAdd",false)
            startActivity(more)
        }*/
    }

    private fun Arguments() {
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
    }
    /*private fun getPlayerData(filterCode: Int)
    {
        searchTabPlayerList.clear()
        progressDialoge2()
        apiInterface.playerAll("Bearer $HeaderToken", playerTabModel(filterCode,User_id.toInt())).enqueue(object : Callback<playerTabResponse>{
            override fun onResponse(call: Call<playerTabResponse>, response: Response<playerTabResponse>)
            {

                //progressDialogue.dismiss()
                val tabResponse = response.body()

                if (tabResponse != null)
                {

                    if (tabResponse.success)
                    {
                        playerAllData.clear()
                        cPlayerData.clear()
                        pfPlayerData.clear()
                        pgPlayerData.clear()
                        sfPlayerData.clear()
                        sgPlayerData.clear()

                        playerAllData.addAll(tabResponse.data.position_All)
                        cPlayerData.addAll(tabResponse.data.position_C)
                        pfPlayerData.addAll(tabResponse.data.position_PF)
                        pgPlayerData.addAll(tabResponse.data.position_PG)
                        sfPlayerData.addAll(tabResponse.data.position_SF)
                        sgPlayerData.addAll(tabResponse.data.position_SG)

                        if (tabResponse.data.position_All.isEmpty())
                        {
                            binding.LlTopPlayerAll.visibility = View.GONE
                        }
                        else
                        {
                            allPlayerNameAdapter = AllPlayerNameAdapter(requireContext(), playerAllData,false)
                            binding.rvPlayersName.adapter = allPlayerNameAdapter

                            allPlayerDetailsAdapter = AllPlayerDetailsAdapter(requireContext(),playerAllData,false)
                            binding.rvPlayersDetails.adapter = allPlayerDetailsAdapter
                            binding.LlTopPlayerAll.visibility = View.VISIBLE
                        }

                        *//*if (tabResponse.data.position_C.isEmpty())
                        {
                            binding.LlTopPlayerC.visibility = View.GONE
                        }
                        else
                        {
                            cPlayerNameAdapter = cPlayerNameAdapter(requireContext(),cPlayerData)
                            binding.rvCPlayer.adapter = cPlayerNameAdapter

                            cPlayerDetailsAdapter = cPlayerDetailsAdapter(requireContext(),cPlayerData)
                            binding.rvCPlayerDetails.adapter = cPlayerDetailsAdapter
                            binding.LlTopPlayerC.visibility = View.VISIBLE
                        }

                        if (tabResponse.data.position_PF.isEmpty())
                        {
                            binding.LlTopPlayerPF.visibility = View.GONE
                        }
                        else
                        {
                            //pf

                            pfPlayerNameAdapter = pfPlayerNameAdapter(requireContext(),pfPlayerData)
                            binding.rvPfPlayer.adapter = pfPlayerNameAdapter

                            pfPlayerDetailsAdapter = pfPlayerDetailsAdapter(requireContext(),pfPlayerData)
                            binding.rvPfPlayerDetails.adapter = pfPlayerDetailsAdapter
                            binding.LlTopPlayerPF.visibility = View.VISIBLE

                        }

                        if (tabResponse.data.position_PG.isEmpty())
                        {
                            binding.LlTopPlayerPG.visibility = View.GONE

                        }
                        else
                        {
                            //PG
                            pgPlayerNameAdapter = pgPlayerNameAdapter(requireContext(),pgPlayerData)
                            binding.rvPgPlayer.adapter = pgPlayerNameAdapter
                            pgPlayerDetailsAdapter = pgPlayerDetailsAdapter(requireContext(),pgPlayerData)
                            binding.rvPgPlayerDetails.adapter = pgPlayerDetailsAdapter
                            binding.LlTopPlayerPG.visibility = View.VISIBLE
                        }

                        if (tabResponse.data.position_SF.isEmpty())
                        {
                            binding.LlTopPlayerSF.visibility = View.GONE
                        }
                        else
                        {
                            //SF
                            sfPlayerNameAdapter = sfPlayerNameAdapter(requireContext(),sfPlayerData)
                            binding.rvSfPlayer.adapter = sfPlayerNameAdapter
                            sfPlayerDetailsAdapter = sfPlayerDetailsAdapter(requireContext(),sfPlayerData)
                            binding.rvSfPlayerDetails.adapter = sfPlayerDetailsAdapter
                            binding.LlTopPlayerSF.visibility = View.VISIBLE
                        }

                        if (tabResponse.data.position_SG.isEmpty())
                        {
                            binding.LlTopPlayerSG.visibility = View.GONE

                        }
                        else
                        {
                            //SG

                            sgPlayerNameAdapter = sgPlayerNameAdapter(requireContext(),sgPlayerData)
                            binding.rvSgPlayer.adapter = sgPlayerNameAdapter

                            sgPlayerDetailsAdapter = sgPlayerDetailsAdapter(requireContext(),sgPlayerData)
                            binding.rvSgPlayerDetails.adapter = sgPlayerDetailsAdapter

                            binding.LlTopPlayerSG.visibility = View.VISIBLE
                        }*//*
                        progressDialogue2.dismiss()
                    }
                }
                else
                {
                    progressDialogue2.dismiss()
                }
            }
            override fun onFailure(call: Call<playerTabResponse>, t: Throwable)
            {
                call.cancel()
            }
        })
    }*/

    private fun setRecyclerViewData(arrPlayerListApi: List<Player>) {
        try {
            /*allPlayerNameAdapter.onDetachedFromRecyclerView(binding.rvPlayersName)
            allPlayerDetailsAdapter.onDetachedFromRecyclerView(binding.rvPlayersDetails)*/
            playerAllData.clear()
            playerAllData.addAll(arrPlayerListApi)
            allPlayerNameAdapter = AllPlayerNameAdapter(requireActivity(), playerAllData,false)
            binding.rvPlayersName.adapter = allPlayerNameAdapter

            allPlayerDetailsAdapter = AllPlayerDetailsAdapter(requireActivity(),playerAllData,false)
            binding.rvPlayersDetails.adapter = allPlayerDetailsAdapter

            binding.LlTopPlayerAll.visibility = View.VISIBLE
        }catch (er: Exception){
            Log.d("getAllPlayerAPi1: ", er.message.toString())
            progressDialogue2.dismiss()
        }
    }

    private fun getAllPlayerAPi(postingCodeLeague: String, filtercode: Int) {
        try {
                progressDialoge2()
                apiInterface.getPlayerList("Bearer $HeaderToken", postingCodeLeague, filtercode.toString(),leagueStartDate, "0").enqueue(object :Callback<PlayerListResponse>{
                    override fun onResponse(call: Call<PlayerListResponse>, response: Response<PlayerListResponse>)
                    {
                        val playerResponse = response.body()
                        if (playerResponse != null)
                        {
                            if (playerResponse.success)
                            {
//                            Log.e("List", "onResponse: "+playerResponse.playerList)
                                if (playerResponse.playerList.isNotEmpty()) {
                                    setRecyclerViewData(playerResponse.playerList)
                                }else{
                                    Toast.makeText(requireContext(), "List is Empty", Toast.LENGTH_SHORT).show()
                                    progressDialogue2.dismiss()
                                }
                            }
                            else
                            {
                                progressDialogue2.dismiss()
                                Log.e("TAG", "onResponse: "+response.message() );
                            }
                        }
                    }
                    override fun onFailure(call: Call<PlayerListResponse>, t: Throwable) {
                        progressDialogue2.dismiss()
                        call.cancel()
                    }

                })
        }catch (er: Exception){
            progressDialogue2.dismiss()
            Log.d("getAllPlayerAPi: ", er.message.toString())
        }

    }

    private fun progressDialoge2()
    {
        val progressDialogeBind = layoutInflater.inflate(R.layout.custom_dialogebox,null)
        progressDialogue2 = Dialog(binding.root.context)
        progressDialogue2.setContentView(progressDialogeBind)

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
        progressDialogue2.setCancelable(false)
        progressDialogue2.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialogue2.show()
    }

    private fun allocateMemory()
    {
        binding.rvPlayersName.setHasFixedSize(false)
        binding.rvPlayersDetails.setHasFixedSize(false)

        /*binding.rvCPlayer.setHasFixedSize(false)
        binding.rvCPlayerDetails.setHasFixedSize(false)*/

        binding.rvPlayersName.layoutManager = LinearLayoutManager(context).apply { isAutoMeasureEnabled = false }
        binding.rvPlayersDetails.layoutManager = LinearLayoutManager(context).apply { isAutoMeasureEnabled = false }


        playerAllData = ArrayList()

        cPlayerData = ArrayList()
        pfPlayerData = ArrayList()

        pgPlayerData = ArrayList()
        sfPlayerData = ArrayList()
        sgPlayerData = ArrayList()

        searchTabPlayerList = ArrayList()

        searchTabPlayerList.clear()
        playerAllData.clear()
        cPlayerData.clear()
        pfPlayerData.clear()
    }

    override fun onBtnSheetItemClicked(buttonText: String) {
        val tmpPos = arrStatus.indexOf(buttonText)
        Handler(Looper.getMainLooper()).postDelayed({
                dialogHelperTwo.dismiss()
                getAllPlayerAPi(currentSelection,tmpPos)
        },100)
    }

    /*override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
            sharedPrefManager = SharedPrefManager.getInstance(requireContext())
            Log.d("VRAJAN","No 3 fragment T_PlayerFragment")

            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val currentDate = sdf.format(Date())
            leagueStartDate = currentDate


            arrStatus.add((requireActivity() as BottomNavigationActivity).resources.getString(R.string.str_this_season))
            arrStatus.add((requireActivity() as BottomNavigationActivity).resources.getString(R.string.str_last_7_games))
            arrStatus.add((requireActivity() as BottomNavigationActivity).resources.getString(R.string.str_last_3_games))
            arrStatus.add((requireActivity() as BottomNavigationActivity).resources.getString(R.string.str_last_game))

            allocateMemory()

            Arguments()
            setupPositionFilter()
//        getPlayerData(filterCode)
            FilterCodeDialoge()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                HandlerScrollEvents()
            }
            MorePlayerEvents()
            searchButtonClick()
        }

    }*/
}
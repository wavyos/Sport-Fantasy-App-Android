package com.example.sportsfantasy.Fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sportsfantasy.Activities.BottomNavigationActivity
import com.example.sportsfantasy.Activities.PgUserActivity
import com.example.sportsfantasy.Activities.TeamActivity
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Fragment.newFlow.LeagueNewFragment
import com.example.sportsfantasy.Interface.BottomSheetItemClickListener
import com.example.sportsfantasy.Model.*
import com.example.sportsfantasy.Model.Rooster.RosterResponse
import com.example.sportsfantasy.Model.Rooster.rosterAddModel
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.Utils.GlobalDialogHelperTwo
import com.example.sportsfantasy.databinding.FragmentTRoosterBinding
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.TextStyle
import java.time.temporal.ChronoField
import java.util.*
import kotlin.collections.ArrayList

class T_RoosterFragment : Fragment(), BottomSheetItemClickListener
{
    lateinit var binding:FragmentTRoosterBinding
    lateinit var apiInterface: ApiInterface
    var arrStatus = ArrayList<String>()

    var filterCode = 0
    lateinit var sharedPrefManager:SharedPrefManager
    var HeaderToken = ""
    var User_id = ""
    var LeagueId = ""
    var UserName = ""
    var LeagueName = ""

    lateinit var rplayerAdapter:rosterPlayerAdapter;
    lateinit var rdplayerAdapter:rosterPlayerDataAdapter;

    lateinit var getRosterPlayers:ArrayList<com.example.sportsfantasy.Model.Rooster.Player>


    var salary = "0"
    var salary_percentage = "0"
    var leaguePoint = ""

    lateinit var progressDialogue: Dialog
    lateinit var custCalenderDialogue: Dialog

    var defaultDate = ""


    var today_m3: String = ""
    var today_m3_show: String = ""
    var today_m2: String = ""
    var today_m2_show: String = ""
    var today_m1: String = ""
    var today_m1_show: String = ""
    var today: String = ""
    var today_show: String = ""
    var today_1: String = ""
    var today_1_show: String = ""
    var today_2: String = ""
    var today_2_show: String = ""
    var today_3: String = ""
    var today_3_show: String = ""

    //type = 0 is filter and type= 1 is date
    var type = 0

    private lateinit var dialogHelperTwo: GlobalDialogHelperTwo

    var calendar = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        binding = FragmentTRoosterBinding.inflate(layoutInflater)
        Log.d("VRAJAN","No 1 fragment T_RoosterFragment")
        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(requireContext())

        binding.rvPlayers.setHasFixedSize(true)
        binding.rvPlayers.layoutManager = LinearLayoutManager(requireContext())
        getRosterPlayers = ArrayList()
        getRosterPlayers.clear()

        defaultDate   = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

//        resources.getString(R.string.str_this_season), resources.getString(R.string.str_last_7_games),
//        resources.getString(R.string.str_last_3_games), resources.getString(R.string.str_last_game)

        if(isAdded && activity != null){
            arrStatus.add((requireActivity() as BottomNavigationActivity).resources.getString(R.string.str_this_season))
            arrStatus.add((requireActivity() as BottomNavigationActivity).resources.getString(R.string.str_last_7_games))
            arrStatus.add((requireActivity() as BottomNavigationActivity).resources.getString(R.string.str_last_3_games))
            arrStatus.add((requireActivity() as BottomNavigationActivity).resources.getString(R.string.str_last_game))
        }

        Arguments()
        checkstatus(0)
        getCurrentDate()
        scrollableEventHandle()

        var isLeagueStarted = false

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val strDate = sdf.parse(sharedPrefManager.getLeague.league_start_date)
        // val strDate = sdf.parse("2023-01-02 00:00:00")
        isLeagueStarted = System.currentTimeMillis() > strDate.time
        if(sharedPrefManager.getLeague.remaining_members != "null"){
            if(!isLeagueStarted || sharedPrefManager.getLeague.remaining_members.toInt() > 0){
                binding.tvDraft.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.stroke_color))
            }else{
                binding.tvDraft.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.bg))
                binding.tvDraft.setOnClickListener {
                    try{
                        val sharedPreference = requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                        val editor = sharedPreference.edit()

                        editor.putString("league_name", "" + sharedPrefManager.getLeague.league_name)
                        editor.putString("league_id", "" + sharedPrefManager.getLeague.id)
                        editor.commit()

                        if(requireActivity() is BottomNavigationActivity){
                            val act = requireActivity() as BottomNavigationActivity
                            val bundle = Bundle()
                            bundle.putString("league_name", "" + sharedPrefManager.getLeague.league_name)
                            bundle.putString("league_id", "" + sharedPrefManager.getLeague.id)

                            val fragment = LeagueNewFragment()
                            fragment.arguments = bundle

                            act.addFragment(fragment)
                        }
                    }catch (_: Exception){

                    }
                }
            }
        } else {
            binding.tvDraft.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(requireContext(),R.color.stroke_color))
        }

        binding.root.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_BACKSLASH)
            {
                Log.e("TAG", "onKey: : backpress")
            }

            false
        }
        //getList()
        return binding.root
    }



    private fun getCurrentDate()
    {
        val ordinalNumbers: MutableMap<Long, String> = HashMap(42)
        ordinalNumbers[1L] = "1st"
        ordinalNumbers[2L] = "2nd"
        ordinalNumbers[3L] = "3rd"
        ordinalNumbers[21L] = "21st"
        ordinalNumbers[22L] = "22nd"
        ordinalNumbers[23L] = "23rd"
        ordinalNumbers[31L] = "31st"

        for (d in 1..31)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                ordinalNumbers.putIfAbsent(d.toLong(), "" + d + "th")
            }
        }

        val dayOfMonthFormatter: DateTimeFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            DateTimeFormatterBuilder()
                .appendText(ChronoField.DAY_OF_MONTH, ordinalNumbers)
                .appendPattern(" MMMM")
                .toFormatter()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        binding.tvSelectDate.text = "${context?.getString(R.string.str_today)} (${LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE MM/dd"))})"

        if (binding.tvSelectDate.text.isNotEmpty())
        {
            binding.tvActStatus.text = arrStatus[0]
            binding.tvSelectDate.setOnClickListener{

                type=2
//                binding.actStatus.setSelection(0)
                binding.tvActStatus.text = arrStatus[0]

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    CustomCalender()
                    // CustomDate()
                }
            }
        }

        binding.rlLayoutStats.setOnClickListener {
            dialogHelperTwo = GlobalDialogHelperTwo(requireActivity(), this)
            dialogHelperTwo.create()
            dialogHelperTwo.setDialogData(requireActivity().getString(R.string.today_stats),arrStatus)
        }

        binding.imgDateLeft.setOnClickListener {
            calendar.add(Calendar.DATE, -1)
            updateDateTextView()
        }

        binding.imgDateRight.setOnClickListener {
            calendar.add(Calendar.DATE, 1)
            updateDateTextView()
        }

    }

    private fun updateDateTextView() {
        val dateFormat = SimpleDateFormat("EEE MM/dd", Locale.getDefault())
        binding.tvSelectDate.text = dateFormat.format(calendar.time)
        val dateuse = ""+calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH).toString()
        getList(0,dateuse)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatSelectedDate(date: LocalDate): String {
        val formatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .appendText(ChronoField.DAY_OF_WEEK, TextStyle.SHORT)
            .appendLiteral(' ')
            .appendValue(ChronoField.MONTH_OF_YEAR)
            .appendLiteral('/')
            .appendValue(ChronoField.DAY_OF_MONTH)
            .toFormatter()

        return date.format(formatter)
    }


    private fun CustomCalender()
    {
        val gc1 = GregorianCalendar()
        val year: Int = gc1[Calendar.YEAR]
        val month: Int = gc1[Calendar.MONTH]
        val day: Int = gc1[Calendar.DAY_OF_MONTH]
        val dp = DatePickerDialog(requireContext(),
            { datePicker, Year, Month, Day ->

                //    binding.tvSelectDate.setText(Day.toString() + "/" + (Month + 1) + "/" + Year)

                val dateuse = ""+Year+"-"+(Month+1)+"-"+Day.toString()
                val dateusenew = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    formatSelectedDate(LocalDate.of(Year, Month + 1, Day))
                } else {
                    ""+Year+"-"+(Month+1)+"-"+Day.toString()
                }
                binding.tvSelectDate.text = dateusenew

                calendar = Calendar.getInstance().apply {
                    set(Calendar.YEAR, Year)
                    set(Calendar.MONTH, Month) // Note that months in Calendar are zero-based (0 for January, 1 for February, etc.)
                    set(Calendar.DAY_OF_MONTH, Day)
                }


                //      Log.e("SSSSS", "CustomCalender: "+Year+"-"+(Month+1)+"-"+Day.toString() )
                getList(0,dateuse)
            },
            year,
            month,
            day
        )
        dp.show()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")
    private fun CustomDate()
    {
        val custDialog = layoutInflater.inflate(R.layout.custom_datepicker,null)
        custCalenderDialogue = Dialog(binding.root.context)
        custCalenderDialogue.setContentView(custDialog)

        val tv_current_date_minus3 = custDialog.findViewById<TextView>(R.id.tv_current_date_minus3)
        val tv_current_date_minus2 = custDialog.findViewById<TextView>(R.id.tv_current_date_minus2)
        val tv_current_date_minus1 = custDialog.findViewById<TextView>(R.id.tv_current_date_minus1)
        val tv_current_date = custDialog.findViewById<TextView>(R.id.tv_current_date)
        val tv_current_date_1 = custDialog.findViewById<TextView>(R.id.tv_current_date_1)
        val tv_current_date_2 = custDialog.findViewById<TextView>(R.id.tv_current_date_2)
        val tv_current_date_3 = custDialog.findViewById<TextView>(R.id.tv_current_date_3)

        val ordinalNumbers: MutableMap<Long, String> = HashMap(42)
        ordinalNumbers[1L] = "1st"
        ordinalNumbers[2L] = "2nd"
        ordinalNumbers[3L] = "3rd"
        ordinalNumbers[21L] = "21st"
        ordinalNumbers[22L] = "22nd"
        ordinalNumbers[23L] = "23rd"
        ordinalNumbers[31L] = "31st"

        for (d in 1..31)
        {
            ordinalNumbers.putIfAbsent(d.toLong(), "" + d + "th")
        }

        val dayOfMonthFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
            .appendText(ChronoField.DAY_OF_MONTH, ordinalNumbers)
            .appendPattern(" MMMM")
            .toFormatter()


        today_m3   = LocalDate.now().minusDays(3).format(DateTimeFormatter.ofPattern("EEE MM/dd"))
        today_m3_show   = LocalDate.now().minusDays(3).format(dayOfMonthFormatter)

        today_m2   = LocalDate.now().minusDays(2).format(DateTimeFormatter.ofPattern("EEE MM/dd"))
        today_m2_show   = LocalDate.now().minusDays(2).format(dayOfMonthFormatter)

        today_m1   = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("EEE MM/dd"))
        today_m1_show   = LocalDate.now().minusDays(1).format(dayOfMonthFormatter)

        today   = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE MM/dd"))
        today_show    = LocalDate.now().format(dayOfMonthFormatter)

        today_1    = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("EEE MM/dd"))
        today_1_show   = LocalDate.now().plusDays(1).format(dayOfMonthFormatter)

        today_2   = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("EEE MM/dd"))
        today_2_show   = LocalDate.now().plusDays(2).format(dayOfMonthFormatter)

        today_3   = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("EEE MM/dd"))
        today_3_show   = LocalDate.now().plusDays(3).format(dayOfMonthFormatter)


        tv_current_date_minus3.text = today_m3.toString()
        tv_current_date_minus2.text = today_m2.toString()
        tv_current_date_minus1.text = today_m1.toString()
        tv_current_date.text = "${context?.getString(R.string.str_today)} (${today})"
        // tv_current_date.text =today.toString()
        tv_current_date_1.text = today_1.toString()
        tv_current_date_2.text = today_2.toString()
        tv_current_date_3.text = today_3.toString()


        tv_current_date_minus3.setOnClickListener{
            binding.tvSelectDate.text = today_m3_show
            tv_current_date_minus3.setTextColor(resources.getColor(R.color.bg))
            tv_current_date_minus2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus1.setTextColor(resources.getColor(R.color.black))
            tv_current_date.setTextColor(resources.getColor(R.color.black))
            tv_current_date_1.setTextColor(resources.getColor(R.color.black))
            tv_current_date_2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_3.setTextColor(resources.getColor(R.color.black))

            getList(0,today_3_show)

            Handler(Looper.getMainLooper()).postDelayed(Runnable
            {
                custCalenderDialogue.dismiss()
            }, 100)


        }
        tv_current_date_minus2.setOnClickListener{
            binding.tvSelectDate.text = today_m2_show
            tv_current_date_minus3.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus2.setTextColor(resources.getColor(R.color.bg))
            tv_current_date_minus1.setTextColor(resources.getColor(R.color.black))
            tv_current_date.setTextColor(resources.getColor(R.color.black))
            tv_current_date_1.setTextColor(resources.getColor(R.color.black))
            tv_current_date_2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_3.setTextColor(resources.getColor(R.color.black))
            getList(0,today_2_show)
            Handler(Looper.getMainLooper()).postDelayed(Runnable
            {
                custCalenderDialogue.dismiss()
            }, 100)

        }
        tv_current_date_minus1.setOnClickListener{

            binding.tvSelectDate.text = today_m1_show
            tv_current_date_minus3.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus1.setTextColor(resources.getColor(R.color.bg))
            tv_current_date.setTextColor(resources.getColor(R.color.black))
            tv_current_date_1.setTextColor(resources.getColor(R.color.black))
            tv_current_date_2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_3.setTextColor(resources.getColor(R.color.black))

            getList(0,today_m1_show)

            Handler(Looper.getMainLooper()).postDelayed(Runnable
            {
                custCalenderDialogue.dismiss()
            }, 100)



        }
        tv_current_date.setOnClickListener{
            binding.tvSelectDate.text = today_show
            tv_current_date_minus3.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus1.setTextColor(resources.getColor(R.color.black))
            tv_current_date.setTextColor(resources.getColor(R.color.bg))
            tv_current_date_1.setTextColor(resources.getColor(R.color.black))
            tv_current_date_2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_3.setTextColor(resources.getColor(R.color.black))

            getList(0,today_show)

            Handler(Looper.getMainLooper()).postDelayed(Runnable
            {
                custCalenderDialogue.dismiss()
            }, 100)


        }
        tv_current_date_1.setOnClickListener{
            binding.tvSelectDate.text = today_1_show
            tv_current_date_minus3.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus1.setTextColor(resources.getColor(R.color.black))
            tv_current_date.setTextColor(resources.getColor(R.color.black))
            tv_current_date_1.setTextColor(resources.getColor(R.color.bg))
            tv_current_date_2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_3.setTextColor(resources.getColor(R.color.black))

            getList(0,today_1_show)

            Handler(Looper.getMainLooper()).postDelayed(Runnable
            {
                custCalenderDialogue.dismiss()
            }, 100)

        }
        tv_current_date_2.setOnClickListener{
            binding.tvSelectDate.text = today_2_show
            tv_current_date_minus3.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus1.setTextColor(resources.getColor(R.color.black))
            tv_current_date.setTextColor(resources.getColor(R.color.black))
            tv_current_date_1.setTextColor(resources.getColor(R.color.black))
            tv_current_date_2.setTextColor(resources.getColor(R.color.bg))
            tv_current_date_3.setTextColor(resources.getColor(R.color.black))

            getList(0,today_2_show)

            Handler(Looper.getMainLooper()).postDelayed(Runnable
            {
                custCalenderDialogue.dismiss()
            }, 100)

        }
        tv_current_date_3.setOnClickListener{
            binding.tvSelectDate.text = today_3_show
            tv_current_date_minus3.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus1.setTextColor(resources.getColor(R.color.black))
            tv_current_date.setTextColor(resources.getColor(R.color.black))
            tv_current_date_1.setTextColor(resources.getColor(R.color.black))
            tv_current_date_2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_3.setTextColor(resources.getColor(R.color.bg))


            getList(0,today_3_show,)



            Handler(Looper.getMainLooper()).postDelayed(Runnable
            {
                custCalenderDialogue.dismiss()
            }, 100)


        }

        Log.e("TAG", "CustomDate: "+defaultDate )


        custCalenderDialogue.setCancelable(false)
        //custCalenderDialogue.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        custCalenderDialogue.show()
    }



    private fun scrollableEventHandle()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            binding.rvNestedHorizontal.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                binding.hvScrollHeader.smoothScrollTo(oldScrollX, oldScrollY)
                binding.hvScrollHeader.scrollTo(oldScrollX, oldScrollY)

            }
        }
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

    private fun getList(filterCode: Int, date: String) {
        //    apiInterface.getPlayerList("Bearer $HeaderToken", postingCodeLeague,  ""+ filterCode)

        progressDialoge()
        Log.e("roster", "getList: filtercode "+filterCode )
        Log.e("roster", "getList: LeagueId "+LeagueId )
        Log.e("roster", "getList: User_id "+User_id )

        apiInterface.rosterData("Bearer $HeaderToken", rosterAddModel(filterCode, LeagueId , User_id,date)).enqueue(object :Callback<RosterResponse>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<RosterResponse>, response: Response<RosterResponse>)
            {
                try {
                    val rosterResponse = response.body()

                    if (rosterResponse != null)
                    {
                        if (rosterResponse.success)
                        {
                            if (rosterResponse.data[0].salary!=null)
                            {
                                salary = rosterResponse.data[0].salary
                                binding.tvSalary.text = "$$salary"
                            }
                            if (rosterResponse.data[0].salary_percentage!= null)
                            {
                                salary_percentage = rosterResponse.data[0].salary_percentage
                                binding.tvSalaryPercentage.text = "$salary_percentage%"
                            }
                            if (rosterResponse.data[0].league_point != null)
                            {
                                leaguePoint = rosterResponse.data[0].league_point
                                binding.tvLeaguePoint.text = ""+leaguePoint.toString()
                            }

                            Log.e("TAG", "onResponse: "+rosterResponse.data)
                            getRosterPlayers.addAll(rosterResponse.data[0].players)
                            rplayerAdapter = rosterPlayerAdapter(requireContext(),getRosterPlayers)
                            rdplayerAdapter = rosterPlayerDataAdapter(requireContext(),getRosterPlayers)
                            binding.rvPlayers.adapter = rplayerAdapter
                            binding.rvPlayerData.adapter = rdplayerAdapter
                            rplayerAdapter.notifyDataSetChanged()
                            rdplayerAdapter.notifyDataSetChanged()

                            binding.tvTeamName.text = ""+UserName.toString()
                            binding.tvUsername.text = ""+UserName.toString()
                            binding.tvLeagueName.text = ""+ LeagueName

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
                }catch (er: Exception){
                    progressDialogue.dismiss()
                }
            }
            override fun onFailure(call: Call<RosterResponse>, t: Throwable) {
                progressDialogue.dismiss()
                call.cancel()
            }

        })


    }

    private fun checkstatus(position: Int)
    {
        if (position==0)
        {
            filterCode = 0
            if (type==0)
            {
                getList(filterCode, defaultDate)
            }
            else if (type==2)
            {
                //
            }
        }
        else if (position==1)
        {
            filterCode = 1
            getList(filterCode, defaultDate)
        }
        else if (position==2)
        {
            filterCode = 2
            getList(filterCode, defaultDate)
        }
        else if (position==3)
        {
            filterCode = 3
            getList(filterCode, defaultDate)
        }
        getCurrentDate()
    }

    public class rosterPlayerAdapter(private val context: Context, private val getRosterPlayers: ArrayList<com.example.sportsfantasy.Model.Rooster.Player>):RecyclerView.Adapter<rosterPlayerAdapter.MyViewHolder>(){

        class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)
        {
            val tv_player_name: TextView = itemView.findViewById(R.id.tv_player_name)
            val civ_player: CircleImageView = itemView.findViewById(R.id.civ_player)

            val view_line = itemView.findViewById<View>(R.id.view_line)

            val tv_league = itemView.findViewById<TextView>(R.id.tv_league)
            val tv_positiontype = itemView.findViewById<TextView>(R.id.tv_positiontype)
            val ll_row_roster_player = itemView.findViewById<LinearLayout>(R.id.ll_row_roster_player)

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_roster_player,parent,false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int)
        {
            val currentItem = getRosterPlayers[position]
            holder.ll_row_roster_player.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            //code for gray background
            if(position % 2 == 0){
                holder.ll_row_roster_player.setBackgroundColor(ContextCompat.getColor(context, R.color.row_even_color))
            } else {
                holder.ll_row_roster_player.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }

            //Log.e("playerlistcolumn", "onBindViewHolder: "+currentItem.fullname )

            val fullname: String = currentItem.fullname
            val positiontype: String = currentItem.positiontype
            val primaryposition: String = currentItem.primaryposition
            val pid:String = currentItem.pid.toString()


            holder.tv_player_name.text = fullname
            holder.tv_positiontype.text = positiontype

            holder.tv_league.text = primaryposition

            holder.tv_player_name.setOnClickListener {
                val playerScreen = Intent(context,PgUserActivity::class.java)
                playerScreen.putExtra("pid",pid)
                context.startActivity(playerScreen)
            }
            Glide.with(context)
                .load(currentItem.headshot)
                .placeholder(ContextCompat.getDrawable(context, R.drawable.player_placeholder_img))
                .into(holder.civ_player)

            holder.civ_player.setOnClickListener{
                val playerScreen = Intent(context,PgUserActivity::class.java)
                playerScreen.putExtra("pid",pid)
                context.startActivity(playerScreen)
            }


            val list = getRosterPlayers.lastIndex
//        Log.d("krunal", "Playerlist column" + list)

            if (position == list) {
                //  holder.view_line.visibility = View.GONE
            }

        }

        override fun getItemCount(): Int {
            return getRosterPlayers.size
        }

    }


    public class rosterPlayerDataAdapter(private val context: Context, private val getRosterPlayers: ArrayList<com.example.sportsfantasy.Model.Rooster.Player>):RecyclerView.Adapter<rosterPlayerDataAdapter.MyViewHolder>(){

        class MyViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView)
        {
            val tv_points = itemView.findViewById<TextView>(R.id.tv_points)
            val tv_salary = itemView.findViewById<TextView>(R.id.tv_salary)
            val tv_FGM = itemView.findViewById<TextView>(R.id.tv_FGM)
            val tv_PTS = itemView.findViewById<TextView>(R.id.tv_PTS)
            val tv_REB = itemView.findViewById<TextView>(R.id.tv_REB)
            val tv_FT = itemView.findViewById<TextView>(R.id.tv_FT)
            val tv_AST = itemView.findViewById<TextView>(R.id.tv_AST)
            val tv_BLK = itemView.findViewById<TextView>(R.id.tv_BLK)
            val tv_STL = itemView.findViewById<TextView>(R.id.tv_STL)
            val tv_TOV = itemView.findViewById<TextView>(R.id.tv_TOV)

            val view_line = itemView.findViewById<View>(R.id.view_line)
            val llMain = itemView.findViewById<HorizontalScrollView>(R.id.scroll)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_player_pg,parent,false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int)
        {
            val currentItem  = getRosterPlayers[position]

            if(position % 2 == 0){
                holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.row_even_color))
            } else {
                holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
            }

            var FG: String = currentItem.FG
            var Ast: String = currentItem.Ast
            var BLK: String = currentItem.BLK
            var FGM: String = currentItem.FGM
            var FT: String = currentItem.FT
            var Reb: String = currentItem.Reb
            var STL: String = currentItem.STL
            var TOV: String = currentItem.TOV
            var fullname: String = currentItem.fullname
            var pid: String = currentItem.pid.toString()
            var positiontype: String = currentItem.positiontype
            var primaryposition: String = currentItem.primaryposition
            val customTypeface: Typeface = ResourcesCompat.getFont(context, R.font.roboto_bold)!!
            holder.tv_points.typeface = customTypeface
//        var float2 : Float = str2.toFloat();

            if((FG == "-") || (Ast == "-") || (BLK == "-") || (FGM == "-") || (FT == "-") || (Reb=="-") || (STL=="-") || (TOV == "-"))
            {
                FG = "0"
                Ast = "0"
                BLK = "0"
                FGM = "0"
                FT = "0"
                Reb = "0"
                STL = "0"
                TOV = "0"
            }
            else
            {
                FGM = FGM.toBigDecimal().setScale(0, RoundingMode.UP).toDouble().toString()
                FG = FG.toBigDecimal().setScale(0, RoundingMode.UP).toDouble().toString()
                Reb = Reb.toBigDecimal().setScale(0, RoundingMode.UP).toDouble().toString()
                FT = FT.toBigDecimal().setScale(0, RoundingMode.UP).toDouble().toString()
                BLK = BLK.toBigDecimal().setScale(0, RoundingMode.UP).toDouble().toString()
                Ast = Ast.toBigDecimal().setScale(0, RoundingMode.UP).toDouble().toString()
                STL = STL.toBigDecimal().setScale(0, RoundingMode.UP).toDouble().toString()
                TOV = TOV.toBigDecimal().setScale(0, RoundingMode.UP).toDouble().toString()



            }


            holder.tv_FGM.text = FGM
            holder.tv_PTS.text = FG
            holder.tv_REB.text = Reb
            holder.tv_FT.text = FT
            holder.tv_BLK.text = BLK
            holder.tv_AST.text = Ast
            holder.tv_STL.text = STL
            holder.tv_TOV.text = TOV


            var salary = "0"
            if (currentItem.salary.equals("-") || currentItem.salary.equals(""))
            {
                holder.tv_salary.text = "$"+salary
            }
            else
            {
                salary = currentItem.salary.toString()
                holder.tv_salary.text = "$"+salary
            }

            try {
                holder.tv_points.text = "%.2f".format(currentItem.points.toBigDecimal().toDouble())
            }catch (er: java.lang.Exception){
                holder.tv_points.text = "0.0"
            }

            val list = getRosterPlayers.lastIndex

            if (position==list)
            {
                //holder.view_line.visibility = View.GONE
            }


        }

        override fun getItemCount(): Int {
            return getRosterPlayers.size
        }

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

    override fun onBtnSheetItemClicked(buttonText: String) {
        val tmpPos = arrStatus.indexOf(buttonText)
        checkstatus(tmpPos)
        dialogHelperTwo.dismiss()
    }

}
package com.example.sportsfantasy.Fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import androidx.viewpager.widget.ViewPager
import com.androidadvance.topsnackbar.TSnackbar
import com.example.sportsfantasy.Activities.BottomNavigationActivity
import com.example.sportsfantasy.Adapter.MatchUpA.teamPlayerAdapter
import com.example.sportsfantasy.Adapter.MatchUpWormAdapter
import com.example.sportsfantasy.Adapter.OnboardingAdapter
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Interface.dateChangeListner
import com.example.sportsfantasy.Model.MatchUp.Data
import com.example.sportsfantasy.Model.MatchUp.MatchUpModel
import com.example.sportsfantasy.Model.MatchUp.MatchUpResponse
import com.example.sportsfantasy.Model.MatchUp.TeamPlayer
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.Utils.WormDotsIndicator
import com.example.sportsfantasy.databinding.FragmentTMatchupBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.TextStyle
import java.time.temporal.ChronoField
import java.util.*


class T_MatchupFragment : Fragment()
{
    lateinit var binding:FragmentTMatchupBinding
    lateinit var apiInterface:ApiInterface
    lateinit var sharedPrefManager:SharedPrefManager

    lateinit var progressDialogue: Dialog
    lateinit var custCalenderDialogue: Dialog

    var HeaderToken=""
    var User_id = ""
    var LeagueId = ""
    var UserName = ""
    var LeagueName = ""


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

    var dataArrayList= ArrayList<Data>()

    lateinit var dcListner: dateChangeListner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
        binding = FragmentTMatchupBinding.inflate(layoutInflater)
        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(requireContext())
        Log.d("VRAJAN","No 2 fragment T_MatchupFragment")
        Arguments()
        getCurrentDate()

        dcListner = object :dateChangeListner
        {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDateChangeApiCall(date: String)
            {
                Log.e("Vrajan", "onDateChangeApiCall: "+date)
                SharedPrefManager.getInstance(requireContext()).SaveDate(date)
                getMatchByDate(date)
            }
        }
        AllocateMemory()
        return binding.root
    }

    private fun getMatchByDate(date: String)
    {
        dataArrayList.clear()
        progressDialoge()
        apiInterface.matchUpDate("Bearer $HeaderToken", MatchUpModel(date,LeagueId.toInt(),User_id.toInt())).enqueue(
            object : Callback<MatchUpResponse>{
                override fun onResponse(
                    call: Call<MatchUpResponse>,
                    response: Response<MatchUpResponse>
                ) {
                    progressDialogue.dismiss()
                    val mResponse = response.body()
                    Log.e("vr", "onResponse: "+mResponse )
                    if (mResponse != null)
                    {
                        if (mResponse.success)
                        {
                            Log.e("XXX", "onResponse: "+response.body() )
                            dataArrayList.addAll(mResponse.data)
                            val matchUpAdapter = MatchUpAdapter(requireContext(),dataArrayList,dcListner)
                            binding.rvMatchup.adapter = matchUpAdapter
                            matchUpAdapter.notifyDataSetChanged()

                            val pagerSnapHelper = PagerSnapHelper()
                            binding.rvMatchup.onFlingListener = null
                            pagerSnapHelper.attachToRecyclerView(binding.rvMatchup)
                            binding.wormDotsIndicator.attachToRecyclerView(binding.rvMatchup, pagerSnapHelper)

                            binding.rvMatchup.parent.requestDisallowInterceptTouchEvent(true)
                        }
                        else
                        {
                            MessageForValid("")
                        }
                    }
                    else{

                    }
                }
                override fun onFailure(call: Call<MatchUpResponse>, t: Throwable) {
                    progressDialogue.dismiss()
                    call.cancel()
                }
            })

    }

    private fun AllocateMemory()
    {
        binding.rvMatchup.setHasFixedSize(true)
        val HorizontalLayout = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvMatchup.layoutManager = HorizontalLayout
        binding.rvMatchup.setHasFixedSize(true)
        PagerSnapHelper().attachToRecyclerView(binding.rvMatchup)
        dataArrayList = ArrayList<Data>()
    }

    private fun Arguments() {
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
        LeagueName = sharedPrefManager.getLeague.league_name

    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMatchupdata() {
        progressDialoge()

        val date_today   = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//        val date_today   = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

        apiInterface.matchUpDate("Bearer $HeaderToken", MatchUpModel(date_today,LeagueId.toInt(),User_id.toInt())).enqueue(
            object : Callback<MatchUpResponse>{
                override fun onResponse(
                    call: Call<MatchUpResponse>,
                    response: Response<MatchUpResponse>
                ) {
                    val mResponse = response.body();
                    Log.e("kkkkkk", "onResponse: "+mResponse )
                    progressDialogue.dismiss()
                    if (mResponse != null)
                    {
                        dataArrayList.clear()
                        if (mResponse.success)
                        {
                            dataArrayList.addAll(mResponse.data)
                            val matchUpAdapter = MatchUpAdapter(requireContext(),dataArrayList,dcListner)
                            binding.rvMatchup.adapter = matchUpAdapter
                            val pagerSnapHelper = PagerSnapHelper()
                            binding.rvMatchup.onFlingListener = null
                            pagerSnapHelper.attachToRecyclerView(binding.rvMatchup)
                            binding.wormDotsIndicator.attachToRecyclerView(binding.rvMatchup, pagerSnapHelper)

                            val tmpData = dataArrayList.filter { item -> item.fisrt_team_id == User_id.toInt() || item.second_team_id == User_id.toInt() }
                            if(tmpData.isNotEmpty()){
                                binding.rvMatchup.scrollToPosition(dataArrayList.indexOf(tmpData[0]))
                            }
                        }
                        else
                        {

                            MessageForValid("")
                        }
                    }
                    else{

                    }
                }
                override fun onFailure(call: Call<MatchUpResponse>, t: Throwable) {
                    progressDialogue.dismiss()
                    call.cancel()
                }

            })

    }



    private fun getCurrentDate() {
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

        val dayOfMonthFormatter: DateTimeFormatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatterBuilder()
                .appendText(ChronoField.DAY_OF_MONTH, ordinalNumbers)
                .appendPattern(" MMMM")
                .toFormatter()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        binding.tvSelectDate.text = LocalDate.now().format(dayOfMonthFormatter)
        binding.tvSelectDate.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        if (binding.tvSelectDate.text.isNotEmpty())
        {
            binding.tvSelectDate.setOnClickListener{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                {
                    CustomDate()
                }

            }
        }
    }


    private fun MessageForValid(s: String)
    {
        val snackbar = TSnackbar.make(binding.root.findViewById(android.R.id.content), s, TSnackbar.LENGTH_SHORT)
        // snackbar.setActionTextColor(Color.WHITE)
        val snackbarView = snackbar.view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            //    snackbarView.setBackgroundColor(getColor(R.color.bg))
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

        today_m3   = LocalDate.now().minusDays(3).format(DateTimeFormatter.ofPattern("EEE, MMM dd"))
        today_m3_show   = LocalDate.now().minusDays(3).format(dayOfMonthFormatter)

        today_m2   = LocalDate.now().minusDays(2).format(DateTimeFormatter.ofPattern("EEE, MMM dd"))
        today_m2_show   = LocalDate.now().minusDays(2).format(dayOfMonthFormatter)

        today_m1   = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("EEE, MMM dd"))
        today_m1_show   = LocalDate.now().minusDays(1).format(dayOfMonthFormatter)

        today   = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, MMM dd"))
        today_show    = LocalDate.now().format(dayOfMonthFormatter)

        today_1    = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("EEE, MMM dd"))
        today_1_show   = LocalDate.now().plusDays(1).format(dayOfMonthFormatter)

        today_2   = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("EEE, MMM dd"))
        today_2_show   = LocalDate.now().plusDays(2).format(dayOfMonthFormatter)

        today_3   = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("EEE, MMM dd"))
        today_3_show   = LocalDate.now().plusDays(3).format(dayOfMonthFormatter)


        tv_current_date_minus3.text = today_m3.toString()
        tv_current_date_minus2.text = today_m2.toString()
        tv_current_date_minus1.text = today_m1.toString()
        tv_current_date.text = "Today"
        // tv_current_date.text =today.toString()
        tv_current_date_1.text = today_1.toString()
        tv_current_date_2.text = today_2.toString()
        tv_current_date_3.text = today_3.toString()


        tv_current_date_minus3.setOnClickListener{
            binding.tvSelectDate.text = today_m3_show
            binding.tvSelectDate.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            tv_current_date_minus3.setTextColor(resources.getColor(R.color.bg))
            tv_current_date_minus2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus1.setTextColor(resources.getColor(R.color.black))
            tv_current_date.setTextColor(resources.getColor(R.color.black))
            tv_current_date_1.setTextColor(resources.getColor(R.color.black))
            tv_current_date_2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_3.setTextColor(resources.getColor(R.color.black))

            Handler(Looper.getMainLooper()).postDelayed(Runnable
            {
                custCalenderDialogue.dismiss()
            }, 100)


        }
        tv_current_date_minus2.setOnClickListener{
            binding.tvSelectDate.text = today_m2_show
            binding.tvSelectDate.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            tv_current_date_minus3.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus2.setTextColor(resources.getColor(R.color.bg))
            tv_current_date_minus1.setTextColor(resources.getColor(R.color.black))
            tv_current_date.setTextColor(resources.getColor(R.color.black))
            tv_current_date_1.setTextColor(resources.getColor(R.color.black))
            tv_current_date_2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_3.setTextColor(resources.getColor(R.color.black))
            Handler(Looper.getMainLooper()).postDelayed(Runnable
            {
                custCalenderDialogue.dismiss()
            }, 100)

        }
        tv_current_date_minus1.setOnClickListener{
            binding.tvSelectDate.text = today_m1_show
            binding.tvSelectDate.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            tv_current_date_minus3.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus1.setTextColor(resources.getColor(R.color.bg))
            tv_current_date.setTextColor(resources.getColor(R.color.black))
            tv_current_date_1.setTextColor(resources.getColor(R.color.black))
            tv_current_date_2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_3.setTextColor(resources.getColor(R.color.black))
            Handler(Looper.getMainLooper()).postDelayed(Runnable
            {
                custCalenderDialogue.dismiss()
            }, 100)

        }
        tv_current_date.setOnClickListener{
            binding.tvSelectDate.text = today_show
            binding.tvSelectDate.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            tv_current_date_minus3.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus1.setTextColor(resources.getColor(R.color.black))
            tv_current_date.setTextColor(resources.getColor(R.color.bg))
            tv_current_date_1.setTextColor(resources.getColor(R.color.black))
            tv_current_date_2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_3.setTextColor(resources.getColor(R.color.black))

            Handler(Looper.getMainLooper()).postDelayed(Runnable
            {
                custCalenderDialogue.dismiss()
            }, 100)


        }
        tv_current_date_1.setOnClickListener{
            binding.tvSelectDate.text = today_1_show
            binding.tvSelectDate.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            tv_current_date_minus3.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus1.setTextColor(resources.getColor(R.color.black))
            tv_current_date.setTextColor(resources.getColor(R.color.black))
            tv_current_date_1.setTextColor(resources.getColor(R.color.bg))
            tv_current_date_2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_3.setTextColor(resources.getColor(R.color.black))

            Handler(Looper.getMainLooper()).postDelayed(Runnable
            {
                custCalenderDialogue.dismiss()
            }, 100)

        }
        tv_current_date_2.setOnClickListener{
            binding.tvSelectDate.text = today_2_show
            binding.tvSelectDate.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            tv_current_date_minus3.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus1.setTextColor(resources.getColor(R.color.black))
            tv_current_date.setTextColor(resources.getColor(R.color.black))
            tv_current_date_1.setTextColor(resources.getColor(R.color.black))
            tv_current_date_2.setTextColor(resources.getColor(R.color.bg))
            tv_current_date_3.setTextColor(resources.getColor(R.color.black))

            Handler(Looper.getMainLooper()).postDelayed(Runnable
            {
                custCalenderDialogue.dismiss()
            }, 100)

        }
        tv_current_date_3.setOnClickListener{
            binding.tvSelectDate.text = today_3_show
            binding.tvSelectDate.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            tv_current_date_minus3.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_minus1.setTextColor(resources.getColor(R.color.black))
            tv_current_date.setTextColor(resources.getColor(R.color.black))
            tv_current_date_1.setTextColor(resources.getColor(R.color.black))
            tv_current_date_2.setTextColor(resources.getColor(R.color.black))
            tv_current_date_3.setTextColor(resources.getColor(R.color.bg))


            Handler(Looper.getMainLooper()).postDelayed(Runnable
            {
                custCalenderDialogue.dismiss()
            }, 100)


        }

        Log.e("TAG", "CustomDate: "+defaultDate )


        custCalenderDialogue.setCancelable(false)
        custCalenderDialogue.show()
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

    class MatchUpAdapter(private val context: Context, val dataMatchUp:ArrayList<Data>,
                         val dateChangeListner: dateChangeListner): RecyclerView.Adapter<MatchUpAdapter.MyViewHolder>()
    {
        val arryList = ArrayList<TeamPlayer>()
        lateinit var custCalenderDialogue: Dialog
        var today_m3: String = ""
        var today_m3_show: String = ""
        var m3: String = ""

        var today_m2: String = ""
        var today_m2_show: String = ""
        var m2: String = ""

        var today_m1: String = ""
        var today_m1_show: String = ""
        var m1: String = ""

        var today: String = ""
        var today_show: String = ""
        var t0: String = ""

        var today_1: String = ""
        var today_1_show: String = ""
        var t1: String = ""

        var today_2: String = ""
        var today_2_show: String = ""
        var t2: String = ""

        var today_3: String = ""
        var today_3_show: String = ""
        var t3: String = ""


        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            val rv_players_list:RecyclerView = itemView.findViewById(R.id.rv_players_list)

            val tv_select_date:TextView = itemView.findViewById(R.id.tv_select_date)

            //firstplayer

            val tv_one_salary :TextView = itemView.findViewById(R.id.tv_one_salary)
            val tv_team_name_one :TextView = itemView.findViewById(R.id.tv_team_name_one)
            val tv_team_point_one :TextView = itemView.findViewById(R.id.tv_team_point_one)



            //second_player
            //op opposite
            val tv_one_salary_op :TextView = itemView.findViewById(R.id.tv_one_salary_op)
            val tv_team_name_op :TextView = itemView.findViewById(R.id.tv_team_name_op)
            val tv_team_point_op :TextView = itemView.findViewById(R.id.tv_team_point_op)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_matchup_details,parent,false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int)
        {
            val currentItem  = dataMatchUp[position]

            if (currentItem.fisrt_team_name == "" || currentItem.fisrt_team_name.isNullOrEmpty())
            {
                holder.tv_team_name_one.text = ""
            }
            else
            {
                holder.tv_team_name_one.text = currentItem.fisrt_team_name
            }

            if (currentItem.second_team_name == "" || currentItem.second_team_name.isNullOrEmpty())
            {

            }
            else
            {
                holder.tv_team_name_op.text = currentItem.second_team_name
            }
            if (currentItem.fisrt_team_salary == "" || currentItem.fisrt_team_salary.isNullOrEmpty())
            {

            }
            else
            {
                holder.tv_one_salary.text = "$" +currentItem.fisrt_team_salary
            }
            if (currentItem.second_team_salary == "" || currentItem.second_team_salary.isNullOrEmpty())
            {

            }
            else
            {
                holder.tv_one_salary_op.text = "$"+currentItem.second_team_salary.toString()
            }
            if (currentItem.fisrt_team_points == "0" || currentItem.fisrt_team_points.equals(null))
            {

            }
            else
            {
                holder.tv_team_point_one.text = currentItem.fisrt_team_points.toString()
            }
            if (currentItem.second_team_points == "0" || currentItem.second_team_points.equals(null))
            {

            }
            else
            {
                holder.tv_team_point_op.text = currentItem.second_team_points.toString()
            }

            val ordinalNumbers: MutableMap<Long, String> = java.util.HashMap(42)
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

            val currentdate = SharedPrefManager.getInstance(context).getSDate

            if (currentdate.equals(""))
            {
                holder.tv_select_date.text  = "${context.getString(R.string.str_today)} (${LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE MM/dd"))})"
                holder.tv_select_date.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            }
            else
            {
                holder.tv_select_date.text  = currentdate
                holder.tv_select_date.paintFlags = Paint.UNDERLINE_TEXT_FLAG
            }




            //date
            if (holder.tv_select_date.text.isNotEmpty())
            {
                holder.tv_select_date.setOnClickListener{

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    {
                        val custDialog = LayoutInflater.from(context).inflate(R.layout.custom_datepicker_new,null)
                        custCalenderDialogue = BottomSheetDialog(context)
                        custCalenderDialogue.setContentView(custDialog)

                        val tv_current_date_minus3 = custDialog.findViewById<TextView>(R.id.tv_current_date_minus3)
                        val tv_current_date_minus2 = custDialog.findViewById<TextView>(R.id.tv_current_date_minus2)
                        val tv_current_date_minus1 = custDialog.findViewById<TextView>(R.id.tv_current_date_minus1)
                        val tv_current_date = custDialog.findViewById<TextView>(R.id.tv_current_date)
                        val tv_current_date_1 = custDialog.findViewById<TextView>(R.id.tv_current_date_1)
                        val tv_current_date_2 = custDialog.findViewById<TextView>(R.id.tv_current_date_2)
                        val tv_current_date_3 = custDialog.findViewById<TextView>(R.id.tv_current_date_3)
                        val tv_cancel = custDialog.findViewById<TextView>(R.id.tv_custom_date_cancel)

                        tv_cancel.setOnClickListener {
                            custCalenderDialogue.dismiss()
                        }

                        val ordinalNumbers: MutableMap<Long, String> = java.util.HashMap(42)
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

//                        val dayOfMonthFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
//                            .appendText(ChronoField.DAY_OF_WEEK, TextStyle.SHORT)
//                            .appendPattern(" M/d")
//                            .toFormatter()

                        m3 = LocalDate.now().minusDays(3).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        today_m3   = LocalDate.now().minusDays(3).format(DateTimeFormatter.ofPattern("EEE MM/dd"))
                        today_m3_show   = LocalDate.now().minusDays(3).format(dayOfMonthFormatter)

                        m2 = LocalDate.now().minusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        today_m2   = LocalDate.now().minusDays(2).format(DateTimeFormatter.ofPattern("EEE MM/dd"))
                        today_m2_show   = LocalDate.now().minusDays(2).format(dayOfMonthFormatter)

                        m1 = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        today_m1   = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("EEE MM/dd"))
                        today_m1_show   = LocalDate.now().minusDays(1).format(dayOfMonthFormatter)


                        t0 = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        today   = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE MM/dd"))
                        today_show    = LocalDate.now().format(dayOfMonthFormatter)

                        t1 = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        today_1    = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("EEE MM/dd"))
                        today_1_show   = LocalDate.now().plusDays(1).format(dayOfMonthFormatter)

                        t2 = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        today_2   = LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("EEE MM/dd"))
                        today_2_show   = LocalDate.now().plusDays(2).format(dayOfMonthFormatter)

                        t3 = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        today_3   = LocalDate.now().plusDays(3).format(DateTimeFormatter.ofPattern("EEE MM/dd"))
                        today_3_show   = LocalDate.now().plusDays(3).format(dayOfMonthFormatter)


                        tv_current_date_minus3.text = today_m3.toString()
                        tv_current_date_minus2.text = today_m2.toString()
                        tv_current_date_minus1.text = today_m1.toString()
                        tv_current_date.text = "${context.getString(R.string.str_today)} (${today})"
                        // tv_current_date.text =today.toString()
                        tv_current_date_1.text = today_1.toString()
                        tv_current_date_2.text = today_2.toString()
                        tv_current_date_3.text = today_3.toString()


                        tv_current_date_minus3.setOnClickListener{
                            holder.tv_select_date.text = today_m3_show
                            tv_current_date_minus3.setTextColor(context.resources.getColor(R.color.bg))
                            tv_current_date_minus2.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_minus1.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_1.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_2.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_3.setTextColor(context.resources.getColor(R.color.black))

                            dateChangeListner.onDateChangeApiCall(m3)
                            // getList(0,today_3_show)

                            Handler(Looper.getMainLooper()).postDelayed(Runnable
                            {
                                custCalenderDialogue.dismiss()
                            }, 100)
                        }
                        tv_current_date_minus2.setOnClickListener{
                            holder.tv_select_date.text = today_m2_show
                            tv_current_date_minus3.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_minus2.setTextColor(context.resources.getColor(R.color.bg))
                            tv_current_date_minus1.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_1.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_2.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_3.setTextColor(context.resources.getColor(R.color.black))
                            //getList(0,today_2_show)
                            dateChangeListner.onDateChangeApiCall(m2)
                            Handler(Looper.getMainLooper()).postDelayed(Runnable
                            {
                                custCalenderDialogue.dismiss()
                            }, 100)

                        }
                        tv_current_date_minus1.setOnClickListener{

                            holder.tv_select_date.text = today_m1_show
                            tv_current_date_minus3.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_minus2.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_minus1.setTextColor(context.resources.getColor(R.color.bg))
                            tv_current_date.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_1.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_2.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_3.setTextColor(context.resources.getColor(R.color.black))

                            dateChangeListner.onDateChangeApiCall(m1)
//                            getList(0,today_m1_show)

                            Handler(Looper.getMainLooper()).postDelayed(Runnable
                            {
                                custCalenderDialogue.dismiss()
                            }, 100)



                        }
                        tv_current_date.setOnClickListener{
                            holder.tv_select_date.text = today_show
                            tv_current_date_minus3.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_minus2.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_minus1.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date.setTextColor(context.resources.getColor(R.color.bg))
                            tv_current_date_1.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_2.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_3.setTextColor(context.resources.getColor(R.color.black))
                            dateChangeListner.onDateChangeApiCall(t0)

//                            getList(0,today_show)

                            Handler(Looper.getMainLooper()).postDelayed(Runnable
                            {
                                custCalenderDialogue.dismiss()
                            }, 100)


                        }
                        tv_current_date_1.setOnClickListener{
                            holder.tv_select_date.text = today_1_show
                            tv_current_date_minus3.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_minus2.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_minus1.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_1.setTextColor(context.resources.getColor(R.color.bg))
                            tv_current_date_2.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_3.setTextColor(context.resources.getColor(R.color.black))
                            dateChangeListner.onDateChangeApiCall(t1)
//                            getList(0,today_1_show)

                            Handler(Looper.getMainLooper()).postDelayed(Runnable
                            {
                                custCalenderDialogue.dismiss()
                            }, 100)

                        }
                        tv_current_date_2.setOnClickListener{
                            holder.tv_select_date.text = today_2_show
                            tv_current_date_minus3.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_minus2.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_minus1.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_1.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_2.setTextColor(context.resources.getColor(R.color.bg))
                            tv_current_date_3.setTextColor(context.resources.getColor(R.color.black))
                            dateChangeListner.onDateChangeApiCall(t2)
//                            getList(0,today_2_show)

                            Handler(Looper.getMainLooper()).postDelayed(Runnable
                            {
                                custCalenderDialogue.dismiss()
                            }, 100)

                        }
                        tv_current_date_3.setOnClickListener{
                            holder.tv_select_date.text = today_3_show
                            tv_current_date_minus3.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_minus2.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_minus1.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_1.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_2.setTextColor(context.resources.getColor(R.color.black))
                            tv_current_date_3.setTextColor(context.resources.getColor(R.color.bg))
                            dateChangeListner.onDateChangeApiCall(t3)

///                            getList(0, today_3_show)



                            Handler(Looper.getMainLooper()).postDelayed(Runnable
                            {
                                custCalenderDialogue.dismiss()
                            }, 100)


                        }

                        Log.e("date", "adapter CustomDate: "+"defaultDate" )


                        custCalenderDialogue.setCancelable(false)
                        //custCalenderDialogue.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        custCalenderDialogue.show()
                    }
                }
            }

            //date
            arryList.clear()
            arryList.addAll(currentItem.team_players)
            holder.rv_players_list.setHasFixedSize(false)
            holder.rv_players_list.layoutManager = LinearLayoutManager(context)
            val adapter = teamPlayerAdapter(context,arryList)
            holder.rv_players_list.adapter = adapter
            adapter.notifyDataSetChanged()
        }


        override fun getItemCount() = dataMatchUp.size


    }


    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getMatchupdata()
        }

    }


}
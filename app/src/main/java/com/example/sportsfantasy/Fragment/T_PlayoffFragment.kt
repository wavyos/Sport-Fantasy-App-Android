package com.example.sportsfantasy.Fragment

import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.sportsfantasy.Adapter.PlayOffAdapter
import com.example.sportsfantasy.Adapter.ViewPagerPlayOffAdapter
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Model.PlayOff.Data
import com.example.sportsfantasy.Model.PlayOff.Group
import com.example.sportsfantasy.Model.PlayOff.PlayOffModel
import com.example.sportsfantasy.Model.PlayOff.PlayOffResponse
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.FragmentTPlayoffBinding
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class T_PlayoffFragment : Fragment() {
    lateinit var binding:FragmentTPlayoffBinding
    lateinit var apiInterface: ApiInterface
    lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var myButton : Button
    private val tempArray = arrayOf<Int>(1, 2, 3, 4, 5)
    private lateinit var playOffList: ArrayList<PlayOffResponse>

    lateinit var progressDialogue: Dialog

    private lateinit var playoffAdapter : PlayOffAdapter
    private lateinit var mViewPagerAdapter: ViewPagerPlayOffAdapter

    var HeaderToken=""
    var User_id = ""
    var LeagueId = ""
    var UserName = ""
    var LeagueName = ""


    var defaultDate = ""

    lateinit var iv_menu: ImageView
    lateinit var iv_back: ImageView
    lateinit var tv_name_app: TextView


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentTPlayoffBinding.inflate(layoutInflater)
        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(requireContext())

        playOffList = ArrayList()
        Arguments()
        getPlayOffData()
        CustomHeader()
        /*val itemList = ArrayList<Group>()
        itemList.add(Group("Rohit",0,0,"Dhoni", 0, 0))
        itemList.add(Group("Rohit",0,0,"Dhoni", 0, 0))
        itemList.add(Group("Rohit",0,0,"Dhoni", 0, 0))
        itemList.add(Group("Rohit",0,0,"Dhoni", 0, 0))

        val itemListsec = ArrayList<Group>()
        itemListsec.add(Group("Rohit",0,0,"Dhoni", 0, 0))
        itemListsec.add(Group("Rohit",0,0,"Dhoni", 0, 0))

        val itemListthi = ArrayList<Group>()
        itemListthi.add(Group("Rohit",0,0,"Dhoni", 0, 0))

        val itemData = Data(itemList, "Round 1")
        val itemDatasec = Data(itemListsec, "Round 2")
        val itemDatathi = Data(itemListthi, "Round 3")
        val itemDataList = ArrayList<Data>()
        itemDataList.add(itemData)
        itemDataList.add(itemDatasec)
        itemDataList.add(itemDatathi)
        val item = PlayOffResponse(itemDataList, true)

        setUpViewPager(item)*/

        /*binding.rvMainPlayoff.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = PlayOffMainAdapter(requireContext(),item)
        }*/
        
//        createViewHere(item)
        return binding.root
    }

    private fun CustomHeader() {
        iv_menu = binding.root.findViewById(R.id.iv_menu)
        iv_back = binding.root.findViewById(R.id.iv_back)
        tv_name_app = binding.root.findViewById(R.id.tv_name_app)
        iv_menu.visibility = View.GONE

        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }

        val args = arguments
        if (args != null) {
            if(args.getBoolean("isHeader", false)){
                binding.llHeaderPlayoff.visibility = View.VISIBLE
            } else {
                binding.llHeaderPlayoff.visibility = View.GONE
            }
        }

    }

    var viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
        object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(position: Int,positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {
                binding.tvRoundTitle.text = "Round ${position + 1}"
            }
        }


    private fun setUpViewPager(item: PlayOffResponse) {
        try {
            val displayMetrics = DisplayMetrics()
            requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
            val height = displayMetrics.heightPixels
            val width = displayMetrics.widthPixels

            mViewPagerAdapter = ViewPagerPlayOffAdapter(requireContext(), item.playOffData, height)

            binding.viewpagerPlayoff.pageMargin = 15
            binding.viewpagerPlayoff.setPadding(50, 0, 50, 0);
            binding.viewpagerPlayoff.setClipToPadding(false)
            binding.viewpagerPlayoff.adapter = mViewPagerAdapter
            binding.viewpagerPlayoff.addOnPageChangeListener(viewPagerPageChangeListener)
        }catch (_: Exception){
        }
    }

    private fun createViewHere(response: PlayOffResponse) {

        val lp = LinearLayout.LayoutParams(
            650,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.gravity = Gravity.CENTER
        val roundTextParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        roundTextParams.gravity = Gravity.CENTER_HORIZONTAL
        roundTextParams.setMargins(50,-20,30,20)

        val hormainLinLayout = LinearLayout(requireContext())
        hormainLinLayout.orientation = LinearLayout.HORIZONTAL
        hormainLinLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        hormainLinLayout.gravity = Gravity.CENTER

        for(tempItem in response.playOffData){
            val i = 0;
            val mainLinLayout = LinearLayout(requireContext())
            mainLinLayout.orientation = LinearLayout.VERTICAL
            mainLinLayout.layoutParams = lp

            val tv = TextView(requireContext())
            tv.text = tempItem.rountTitle
            tv.textSize = 14F
            tv.textAlignment = View.TEXT_ALIGNMENT_CENTER
            tv.setPadding(10,30,10,20)
            tv.background = resources.getDrawable(R.drawable.custom_card)
            tv.setTypeface(tv.typeface, Typeface.BOLD)
            mainLinLayout.addView(tv, roundTextParams)

            val rv = RecyclerView(requireContext())
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.gravity = Gravity.CENTER
            rv.isNestedScrollingEnabled = false
            rv.setPadding(0,20,0,0)
            rv.layoutParams = params

            val llm = LinearLayoutManager(requireContext())
            playoffAdapter = PlayOffAdapter(requireContext(), tempItem.groupList)
            rv.adapter = playoffAdapter
            rv.layoutManager = llm
            mainLinLayout.addView(rv)
            hormainLinLayout.addView(mainLinLayout)
        }

        /*binding.frameLayoutPlayOff.addView(hormainLinLayout)*/

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

    private fun getPlayOffData()
    {
        playOffList.clear()
        progressDialoge()
        apiInterface.playOffDate("Bearer $HeaderToken", PlayOffModel(LeagueId.toInt(),User_id.toInt())).enqueue(
            object : Callback<PlayOffResponse> {
                override fun onResponse(
                    call: Call<PlayOffResponse>,
                    response: Response<PlayOffResponse>
                ) {
                    progressDialogue.dismiss()
                    val mResponse = response.body()
                    Log.e("vr", "onResponse: $mResponse")
                    if (mResponse != null)
                    {
                        if (mResponse.success && mResponse.playOffData != null) {
                            Log.e("PlayOff: ", "onResponse: "+response.body() )
                            if(mResponse.playOffData.size > 0){
                                setUpViewPager(mResponse)
                            }
                        } else {

                        }
                    }
                    else{

                    }
                }
                override fun onFailure(call: Call<PlayOffResponse>, t: Throwable) {
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

    private fun createCard(cardLayout: LinearLayout, d_item: List<Group>) {
        val cardParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        cardParams.weight = 1.0f
        val llFirst = LinearLayout(requireContext())
        llFirst.layoutParams = cardParams
        llFirst.orientation = LinearLayout.HORIZONTAL
        llFirst.setPadding(20,10,20,10)
        llFirst.gravity = Gravity.CENTER

        val llSecond = LinearLayout(requireContext())
        llSecond.orientation = LinearLayout.HORIZONTAL
        llSecond.gravity = Gravity.CENTER

        val insideLLParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        insideLLParams.weight = 1.0F

        val linLayout = LinearLayout(requireContext())
        linLayout.orientation = LinearLayout.VERTICAL
        linLayout.layoutParams = insideLLParams
        val lpView = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lpView.weight = 1.0F
        lpView.gravity = Gravity.LEFT

        val tv = TextView(requireContext())
        tv.text = "${d_item[0].fisrtTeamName}"
        tv.textSize = 13F
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.setTextColor(Color.parseColor("#000000"))
        tv.layoutParams = lpView
        linLayout.addView(tv)

        val tvValue1 = TextView(requireContext())
        tvValue1.text = "${d_item[0].fisrtTeamSalary}"
        tvValue1.textSize = 11F
        tvValue1.setTextColor(Color.parseColor("#000000"))
        tvValue1.layoutParams = lpView
        linLayout.addView(tvValue1)

        val linLayouttwo = LinearLayout(requireContext())
        linLayouttwo.orientation = LinearLayout.VERTICAL
        linLayouttwo.layoutParams = insideLLParams

        val lpViewtwo = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lpViewtwo.gravity = Gravity.RIGHT

        val tvtwo = TextView(requireContext())
        tvtwo.textSize = 11F
        tvtwo.text = "${d_item[0].fisrtTeamPoints}"
        tvtwo.setTextColor(Color.parseColor("#000000"))
        tvtwo.layoutParams = lpViewtwo
        linLayouttwo.addView(tvtwo)

        llFirst.addView(linLayout)
        llFirst.addView(linLayouttwo)

        cardLayout.addView(llFirst)
    }

    private fun createSecondCard(cardLayout: LinearLayout, d_item: List<Group>) {
        val cardParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        cardParams.weight = 1.0f
        val llSecond = LinearLayout(requireContext())
        llSecond.layoutParams = cardParams
        llSecond.orientation = LinearLayout.HORIZONTAL
        llSecond.setPadding(20,10,20,10)
        llSecond.gravity = Gravity.CENTER

        val insideLLParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        insideLLParams.weight = 1.0F

        val linLayout = LinearLayout(requireContext())
        linLayout.orientation = LinearLayout.VERTICAL
        linLayout.layoutParams = insideLLParams
        val lpView = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lpView.weight = 1.0F
        lpView.gravity = Gravity.LEFT

        val tv = TextView(requireContext())
        tv.text = "${d_item[0].secondTeamName}"
        tv.textSize = 13F
        tv.setTypeface(tv.typeface, Typeface.BOLD)
        tv.setTextColor(Color.parseColor("#000000"))
        tv.layoutParams = lpView
        linLayout.addView(tv)

        val tvValue1 = TextView(requireContext())
        tvValue1.textSize = 11F
        tvValue1.text = "${d_item[0].secondTeamSalary}"
        tvValue1.setTextColor(Color.parseColor("#000000"))
        tvValue1.layoutParams = lpView
        linLayout.addView(tvValue1)

        val linLayouttwo = LinearLayout(requireContext())
        linLayouttwo.orientation = LinearLayout.VERTICAL
        linLayouttwo.layoutParams = insideLLParams

        val lpViewtwo = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lpViewtwo.gravity = Gravity.RIGHT

        val tvtwo = TextView(requireContext())
        tvtwo.textSize = 11F
        tvtwo.text = "${d_item[0].secondTeamPoints}"
        tvtwo.setTextColor(Color.parseColor("#000000"))
        tvtwo.layoutParams = lpViewtwo
        linLayouttwo.addView(tvtwo)

        llSecond.addView(linLayout)
        llSecond.addView(linLayouttwo)

        cardLayout.addView(llSecond)
    }
}
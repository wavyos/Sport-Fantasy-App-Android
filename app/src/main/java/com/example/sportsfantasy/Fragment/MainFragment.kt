package com.example.sportsfantasy.Fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.androidadvance.topsnackbar.TSnackbar
import com.example.sportsfantasy.Activities.BottomNavigationActivity
import com.example.sportsfantasy.Activities.ChoiceActivity
import com.example.sportsfantasy.Activities.CreateLeagueActivity
import com.example.sportsfantasy.Activities.LoginActivity
import com.example.sportsfantasy.Activities.RegisterActivity
import com.example.sportsfantasy.Adapter.LeagueListAdapter
import com.example.sportsfantasy.Adapter.NewsAdapter
import com.example.sportsfantasy.Adapter.PlayerTab.AllPlayerDetailsAdapter
import com.example.sportsfantasy.Adapter.PlayerTab.AllPlayerNameAdapter
import com.example.sportsfantasy.Adapter.RankAdapter
import com.example.sportsfantasy.Adapter.ViewPagerAdapter
import com.example.sportsfantasy.Adapter.ViewPagerAdapterMainActivity
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Fragment.newFlow.CommunityNewsFragment
import com.example.sportsfantasy.Fragment.newFlow.LeagueNewFragment
import com.example.sportsfantasy.Fragment.newFlow.TeamFragment
import com.example.sportsfantasy.Interface.BottomSheetItemClickListener
import com.example.sportsfantasy.Interface.Listner
import com.example.sportsfantasy.Interface.NewsItemClick
import com.example.sportsfantasy.Model.DeviceToken
import com.example.sportsfantasy.Model.ErrorResponse
import com.example.sportsfantasy.Model.JoinLeagueModel
import com.example.sportsfantasy.Model.JoinLeagueResponse
import com.example.sportsfantasy.Model.NewTenPos.NewTenPosResponse
import com.example.sportsfantasy.Model.NewTenPos.PlayerNewTen
import com.example.sportsfantasy.Model.PlayerF.playerTabModel
import com.example.sportsfantasy.Model.PlayerF.playerTabResponse
import com.example.sportsfantasy.Model.Rank
import com.example.sportsfantasy.Model.SearchTab.Data
import com.example.sportsfantasy.Model.UserLeagueResponse
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.Utils.CheckNetworkConnection
import com.example.sportsfantasy.Utils.Constant
import com.example.sportsfantasy.Utils.CustomSnackbar
import com.example.sportsfantasy.Utils.GlobalDialogHelper
import com.example.sportsfantasy.Utils.GlobalDialogHelperThree
import com.example.sportsfantasy.Utils.LoadingDialoge
import com.example.sportsfantasy.databinding.FragmentMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList
import java.util.Locale

class MainFragment : Fragment(), Listner, BottomSheetItemClickListener, NewsItemClick {
    private lateinit var binding: FragmentMainBinding

    var kr = ""
    lateinit var toggle: ActionBarDrawerToggle

    lateinit var iv_menu: ImageView
    lateinit var iv_back: ImageView
    lateinit var tv_name_app: TextView
    lateinit var tv_nav_username_: TextView

    lateinit var Token: String

    lateinit var sharedPreferences: SharedPreferences
    lateinit var myEdit: SharedPreferences.Editor

    lateinit var apiInterface: ApiInterface

    var userid: String = ""

    // lateinit var leaguelist: ArrayList<UserLeague>
    lateinit var leaguelist: List<UserLeagueResponse.UserLeague>
    lateinit var ladapter: LeagueListAdapter

    lateinit var name: String

    lateinit var loading: LoadingDialoge

    lateinit var progressDialogue: Dialog

    lateinit var snack_cust: CustomSnackbar

    //lateinit var mProgressDialog:ProgressDialog
    lateinit var clipboard: ClipboardManager
    lateinit var clip: ClipData

    private lateinit var checkNetworkConnection: CheckNetworkConnection
    lateinit var sharedPrefManager: SharedPrefManager

    var fcmToken = ""
    var headerToken = ""
    var searchTabPlayerList = ArrayList<Data>()
    var playerAllData = ArrayList<com.example.sportsfantasy.Model.Player>()

    lateinit var allPlayerNameAdapter: AllPlayerNameAdapter
    lateinit var allPlayerDetailsAdapter: AllPlayerDetailsAdapter

    //exoplayer
    private var player: ExoPlayer? = null

    private var dialogHelper: GlobalDialogHelperThree? = null
    lateinit var gdb : GlobalDialogHelper

    private val images = listOf(
        R.drawable.home_img_one,
        R.drawable.home_img_two,
        R.drawable.home_img_three
    )


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(layoutInflater)

        dialogHelper = GlobalDialogHelperThree(requireActivity(), this)
        gdb = GlobalDialogHelper(requireActivity(),this)
        gdb.create()

        allocatememory()
        arguments()
        CustomHeader()

        setUpVideoPlayer()

        sharedPreferences = requireActivity().getSharedPreferences("MySharedPref", AppCompatActivity.MODE_PRIVATE)
        myEdit = sharedPreferences.edit()
        Token = sharedPreferences.getString("access_token", null).toString()

        val back = requireActivity().intent.getStringExtra("league")

        setEvents()

        setupViewPager()

        setupTabLayout()

        val handler = Handler()
        handler.postDelayed({
            val h1: Int = binding.rvLeagueList.width
            val h2: Int = binding.drawer.width
            binding.rvLeagueList.smoothScrollToPosition(h2 - h1)
        }, 1000)

        updatedevicetoken()
        binding.tvMore.setOnClickListener {
//            Toast.makeText(this, "View more clicked", Toast.LENGTH_SHORT).show()
//            val intent = Intent(requireActivity(), TopPlayerViewMoreActivity::class.java)
//            startActivity(intent)
            try {
                if(requireActivity() is BottomNavigationActivity){
                    val act = requireActivity() as BottomNavigationActivity
                    val bundle = Bundle()
                    bundle.putString("ChooseTab","TopPlayer")

                    val fragment = TeamFragment()
                    fragment.arguments = bundle

                    act.addFragment(fragment)
                }
            }catch (er: Exception){
                Log.d("LoadFrag", er.message.toString())
            }
        }

        try {

//                val newhandler = Handler()

//                newhandler.postDelayed({
//
//                    if(isAdded){
//                        val dialog = BottomSheetDialog(requireContext())
//
//                        val view = layoutInflater.inflate(R.layout.layout_btmsheet_winlose_popup, null)
//                        val llWinView = view.findViewById<LinearLayout>(R.id.ll_win_view)
//                        val llLoseView = view.findViewById<LinearLayout>(R.id.ll_lose_view)
//                        val btnAcceptWin = view.findViewById<TextView>(R.id.btn_accept_win_dialog)
//                        val btnAcceptLose = view.findViewById<TextView>(R.id.btn_accept_lose_dialog)
//
//                        llWinView.visibility = View.VISIBLE
//                        llLoseView.visibility = View.GONE
//
//                        btnAcceptWin.setOnClickListener {
////                dialog.dismiss()
//                            llWinView.visibility = View.GONE
//                            llLoseView.visibility = View.VISIBLE
//                        }
//                        btnAcceptLose.setOnClickListener {
//                            dialog.dismiss()
//                        }
//
//                        dialog.setCancelable(true)
//                        dialog.setCanceledOnTouchOutside(true)
//                        dialog.setContentView(view)
//
//                        dialog.show()
//                    }
//
//                }, 5000)
        }catch (er:Exception){

        }

        return binding.root
    }


    private fun LogOut() {
        gdb.setDialogData(getString(R.string.app_name), getString(R.string.is_sure_logout),true)
    }

    override fun onDraftClickListner(position: Int, createdLeague: UserLeagueResponse.UserLeague) {
        try{
            val sharedPreference = requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()

            editor.putString("league_name", "" + createdLeague.leagueName)
            editor.putString("league_id", "" + createdLeague.id)
            editor.commit()

            if(requireActivity() is BottomNavigationActivity){
                val act = requireActivity() as BottomNavigationActivity
                val bundle = Bundle()
                bundle.putString("league_name", "" + createdLeague.leagueName)
                bundle.putString("league_id", "" + createdLeague.id)

                val fragment = LeagueNewFragment()
                fragment.arguments = bundle

                act.addFragment(fragment)
            }
        }catch (_: Exception){

        }

    }

    override fun onTeamClickListner(position: Int, createdLeague: UserLeagueResponse.UserLeague) {

        try {
            sharedPrefManager.saveLeague(createdLeague)
            if(requireActivity() is BottomNavigationActivity){
                val act = requireActivity() as BottomNavigationActivity
                val bundle = Bundle()
                bundle.putString("ChooseTab","MyTeam")

                val fragment = TeamFragment()
                fragment.arguments = bundle

                act.addFragment(fragment)
            }
        }catch (er: Exception){
            Log.d("LoadFrag", er.message.toString())
        }
    }

    override fun onMatchUpClickListner(
        position: Int,
        createdLeague: UserLeagueResponse.UserLeague
    ) {
        Toast.makeText(
            requireActivity(),
            getString(R.string.str_work_in_progress),
            Toast.LENGTH_SHORT
        ).show()
    }


    override fun onInviteItemClickListner(
        position: Int,
        createdLeague: UserLeagueResponse.UserLeague
    ) {
        val dialog = BottomSheetDialog(requireActivity())

        val view = layoutInflater.inflate(R.layout.layout_btnsheet_league_joined, null)
        val tvCode = view.findViewById<TextView>(R.id.tv_league_join_code)
        val btnCopyCode = view.findViewById<LinearLayout>(R.id.btn_copy_code)
        val btnShareLink = view.findViewById<LinearLayout>(R.id.btn_share_league)
        val btnShareWeChat = view.findViewById<LinearLayout>(R.id.btn_share_wechat)

        btnShareWeChat.visibility = View.VISIBLE

        tvCode.text = requireContext().getString(R.string.your_code)+ "${createdLeague.invitationCode}"

        btnCopyCode.setOnClickListener {
            MessageForValid("You've copied the invite link.")
            val data: String = createdLeague.invitationLink.toString()
            clip = ClipData.newPlainText("label", data)
            clipboard.setPrimaryClip(clip)

            try {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
                intent.putExtra(Intent.EXTRA_TEXT, "$data")
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share Via"))
                //startActivity(intent);
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        btnShareLink.setOnClickListener {
            MessageForValid("You've copied the invite code.")
            val data: String = createdLeague.invitationCode.toString()
            clip = ClipData.newPlainText("label", data)
            clipboard.setPrimaryClip(clip)

            try {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
                intent.putExtra(Intent.EXTRA_TEXT, "$data")
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share Via"))
                //startActivity(intent);
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        btnShareWeChat.setOnClickListener {

            val launchIntent =
                requireActivity().packageManager.getLaunchIntentForPackage("com.michatapp.im")
            if (launchIntent != null) {
                startActivity(launchIntent)
            } else {
                MessageForValid("There is no application available in device")
                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=com.michatapp.im")
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.michatapp.im")
                        )
                    )
                }
            }

        }

        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(view)

        dialog.show()

    }

    private fun updatedevicetoken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("TestData", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                // Log and toast
                Log.d("TestData", token)

                apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
                apiInterface.updatedevicetoken(DeviceToken(userid, fcmToken))
                    .enqueue(object : Callback<ResponseBody> {
                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                        }

                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {
                                val deviceTokenResponse = response.body()?.string()
                                Log.d("TestData", "deviceTokenResponse -> $deviceTokenResponse")
                            } else if (response.code() == Constant.REQUEST_STATUS_CODE_UNPROCESSABLE) {

                            } else {

                            }
                        }
                    })
            })
    }

    private fun getPlayerData() {

        searchTabPlayerList.clear()
        showDialoge2()
        apiInterface.getNewPlayerList("Bearer $headerToken", "all", "0","0", "2")
            .enqueue(object :
                Callback<NewTenPosResponse> {
                override fun onResponse(
                    call: Call<NewTenPosResponse>,
                    response: Response<NewTenPosResponse>
                ) {
                    try{
                        val tabResponse = response.body()

                        if (tabResponse != null) {
                            if (tabResponse.success) {
                                playerAllData.clear()

                                playerAllData.addAll(tabResponse.playerList)

                                if (tabResponse.playerList.isEmpty()) {
                                    binding.LlTopPlayerAll.visibility = View.GONE
                                } else {
                                    binding.LlTopPlayerAll.visibility = View.VISIBLE

                                    allPlayerNameAdapter =
                                        AllPlayerNameAdapter(requireActivity(), playerAllData, true)
                                    binding.rvPlayersName.adapter = allPlayerNameAdapter

                                    allPlayerDetailsAdapter =
                                        AllPlayerDetailsAdapter(requireActivity(), playerAllData, true)
                                    binding.rvPlayersDetails.adapter = allPlayerDetailsAdapter
                                }
                            }
                        }
                        progressDialogue.dismiss()
                    }catch (er:Exception){ }
                }

                override fun onFailure(call: Call<NewTenPosResponse>, t: Throwable) {
                    progressDialogue.dismiss()
                    call.cancel()
                }
            })
    }

    private fun setUpVideoPlayer() {
        try {
            val tempUri =
                Uri.parse("android.resource://" + requireActivity().packageName + "/" + R.raw.home)
            player = ExoPlayer.Builder(requireActivity())
                .build()
                .also { exoPlayer ->
                    binding.homeVideoView.player = exoPlayer
                }
                .also {
                    val mediaItem = MediaItem.fromUri(tempUri)
                    it.setMediaItem(mediaItem)
                    it.repeatMode = Player.REPEAT_MODE_ALL
                    it.prepare()
                }
            //To handle live play/pause events,use below code
            player!!.addListener(
                object : androidx.media3.common.Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
//                        if(isPlaying){
//                            binding.imgPlayPause.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.ico_pause))
//                        } else {
//                            binding.imgPlayPause.setImageDrawable(ContextCompat.getDrawable(this@MainActivity, R.drawable.play_circle))
//                        }
                    }
                }
            )


        } catch (er: Exception) {
            Toast.makeText(requireActivity(), "${er.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun arguments() {
        try {
            if (SharedPrefManager.getInstance(requireActivity()).ULoggedIn) {
                Log.d("VR", " if " + SharedPrefManager.getInstance(requireActivity()).ULoggedIn)
                headerToken = SharedPrefManager.getInstance(requireActivity()).getLogin.access_token
                //navigation header
//            tv_nav_username_.text =
//                "" + SharedPrefManager.getInstance(requireActivity()).getLogin.userDetails.name
                userid =
                    SharedPrefManager.getInstance(requireActivity()).getLogin.userDetails.id.toString()
                // login and sign up button and card view  //
                binding.llCreateJoin.visibility = View.VISIBLE
//            binding.llCreateJoin2.visibility = View.GONE
                binding.llCardLayout.visibility = View.VISIBLE
                // login and sign up button and card view  //
                name = SharedPrefManager.getInstance(requireActivity()).getLogin.userDetails.name

                binding.tvWelcome.visibility = View.VISIBLE
                name = SharedPrefManager.getInstance(requireActivity()).getRegister.userDetails.name

                binding.tvWelcome.text = "Welcome " + name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }

                //login
                binding.checkLogin.visibility = View.GONE
//            binding.navigationView.visibility = View.VISIBLE
                binding.tvWelcome.visibility = View.GONE
                //backbutton
                Log.d("VR", " if " + SharedPrefManager.getInstance(requireActivity()).ULoggedIn)
                getApiCalling()
                getPlayerData()


            } else if (SharedPrefManager.getInstance(requireActivity()).RLoggedIn) {
                Log.d("VR", " if " + SharedPrefManager.getInstance(requireActivity()).RLoggedIn)
                headerToken = SharedPrefManager.getInstance(requireActivity()).getRegister.access_token
//            tv_nav_username_.text =
//                "" + SharedPrefManager.getInstance(requireActivity()).getRegister.userDetails.name
                binding.checkLogin.visibility = View.GONE
//            binding.navigationView.visibility = View.VISIBLE
                Log.d("VR", " if " + SharedPrefManager.getInstance(requireActivity()).RLoggedIn)


                binding.tvWelcome.visibility = View.VISIBLE
                name = SharedPrefManager.getInstance(requireActivity()).getRegister.userDetails.name

                binding.tvWelcome.text = "Welcome " + name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }

                binding.llCreateJoin.visibility = View.VISIBLE
                binding.llCardLayout.visibility = View.GONE
                userid =
                    SharedPrefManager.getInstance(requireActivity()).getRegister.userDetails.id.toString()
                getApiCalling()
                getPlayerData()

            } else {
                binding.llCreateJoin.visibility = View.VISIBLE
                binding.llCardLayout.visibility = View.GONE

                binding.pbBar.visibility = View.GONE

                binding.tvWelcome.visibility = View.VISIBLE
//            tv_nav_username_.text = ""

                binding.checkLogin.visibility = View.VISIBLE
//            binding.navigationView.visibility = View.GONE
                iv_back.visibility = View.GONE
                !iv_menu.isClickable

//            binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

                iv_menu.visibility = View.GONE
//            binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

                binding.tvTopPlayer.isEnabled = false
                binding.tvTopPlayer.isClickable = false

                userid = ""
                binding.tvTopPlayer.isFocusable = false
                name = ""

            }
        }catch (er: Exception){
            Log.d("not login", ""+er.message)
        }
    }


    private fun allocatememory() {
        snack_cust = CustomSnackbar(requireActivity())

        clipboard =
            requireActivity().getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager

        leaguelist = ArrayList<UserLeagueResponse.UserLeague>()

        loading = LoadingDialoge(requireActivity())

        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(requireActivity())

        binding.rvPlayersName.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvPlayersDetails.layoutManager = LinearLayoutManager(requireActivity())

        //latest news design data
        binding.rvNews.layoutManager = LinearLayoutManager(requireActivity())
        val tempNewArray = ArrayList<String>()
        tempNewArray.add("Heat's Jimmy Butler undeterred: 'I know that we will do it'")
        tempNewArray.add("The bonkers final seconds that saved Boston's season")

        val newsAdapter = NewsAdapter(requireActivity(), tempNewArray, this)
        binding.rvNews.adapter = newsAdapter


        binding.rvLeagueList.hasFixedSize()
        val layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvLeagueList.layoutManager = layoutManager
        binding.rvLeagueList.setNestedScrollingEnabled(false);

        binding.rvLeagueList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstItemPOsition = layoutManager.findFirstVisibleItemPosition()
                val lastItem = firstItemPOsition + visibleItemCount

                if (!recyclerView.isComputingLayout && recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE) {
                    ladapter.notifyDataSetChanged()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })

    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun setupTabLayout() {
    }

    private fun setupViewPager() {
        val imageAdapter = ViewPagerAdapter(this, images)
        binding.viewpagerHome.adapter = imageAdapter

        val adapter2 = ViewPagerAdapterMainActivity(requireActivity(), 2)
        binding.viewpager.adapter = adapter2
    }

    fun setViewPagerClick(position: Int){
        if(position == 0){
            showFullScreenPopup()
        } else {
            val intent = Intent(requireActivity(), CreateLeagueActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showFullScreenPopup() {
        val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        val view = LayoutInflater.from(context).inflate(R.layout.full_screen_popup_layout, null)

        view.findViewById<ImageView>(R.id.image_popup_full).setImageResource(R.drawable.home_img_one)
        view.setOnClickListener { dialog.dismiss() }
        dialog.setContentView(view)
        dialog.show()
    }


    private fun getApiCalling() {
        binding.llCardLayout.visibility = View.VISIBLE
        apiInterface.getLeagueList(userid).enqueue(object : Callback<UserLeagueResponse> {
            @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onResponse(
                call: Call<UserLeagueResponse>,
                response: Response<UserLeagueResponse>
            ) {
                try {
                    val res = response.body()
                    if (response.isSuccessful) {
                        if (res?.success == true) {
                            binding.CLRvLeagueList.visibility = View.VISIBLE
                            binding.pbBar.visibility = View.GONE
                            leaguelist = res.userComLeagues + res.userLeagues
                            ladapter = LeagueListAdapter(
                                requireActivity(),
                                leaguelist as ArrayList<UserLeagueResponse.UserLeague>,
                                this@MainFragment
                            )
                            binding.rvLeagueList.adapter = ladapter
                            binding.indicator.attachToRecyclerView(binding.rvLeagueList)

                            val snapHelper: SnapHelper = LinearSnapHelper()
                            snapHelper.attachToRecyclerView(binding.rvLeagueList)
                            binding.rvLeagueList.parent.requestDisallowInterceptTouchEvent(true)
                            binding.indicator2.attachToRecyclerView(binding.rvLeagueList, snapHelper)

                            ladapter.registerAdapterDataObserver(binding.indicator2.adapterDataObserver)

                            ladapter.notifyDataSetChanged()
                            ladapter.notifyItemChanged(binding.rvLeagueList.id)

                            if (res.userLeagues.isEmpty()) {
                                binding.tvWelcome.visibility = View.VISIBLE
                            } else {
                                binding.tvWelcome.visibility = View.GONE
                            }

                            ladapter.onItemClick = {
                                val dialog = BottomSheetDialog(requireActivity())

                                val view =
                                    layoutInflater.inflate(R.layout.layout_bottom_sheet_rank, null)
                                val rvRank =
                                    view.findViewById<RecyclerView>(R.id.rv_bottom_sheet_rank_data)
                                val lm = LinearLayoutManager(
                                    requireActivity(),
                                    LinearLayoutManager.VERTICAL,
                                    false
                                )
                                rvRank.layoutManager = lm
                                val ranklist = ArrayList<Rank>()
                                ranklist.addAll(it)

                                dialog.setCancelable(true)
                                dialog.setCanceledOnTouchOutside(true)
                                dialog.setContentView(view)

                                dialog.show()

                                val rankAdapter = RankAdapter(requireActivity(), ranklist)
                                rvRank.adapter = rankAdapter
                                rankAdapter.notifyDataSetChanged()
                            }
                        } else {
                            binding.CLRvLeagueList.visibility = View.VISIBLE
                            binding.pbBar.visibility = View.GONE
                            binding.tvWelcome.visibility = View.VISIBLE
                            binding.tvWelcome.text = "" + name
                            binding.tvWelcome.setTextColor(requireActivity().getColor(R.color.bg))
                            binding.rvLeagueList.adapter = null
                            Log.d("success", "yyyy")
                            binding.llCardLayout.visibility = View.GONE
                        }

                    } else {
                        binding.CLRvLeagueList.visibility = View.VISIBLE
                        binding.pbBar.visibility = View.GONE
                        binding.tvWelcome.visibility = View.VISIBLE
                        binding.tvWelcome.text = "" + name
                        binding.tvWelcome.setTextColor(requireActivity().getColor(R.color.bg))
                        binding.rvLeagueList.adapter = null
                        Log.d("success", "yyyy")
                        binding.llCardLayout.visibility = View.GONE
                    }
                }catch (er:Exception){
                    binding.pbBar.visibility = View.GONE
                    binding.llCardLayout.visibility = View.GONE
                    binding.CLRvLeagueList.visibility = View.VISIBLE
                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected exception
             * occurred creating the request or processing the response.
             */
            override fun onFailure(call: Call<UserLeagueResponse>, t: Throwable) {
                call.cancel()
                Log.d("vr", "" + t.message)
                binding.pbBar.visibility = View.GONE
                binding.llCardLayout.visibility = View.GONE
                binding.CLRvLeagueList.visibility = View.VISIBLE
            }

        })
    }

    private fun getApiCalling2() {
        binding.llCardLayout.visibility = View.VISIBLE
        apiInterface.getLeagueList(userid).enqueue(object : Callback<UserLeagueResponse> {
            @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onResponse(
                call: Call<UserLeagueResponse>,
                response: Response<UserLeagueResponse>
            ) {
                val res = response.body()
                if (response.isSuccessful) {
                    if (res?.success == true) {
                        //leaguelist.clear()
                        binding.CLRvLeagueList.visibility = View.VISIBLE
                        leaguelist = res.userComLeagues + res.userLeagues
                        ladapter = LeagueListAdapter(
                            requireActivity(),
                            leaguelist as ArrayList<UserLeagueResponse.UserLeague>,
                            this@MainFragment
                        )
                        binding.rvLeagueList.adapter = ladapter
                        binding.indicator.attachToRecyclerView(binding.rvLeagueList)

                        val snapHelper: SnapHelper = LinearSnapHelper()
                        binding.rvLeagueList.parent.requestDisallowInterceptTouchEvent(true)
                        binding.indicator2.attachToRecyclerView(binding.rvLeagueList, snapHelper)

                        ladapter.registerAdapterDataObserver(binding.indicator2.adapterDataObserver)

                        ladapter.notifyDataSetChanged()
                        ladapter.notifyItemChanged(binding.rvLeagueList.id)
                        if (res.userLeagues.isEmpty()) {
                            binding.tvWelcome.visibility = View.VISIBLE
                        } else {
                            binding.tvWelcome.visibility = View.GONE

                        }

                    } else {
                        binding.CLRvLeagueList.visibility = View.VISIBLE
                        binding.pbBar.visibility = View.GONE
                        binding.tvWelcome.visibility = View.VISIBLE
                        binding.tvWelcome.text = "" + name
                        binding.tvWelcome.setTextColor(requireActivity().getColor(R.color.bg))
                        binding.llCardLayout.visibility = View.GONE
                    }
                } else {
                    binding.CLRvLeagueList.visibility = View.VISIBLE
                    binding.pbBar.visibility = View.GONE
                    binding.tvWelcome.visibility = View.VISIBLE
                    binding.tvWelcome.text = "" + name
                    binding.tvWelcome.setTextColor(requireActivity().getColor(R.color.bg))
                    Log.d("success", "yyyy")
                    binding.llCardLayout.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<UserLeagueResponse>, t: Throwable) {
                call.cancel()
            }
        })
    }

    private fun showDialoge2() {
        val progressDialogeBind = layoutInflater.inflate(R.layout.custom_dialogebox, null)
        progressDialogue = Dialog(binding.root.context)
        progressDialogue.setContentView(progressDialogeBind)

        val civ_progress = progressDialogeBind.findViewById<CircleImageView>(R.id.civ_progress)

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

    private fun CustomHeader() {
//        iv_menu = findViewById(R.id.iv_menu)
//        iv_back = findViewById(R.id.iv_back)
//        tv_name_app = findViewById(R.id.tv_name_app)

//        val header: View = binding.navigationView.getHeaderView(0)
//        tv_nav_username_ = header.findViewById<View>(R.id.tv_nav_username_) as TextView

        binding.imgLogout.setOnClickListener {
            LogOut()
        }
    }

    private fun setEvents() {
        binding.LLCreateLeague.setOnClickListener {
            val intent = Intent(requireActivity(), CreateLeagueActivity::class.java)
            startActivity(intent)
        }

        binding.LLJoinLeague.setOnClickListener {
            /*val intent = Intent(requireActivity(),TeamActivity::class.java)
            startActivity(intent)*/
            JoinLeague()
        }



        binding.tvLogin.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }
        binding.tvSignup.setOnClickListener {
            val intent = Intent(requireActivity(), RegisterActivity::class.java)
            startActivity(intent)
        }


//        val tempNews: Fragment = NewsFragment()
//        requireActivity().supportFragmentManager.beginTransaction()
//            .replace(R.id.news_container, tempNews).commit()

        val tempPlayer: Fragment = TopPlayerFragment()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.player_container, tempPlayer)
            .commit()

        binding.tvNewsMore.setOnClickListener {

        }

        binding.tvNews.setOnClickListener {
            kr = "NEWS"
            binding.tvTopPlayer.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_news_bg_text_right_blank
                )
            )
            binding.tvNews.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_news_bg_text_left
                )
            )
            binding.playerContainer.visibility = View.GONE
            binding.newsContainer.visibility = View.VISIBLE
        }


        binding.tvTopPlayer.setOnClickListener {
            kr = "TopPlayer"
            binding.tvNews.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_news_bg_text_left_blank
                )
            )
            binding.tvTopPlayer.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireActivity(),
                    R.drawable.ic_news_bg_text_right
                )
            )
            binding.playerContainer.visibility = View.VISIBLE
            binding.newsContainer.visibility = View.GONE

        }

    }


    private fun JoinLeague() {
        dialogHelper!!.create()
        dialogHelper!!.setDialogData("Done")
    }


    private fun MessageForValid(s: String) {
        val snackbar = TSnackbar.make(
            requireActivity().findViewById(android.R.id.content),
            s,
            TSnackbar.LENGTH_SHORT
        )
        // snackbar.setActionTextColor(Color.WHITE)
        val snackbarView = snackbar.view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackbarView.setBackgroundColor(requireActivity().getColor(R.color.bg))
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

    override fun onBtnSheetItemClicked(buttonText: String) {
        showDialoge2()
        if(buttonText == "ok"){
            SharedPrefManager.getInstance(requireActivity()).Logout()
            SharedPrefManager.getInstance(requireActivity()).clear()
            Handler(Looper.getMainLooper()).postDelayed({
                val i = Intent(requireActivity(), ChoiceActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                requireActivity().finish()
            }, 1000)
        }else{
            apiInterface.joinLeagueWithCode(JoinLeagueModel(buttonText, userid))
                .enqueue(object : Callback<JoinLeagueResponse> {
                    override fun onResponse(
                        call: Call<JoinLeagueResponse>,
                        response: Response<JoinLeagueResponse>
                    ) {
                        val joinLeagueResponse: JoinLeagueResponse? = response.body()
                        Log.d("VR", " response code " + response.code())
                        Log.d("VR", " response code " + response.message())
                        if (response.code() == Constant.REQUEST_STATUS_CODE_SUCCESS) {
                            if (joinLeagueResponse != null) {
                                if (joinLeagueResponse.success) {
                                    getApiCalling2()
                                    progressDialogue.dismiss()
                                    MessageForValid("" + joinLeagueResponse.message.toString())
                                } else {
                                    getApiCalling2()
                                    progressDialogue.dismiss()
                                    MessageForValid("" + joinLeagueResponse.message.toString())

                                }
                            }
                            Log.d("VR", "if " + response.code())

                        } else if (response.code() == Constant.REQUEST_STATUS_CODE_FAILED) {
                            getApiCalling2()
                            Log.d("VR", " else if response " + response.body())
                            progressDialogue.dismiss()
                            val errorResponse = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ErrorResponse::class.java
                            )
                            MessageForValid("" + errorResponse.message.toString())
                            Log.d("VR", " else if response " + errorResponse.message)
                        } else if (response.code() == Constant.REQUEST_STATUS_CODE_UNPROCESSABLE) {
                            getApiCalling2()
                            Log.d("VR", " else if response " + response.body())
                            progressDialogue.dismiss()
                            val errorResponse = Gson().fromJson(
                                response.errorBody()!!.charStream(),
                                ErrorResponse::class.java
                            )
                            MessageForValid("" + errorResponse.message.toString())

                        } else {
                            getApiCalling2()
                            Log.d("VR", " else if response " + response.body())
                            progressDialogue.dismiss()
                            MessageForValid("You already join this league.")
                        }
                        dialogHelper!!.dismiss()
                    }

                    override fun onFailure(call: Call<JoinLeagueResponse>, t: Throwable) {
                        progressDialogue.dismiss()
                        dialogHelper!!.dismiss()
                        call.cancel()
                    }

                })
        }
    }

    override fun onNewsItemClick() {
        try {
            if(requireActivity() is BottomNavigationActivity){
                val act = requireActivity() as BottomNavigationActivity
                val fragment = NewsFragment()
                act.addFragment(fragment)
            }
        }catch (er: Exception){
            Log.d("LoadFrag", er.message.toString())
        }
    }
}
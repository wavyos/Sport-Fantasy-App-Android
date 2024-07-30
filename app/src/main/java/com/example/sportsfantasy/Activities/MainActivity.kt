@file:Suppress("PackageName")

package com.example.sportsfantasy.Activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.androidadvance.topsnackbar.TSnackbar
import com.example.sportsfantasy.Adapter.LeagueListAdapter
import com.example.sportsfantasy.Adapter.NewsAdapter
import com.example.sportsfantasy.Adapter.PlayerTab.AllPlayerDetailsAdapter
import com.example.sportsfantasy.Adapter.PlayerTab.AllPlayerNameAdapter
import com.example.sportsfantasy.Adapter.RankAdapter
import com.example.sportsfantasy.Adapter.ViewPagerAdapterMainActivity
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Fragment.NewsFragment
import com.example.sportsfantasy.Fragment.TopPlayerFragment
import com.example.sportsfantasy.Interface.Listner
import com.example.sportsfantasy.Interface.NewsItemClick
import com.example.sportsfantasy.Model.*
import com.example.sportsfantasy.Model.PlayerF.playerTabModel
import com.example.sportsfantasy.Model.PlayerF.playerTabResponse
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.Utils.CheckNetworkConnection
import com.example.sportsfantasy.Utils.Constant
import com.example.sportsfantasy.Utils.CustomSnackbar
import com.example.sportsfantasy.Utils.LoadingDialoge
import com.example.sportsfantasy.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


@SuppressLint("SetTextI18n")
@Suppress("DEPRECATION")
class MainActivity : BaseActivity(),Listner, BottomNavigationView.OnNavigationItemSelectedListener, NewsItemClick {
    private lateinit var binding: ActivityMainBinding
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

    lateinit var userid: String

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
    var searchTabPlayerList = ArrayList<com.example.sportsfantasy.Model.SearchTab.Data>()
    var playerAllData = ArrayList<com.example.sportsfantasy.Model.Player>()

    lateinit var allPlayerNameAdapter: AllPlayerNameAdapter
    lateinit var allPlayerDetailsAdapter: AllPlayerDetailsAdapter

    //exoplayer
    private var player: ExoPlayer? = null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        allocatememory()
        CustomHeader()
        arguments()
        NavDrawer()

        setUpVideoPlayer()

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        myEdit = sharedPreferences.edit()
        Token = sharedPreferences.getString("access_token", null).toString()

        val back = intent.getStringExtra("league")

        setEvents()

        setupViewPager()

        setupTabLayout()

        val handler = Handler()
        handler.postDelayed({
            val h1: Int = binding.rvLeagueList.width
            val h2: Int = binding.drawer.width
            binding.rvLeagueList.smoothScrollToPosition(h2-h1)
        }, 1000)

        updatedevicetoken()
        binding.tvMore.setOnClickListener {
//            Toast.makeText(this, "View more clicked", Toast.LENGTH_SHORT).show()
//            val intent = Intent(applicationContext, TopPlayerViewMoreActivity::class.java)
//            startActivity(intent)
            val intent = Intent(applicationContext, TeamActivity::class.java)
            intent.putExtra("ChooseTab","TopPlayer")
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        if (player == null){
            setUpVideoPlayer()
        }
    }

    override fun onResume() {
        super.onResume()
        if (player == null){
            setUpVideoPlayer()
        }
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            exoPlayer.stop()
            exoPlayer.release()
        }
        player = null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (player != null){
            player!!.playWhenReady = false
            releasePlayer()
        }
    }

    public override fun onPause() {
        super.onPause()
        if (player != null){
            player!!.playWhenReady = false
            releasePlayer()
        }
    }


    public override fun onStop() {
        super.onStop()
        if (player != null){
            player!!.playWhenReady = false
            releasePlayer()
        }
    }

    private fun setUpVideoPlayer() {
        try {
            val tempUri = Uri.parse("android.resource://" + packageName + "/"+R.raw.home)
            player = ExoPlayer.Builder(this)
                .build()
                .also { exoPlayer ->
                    binding.homeVideoView.player = exoPlayer
                }
                .also {
                    val mediaItem = MediaItem.fromUri(tempUri)
                    it.setMediaItem(mediaItem)
                    it.repeatMode = REPEAT_MODE_ALL
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


        }catch (er: Exception){
            Toast.makeText(this, "${er.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun arguments() {
        if (SharedPrefManager.getInstance(applicationContext).ULoggedIn) {
            Log.d("VR", " if " + SharedPrefManager.getInstance(applicationContext).ULoggedIn)
            headerToken = SharedPrefManager.getInstance(applicationContext).getLogin.access_token
            //navigation header
//            tv_nav_username_.text =
//                "" + SharedPrefManager.getInstance(applicationContext).getLogin.userDetails.name
            userid =
                SharedPrefManager.getInstance(applicationContext).getLogin.userDetails.id.toString()
            // login and sign up button and card view  //
            binding.llCreateJoin.visibility = View.VISIBLE
//            binding.llCreateJoin2.visibility = View.GONE
            binding.llCardLayout.visibility = View.VISIBLE
            // login and sign up button and card view  //
            name = SharedPrefManager.getInstance(applicationContext).getLogin.userDetails.name

            binding.tvWelcome.visibility = View.VISIBLE
            name = SharedPrefManager.getInstance(applicationContext).getRegister.userDetails.name

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
            Log.d("VR", " if " + SharedPrefManager.getInstance(applicationContext).ULoggedIn)
            getApiCalling()
            getPlayerData()


        } else if (SharedPrefManager.getInstance(applicationContext).RLoggedIn) {
            Log.d("VR", " if " + SharedPrefManager.getInstance(applicationContext).RLoggedIn)
            headerToken = SharedPrefManager.getInstance(applicationContext).getRegister.access_token
//            tv_nav_username_.text =
//                "" + SharedPrefManager.getInstance(applicationContext).getRegister.userDetails.name
            binding.checkLogin.visibility = View.GONE
//            binding.navigationView.visibility = View.VISIBLE
            Log.d("VR", " if " + SharedPrefManager.getInstance(applicationContext).RLoggedIn)


            binding.tvWelcome.visibility = View.VISIBLE
            name = SharedPrefManager.getInstance(applicationContext).getRegister.userDetails.name

            binding.tvWelcome.text = "Welcome " + name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }

            binding.llCreateJoin.visibility = View.VISIBLE
            binding.llCardLayout.visibility = View.GONE
            userid =
                SharedPrefManager.getInstance(applicationContext).getRegister.userDetails.id.toString()
            getApiCalling()
            getPlayerData()

        } else {
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

            binding.llCreateJoin.visibility = View.VISIBLE
            binding.llCardLayout.visibility = View.GONE

            binding.tvTopPlayer.isEnabled = false
            binding.tvTopPlayer.isClickable = false

            userid = ""
            binding.tvTopPlayer.isFocusable = false
            name = ""


        }
    }


    private fun NavDrawer() {
        //start drawer//
//        toggle =
//            ActionBarDrawerToggle(this, binding.drawer, R.string.OpenDrawer, R.string.CloseDrawer)
//        binding.drawer.addDrawerListener(toggle)
//        toggle.syncState()
//        binding.navigationView.bringToFront()
//
//        val menu: Menu = binding.navigationView.menu
//        if (SharedPrefManager.getInstance(applicationContext).ULoggedIn) {
//            menu.findItem(R.id.logout).title = getString(R.string.logout)
//        } else if (SharedPrefManager.getInstance(applicationContext).RLoggedIn) {
//            menu.findItem(R.id.logout).title = getString(R.string.logout)
//        } else {
//            menu.findItem(R.id.logout).title = getString(R.string.login)
//        }

        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
        //end drawer//
    }

    private fun allocatememory() {
        snack_cust = CustomSnackbar(this)

        clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager

        leaguelist = ArrayList<UserLeagueResponse.UserLeague>()

        loading = LoadingDialoge(this)

        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(applicationContext)

        binding.rvPlayersName.layoutManager = LinearLayoutManager(this)
        binding.rvPlayersDetails.layoutManager = LinearLayoutManager(this)

        //latest news design data
        binding.rvNews.layoutManager = LinearLayoutManager(this)
        val tempNewArray = ArrayList<String>()
        tempNewArray.add("Heat's Jimmy Butler undeterred: 'I know that we will do it'")
        tempNewArray.add("The bonkers final seconds that saved Boston's season")

        val  newsAdapter = NewsAdapter(this@MainActivity, tempNewArray, this)
        binding.rvNews.adapter = newsAdapter


        binding.rvLeagueList.hasFixedSize()
        val layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        binding.rvLeagueList.layoutManager = layoutManager
        binding.rvLeagueList.setNestedScrollingEnabled(false);

        binding.rvLeagueList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val visibleItemCount: Int = layoutManager.childCount
                val totalItemCount: Int = layoutManager.itemCount
                val firstItemPOsition = layoutManager.findFirstVisibleItemPosition()
                val lastItem = firstItemPOsition + visibleItemCount

                if (!recyclerView.isComputingLayout && recyclerView.scrollState == SCROLL_STATE_IDLE) {
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
        val adapter2 = ViewPagerAdapterMainActivity(this, 2)
        binding.viewpager.adapter = adapter2
    }

    private fun getApiCalling() {
        binding.llCardLayout.visibility = View.VISIBLE
        binding.shimmer.startShimmer()
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
                        binding.shimmer.stopShimmer()
                        binding.shimmer.visibility = View.GONE
                        binding.CLRvLeagueList.visibility = View.VISIBLE
                        binding.pbBar.visibility = View.GONE
                        leaguelist =  res.userComLeagues + res.userLeagues
                        ladapter = LeagueListAdapter(
                            this@MainActivity,
                            leaguelist as ArrayList<UserLeagueResponse.UserLeague>,
                            this@MainActivity
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
                            val dialog = BottomSheetDialog(this@MainActivity)

                            val view = layoutInflater.inflate(R.layout.layout_bottom_sheet_rank, null)
                            val rvRank = view.findViewById<RecyclerView>(R.id.rv_bottom_sheet_rank_data)
                            val lm = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
                            rvRank.layoutManager = lm
                            val ranklist = ArrayList<Rank>()
                            ranklist.addAll(it)

                            dialog.setCancelable(true)
                            dialog.setCanceledOnTouchOutside(true)
                            dialog.setContentView(view)

                            dialog.show()

                            val rankAdapter = RankAdapter(applicationContext,ranklist)
                            rvRank.adapter = rankAdapter
                            rankAdapter.notifyDataSetChanged()
                        }
                    } else {
                        binding.shimmer.stopShimmer()
                        binding.shimmer.visibility = View.GONE
                        binding.CLRvLeagueList.visibility = View.VISIBLE
                        binding.pbBar.visibility = View.GONE
                        binding.tvWelcome.visibility = View.VISIBLE
                        binding.tvWelcome.text = "" + name
                        binding.tvWelcome.setTextColor(getColor(R.color.bg))
                        binding.rvLeagueList.adapter = null
                        Log.d("success", "yyyy")
                        binding.llCardLayout.visibility = View.GONE
                    }

                } else {
                    binding.shimmer.stopShimmer()
                    binding.shimmer.visibility = View.GONE
                    binding.CLRvLeagueList.visibility = View.VISIBLE
                    binding.pbBar.visibility = View.GONE
                    binding.tvWelcome.visibility = View.VISIBLE
                    binding.tvWelcome.text = "" + name
                    binding.tvWelcome.setTextColor(getColor(R.color.bg))
                    binding.rvLeagueList.adapter = null
                    Log.d("success", "yyyy")
                    binding.llCardLayout.visibility = View.GONE
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
                binding.shimmer.stopShimmer()
                binding.shimmer.visibility = View.GONE
                binding.CLRvLeagueList.visibility = View.VISIBLE
            }

        })
    }

    private fun getApiCalling2() {
        binding.llCardLayout.visibility = View.VISIBLE
        binding.shimmer.startShimmer()
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
                        binding.shimmer.stopShimmer()
                        binding.shimmer.visibility = View.GONE
                        binding.CLRvLeagueList.visibility = View.VISIBLE
                        leaguelist = res.userComLeagues + res.userLeagues
                        ladapter = LeagueListAdapter(
                            this@MainActivity,
                            leaguelist as ArrayList<UserLeagueResponse.UserLeague>,
                            this@MainActivity
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
                        binding.shimmer.stopShimmer()
                        binding.shimmer.visibility = View.GONE
                        binding.CLRvLeagueList.visibility = View.VISIBLE
                        binding.pbBar.visibility = View.GONE
                        binding.tvWelcome.visibility = View.VISIBLE
                        binding.tvWelcome.text = "" + name
                        binding.tvWelcome.setTextColor(getColor(R.color.bg))
                        binding.llCardLayout.visibility = View.GONE
                    }

                } else {
                    binding.shimmer.stopShimmer()
                    binding.shimmer.visibility = View.GONE
                    binding.CLRvLeagueList.visibility = View.VISIBLE
                    binding.pbBar.visibility = View.GONE
                    binding.tvWelcome.visibility = View.VISIBLE
                    binding.tvWelcome.text = "" + name
                    binding.tvWelcome.setTextColor(getColor(R.color.bg))
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
    }

    private fun setEvents() {
        binding.LLCreateLeague.setOnClickListener {
            val intent = Intent(applicationContext, CreateLeagueActivity::class.java)
            startActivity(intent)
        }

        binding.LLJoinLeague.setOnClickListener {
            /*val intent = Intent(applicationContext,TeamActivity::class.java)
            startActivity(intent)*/
            JoinLeague()
        }



        binding.tvLogin.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.tvSignup.setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
        }


        val tempNews: Fragment = NewsFragment()
        supportFragmentManager.beginTransaction().replace(R.id.news_container, tempNews).commit()

        val tempPlayer: Fragment = TopPlayerFragment()
        supportFragmentManager.beginTransaction().replace(R.id.player_container, tempPlayer)
            .commit()


        binding.tvNews.setOnClickListener {
            kr = "NEWS"
            binding.tvTopPlayer.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_news_bg_text_right_blank
                )
            )
            binding.tvNews.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
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
                    applicationContext,
                    R.drawable.ic_news_bg_text_left_blank
                )
            )
            binding.tvTopPlayer.setBackgroundDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.ic_news_bg_text_right
                )
            )
            binding.playerContainer.visibility = View.VISIBLE
            binding.newsContainer.visibility = View.GONE

        }

    }


    private fun JoinLeague() {
        // Toast.makeText(applicationContext,"kk",Toast.LENGTH_LONG).show()
        val dialogebind = layoutInflater.inflate(R.layout.join_league_dialogebox, null)
        val myDialoge = Dialog(this)
        myDialoge.setContentView(dialogebind)

        val Close_join_league = myDialoge.findViewById(R.id.iv_closed) as CircleImageView
        val edt_Invite_Code = myDialoge.findViewById(R.id.edt_invite_code) as EditText
        val tv_join_league = myDialoge.findViewById(R.id.tv_join_league) as TextView
        val cv_invite_code = myDialoge.findViewById(R.id.cv_invite_code) as CardView

        tv_join_league.setOnClickListener {
            val InviteCode = edt_Invite_Code.text.toString().trim()
            if (InviteCode == "") {
                MessageForValid("Blank invitation codes are not allowed.")
                cv_invite_code.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake))
            } else {
                showDialoge2()
                apiInterface.joinLeagueWithCode(JoinLeagueModel(InviteCode, userid))
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
                                        myDialoge.dismiss()
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
                                myDialoge.dismiss()
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
                                myDialoge.dismiss()

                                val errorResponse = Gson().fromJson(
                                    response.errorBody()!!.charStream(),
                                    ErrorResponse::class.java
                                )
                                MessageForValid("" + errorResponse.message.toString())

                            } else {
                                getApiCalling2()
                                Log.d("VR", " else if response " + response.body())
                                progressDialogue.dismiss()
                                myDialoge.dismiss()
                                MessageForValid("You already join this league.")
                            }


                        }

                        override fun onFailure(call: Call<JoinLeagueResponse>, t: Throwable) {
                            progressDialogue.dismiss()
                            call.cancel()
                        }

                    })

            }
        }

        Close_join_league.setOnClickListener {
            myDialoge.dismiss()
        }

        myDialoge.setCancelable(false)
        myDialoge.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialoge.show()
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



    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val back = intent.getStringExtra("league")

        if (back == "back") {

            AlertDialog.Builder(this)
                .setTitle(R.string.areyousure)
                .setCancelable(true)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, object : DialogInterface.OnClickListener {
                    override fun onClick(arg0: DialogInterface?, arg1: Int) {
                        finishAffinity()
                    }
                }).create().show()
        } else {
            AlertDialog.Builder(this)
                .setTitle(R.string.areyousure)
                .setCancelable(true)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, object : DialogInterface.OnClickListener {
                    override fun onClick(arg0: DialogInterface?, arg1: Int) {
                        finishAffinity()
                    }
                }).create().show()

            // super.onBackPressed()
        }

//        super.onBackPressed()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId) {
            R.id.page_1 -> {
                // Respond to navigation item 1 click
                Toast.makeText(this, getString(R.string.str_work_in_progress), Toast.LENGTH_SHORT).show()
                true
            }
            R.id.page_2-> {
                // Respond to navigation item 2 click
                Toast.makeText(this, getString(R.string.str_work_in_progress), Toast.LENGTH_SHORT).show()
                true
            }
            R.id.page_3-> {
                // Respond to navigation item 2 click
                Toast.makeText(this, getString(R.string.str_work_in_progress), Toast.LENGTH_SHORT).show()
                true
            }
            R.id.page_4-> {
                // Respond to navigation item 2 click
                Toast.makeText(this, getString(R.string.str_work_in_progress), Toast.LENGTH_SHORT).show()
                true
            }
            R.id.page_5-> {
                // Respond to navigation item 2 click
                Toast.makeText(this, getString(R.string.str_work_in_progress), Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }

    private fun LogOut() {

        AlertDialog.Builder(this)
            .setMessage(R.string.is_sure_logout)
            .setTitle(getString(R.string.app_name))
            .setCancelable(false)
            .setNegativeButton(R.string.no, null)
            .setPositiveButton(R.string.yes, object : DialogInterface.OnClickListener {
                override fun onClick(arg0: DialogInterface?, arg1: Int) {
//                    showDialoge2()
                    SharedPrefManager.getInstance(applicationContext).Logout()
                    SharedPrefManager.getInstance(applicationContext).clear()
                    Handler(Looper.getMainLooper()).postDelayed({
                        val i = Intent(applicationContext, ChoiceActivity::class.java)
                        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(i)
                        finish()
                    }, 1000)


                }
            }).create().show()


    }

    override fun onDraftClickListner(position: Int, createdLeague: UserLeagueResponse.UserLeague) {


//        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
//        var editor = sharedPreference.edit()
//
//        val intent = Intent(applicationContext, LeagueActivity::class.java)
//        intent.putExtra("league_name", "" + createdLeague.leagueName)
//        intent.putExtra("league_id", "" + createdLeague.id)
//        editor.putString("league_name", "" + createdLeague.leagueName)
//        editor.putString("league_id", "" + createdLeague.id)
//        editor.commit()
//        startActivity(intent)
        Toast.makeText(this, getString(R.string.str_work_in_progress), Toast.LENGTH_SHORT).show()

    }

    override fun onTeamClickListner(position: Int, createdLeague: UserLeagueResponse.UserLeague) {
//        val intent = Intent(applicationContext, TeamActivity::class.java)
//        intent.putExtra("ChooseTab","MyTeam")
//        sharedPrefManager.saveLeague(createdLeague)
//        startActivity(intent)
        Toast.makeText(this, getString(R.string.str_work_in_progress), Toast.LENGTH_SHORT).show()
    }

    override fun onMatchUpClickListner(
        position: Int,
        createdLeague: UserLeagueResponse.UserLeague
    ) {
//        val intent = Intent(applicationContext, TeamActivity::class.java)
//        intent.putExtra("ChooseTab","Matchup")
//        sharedPrefManager.saveLeague(createdLeague)
//        startActivity(intent)
        Toast.makeText(this, getString(R.string.str_work_in_progress), Toast.LENGTH_SHORT).show()
    }


    override fun onInviteItemClickListner(
        position: Int,
        createdLeague: UserLeagueResponse.UserLeague
    ) {
        // Toast.makeText(applicationContext,"kk",Toast.LENGTH_LONG).show()
        val dialogebind = layoutInflater.inflate(R.layout.invite_dialogebox, null)
        val myDialoge = Dialog(this)
        myDialoge.setContentView(dialogebind)

        val nobtn = myDialoge.findViewById(R.id.iv_closed) as CircleImageView
        val Invite_Code = myDialoge.findViewById(R.id.tv_invite_code) as TextView
        val Invite_Link = myDialoge.findViewById(R.id.tv_ivite_link) as TextView
        val Share_WeChat = myDialoge.findViewById(R.id.tv_share_wechat) as TextView

        Invite_Code.setOnClickListener {
            myDialoge.dismiss()
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
        Invite_Link.setOnClickListener {
            myDialoge.dismiss()
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
        Share_WeChat.setOnClickListener {

            val launchIntent = packageManager.getLaunchIntentForPackage("com.michatapp.im")
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


        nobtn.setOnClickListener {
            myDialoge.cancel()
        }

        myDialoge.setCancelable(false)
        myDialoge.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialoge.show()

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

                        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
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

    @SuppressLint("NotifyDataSetChanged")
    fun setNewData(currentItem: UserLeagueResponse.UserLeague, position: Int) {

    }

    private fun getPlayerData()
    {
        searchTabPlayerList.clear()
        showDialoge2()
        apiInterface.playerAll("Bearer $headerToken", playerTabModel(0,userid.toInt())).enqueue(object : Callback<playerTabResponse>{
            override fun onResponse(call: Call<playerTabResponse>, response: Response<playerTabResponse>)
            {
                val tabResponse = response.body()

                if (tabResponse != null)
                {
                    if (tabResponse.success)
                    {
                        playerAllData.clear()

                        playerAllData.addAll(tabResponse.data.position_All)

                        if (tabResponse.data.position_All.isEmpty())
                        {
                            binding.LlTopPlayerAll.visibility = View.GONE
                        }
                        else
                        {
                            binding.LlTopPlayerAll.visibility = View.VISIBLE

                            allPlayerNameAdapter = AllPlayerNameAdapter(this@MainActivity, playerAllData, true)
                            binding.rvPlayersName.adapter = allPlayerNameAdapter

                            allPlayerDetailsAdapter = AllPlayerDetailsAdapter(this@MainActivity,playerAllData, true)
                            binding.rvPlayersDetails.adapter = allPlayerDetailsAdapter
                        }
                    }
                }
                progressDialogue.dismiss()
            }
            override fun onFailure(call: Call<playerTabResponse>, t: Throwable)
            {
                progressDialogue.dismiss()
                call.cancel()
            }
        })
    }

    override fun onNewsItemClick() {
        TODO("Not yet implemented")
    }
}
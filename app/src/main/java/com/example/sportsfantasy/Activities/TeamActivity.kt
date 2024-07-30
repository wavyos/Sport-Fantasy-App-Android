package com.example.sportsfantasy.Activities

import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.sportsfantasy.Adapter.TeamViewPagerAdapter
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.ActivityTeamBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


//13 no
class TeamActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityTeamBinding
    lateinit var sharedPrefManager: SharedPrefManager
    var HeaderToken = ""
    var User_id = ""
    var LeagueId = ""
    lateinit var toggle: ActionBarDrawerToggle
    var UserName = ""
    var LeagueName = ""
    var isPlayOff = true
    var isFirstTime = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefManager = SharedPrefManager.getInstance(applicationContext)
        Arguments()

        NavDrawer()
        binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        val iv_menu: ImageView = findViewById(R.id.iv_menu)
        val iv_back: ImageView = findViewById(R.id.iv_back)
        val tv_name_app: TextView = findViewById(R.id.tv_name_app)

        binding.viewPager.isUserInputEnabled = false

        iv_menu.setOnClickListener {
            binding.drawer.openDrawer(GravityCompat.END)
        }

        iv_back.setOnClickListener {
            onBackPressed()
        }

        setupViewPager()
        setupTabLayout()
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
            UserName = ""
        }

        LeagueId = sharedPrefManager.getLeague.id
        LeagueName = sharedPrefManager.getLeague.league_name
        isPlayOff = sharedPrefManager.getLeague.is_postseason == "1"

        binding.tvName.text = UserName

    }

    private fun setupTabLayout() {

        TabLayoutMediator(binding.tabLayout, binding.viewPager)
        { tab, index ->
            tab.text =
                if (isPlayOff) {
                    when (index) {
                        0 -> {
                            resources.getString(R.string.roster)
                        }
                        1 -> {
                            resources.getString(R.string.matchup)
                        }
                        2 -> {
                            resources.getString(R.string.playoff)
                        }
                        3 -> {
                            resources.getString(R.string.player_c)
                        }
                        4 -> {
                            resources.getString(R.string.league)
                        }
                        else -> {
                            throw Resources.NotFoundException("Position Not Found")
                        }
                    }
                } else {
                    when (index) {
                        0 -> {
                            resources.getString(R.string.roster)
                        }
                        1 -> {
                            resources.getString(R.string.matchup)
                        }
                        2 -> {
                            resources.getString(R.string.player_c)
                        }
                        3 -> {
                            resources.getString(R.string.league)
                        }
                        else -> {
                            throw Resources.NotFoundException("Position Not Found")
                        }
                    }
                }

        }.attach()

        val Choose = intent.getStringExtra("ChooseTab")
        if (Choose == "MyTeam") {
            val tab: TabLayout.Tab = binding.tabLayout.getTabAt(0)!!
            tab.select()
//            binding.tabLayout.getTabAt(0)!!.select();
            Log.d("VRAJAN", "" + Choose)
        } else if (Choose == "Matchup") {
            binding.tabLayout.getTabAt(1)!!.select()
            Log.d("VRAJAN", "" + Choose)
        }else if (Choose == "TopPlayer") {
            binding.tabLayout.getTabAt(binding.tabLayout.tabCount - 2)!!.select();
            binding.tabLayout.visibility = View.GONE
            binding.llNameAndTab.visibility = View.GONE
        }

        binding.ivRight.setOnClickListener {

            if (binding.tabLayout.selectedTabPosition == 0) {
                binding.tabLayout.getTabAt(1)!!.select()
            } else if (binding.tabLayout.selectedTabPosition == 1) {
                binding.tabLayout.getTabAt(2)!!.select()
            } else if (binding.tabLayout.selectedTabPosition == 2) {
                binding.tabLayout.getTabAt(3)!!.select()
            } else if (binding.tabLayout.selectedTabPosition == 3) {
                binding.tabLayout.getTabAt(3)!!.select()
            } else {
                binding.tabLayout.getTabAt(3)!!.select()
            }

            Log.e("kkc", "setupTabLayout: " + binding.tabLayout.selectedTabPosition)

        }

        binding.ivLeft.setOnClickListener {

            if (binding.tabLayout.selectedTabPosition == 0) {
                binding.tabLayout.getTabAt(0)!!.select()
            } else if (binding.tabLayout.selectedTabPosition == 1) {
                binding.tabLayout.getTabAt(0)!!.select()
            } else if (binding.tabLayout.selectedTabPosition == 2) {
                binding.tabLayout.getTabAt(1)!!.select()
            } else if (binding.tabLayout.selectedTabPosition == 3) {
                binding.tabLayout.getTabAt(2)!!.select()
            } else {
                binding.tabLayout.getTabAt(0)!!.select()
                //             Toast.makeText(applicationContext, "last", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun NavDrawer() {
        //start drawer//
        toggle = ActionBarDrawerToggle(this, binding.drawer, R.string.OpenDrawer, R.string.CloseDrawer)
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()
        binding.navigationView.bringToFront()

        val menu: Menu = binding.navigationView.menu
        if (SharedPrefManager.getInstance(applicationContext).ULoggedIn) {
            menu.findItem(R.id.logout).title = getString(R.string.logout)
        } else if (SharedPrefManager.getInstance(applicationContext).RLoggedIn) {
            menu.findItem(R.id.logout).title = getString(R.string.logout)
        } else {
            menu.findItem(R.id.logout).title = getString(R.string.login)
        }

        binding.navigationView.setNavigationItemSelectedListener(this)
        binding.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    private fun setupViewPager() {
        if (isPlayOff) {
            val adapter = TeamViewPagerAdapter(this, 5, isPlayOff)
            binding.viewPager.adapter = adapter
        } else {
            val adapter = TeamViewPagerAdapter(this, 4, isPlayOff)
            binding.viewPager.adapter = adapter
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                if (SharedPrefManager.getInstance(applicationContext).ULoggedIn) {
                    LogOut()
                } else if (SharedPrefManager.getInstance(applicationContext).RLoggedIn) {
                    LogOut()
                } else {
                    val i = Intent(this@TeamActivity, ChoiceActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }
        }
        binding.drawer.closeDrawer(GravityCompat.END)
        return true
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
}
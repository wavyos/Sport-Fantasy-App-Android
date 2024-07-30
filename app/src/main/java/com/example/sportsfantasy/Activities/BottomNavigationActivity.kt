package com.example.sportsfantasy.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.sportsfantasy.Fragment.MainFragment
import com.example.sportsfantasy.Fragment.newFlow.CommunityNewsFragment
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.ActivityBottomNavigationBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding : ActivityBottomNavigationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
        loadFragment(MainFragment())
        setEvents()*/
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigation.setOnNavigationItemSelectedListener(this)
        loadFragment(MainFragment())
        setEvents()
    }

    private fun setEvents() {
        val back = intent.getStringExtra("league")
        if (back == "back") {

        }
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
//        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }

    fun addFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(fragment.tag)
        transaction.commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId) {
            R.id.page_1 -> {
                // Respond to navigation item 1 click
                loadFragment(MainFragment())
                true
            }
            R.id.page_2-> {
                // Respond to navigation item 2 click
//                loadFragment(CommunityNewsFragment())
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

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
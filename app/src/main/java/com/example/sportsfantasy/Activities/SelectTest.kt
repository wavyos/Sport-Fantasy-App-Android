package com.example.sportsfantasy.Activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AbsListView
import android.widget.HorizontalScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportsfantasy.Adapter.PlayerListAdapter
import com.example.sportsfantasy.Adapter.PlayerListAdapter_java
import com.example.sportsfantasy.Adapter.PlayerListColumnAdapter
import com.example.sportsfantasy.DataStorage.DbSqliteHelper
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Interface.PlayerListner
import com.example.sportsfantasy.Model.Player
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R

class SelectTest : BaseActivity(),PlayerListner
{

    lateinit var  rv_player_name:RecyclerView
    lateinit var  rv_players_details:RecyclerView

    lateinit var header_scroll:HorizontalScrollView
    lateinit var details_scroll:HorizontalScrollView

    lateinit var apiInterface: ApiInterface
    lateinit var sharedPrefManager: SharedPrefManager

    lateinit var tv_league_name: TextView
    lateinit var scroll_nested:NestedScrollView

    var postingCodeLeague: String = ""
    var League_id_Position: String = ""
    var Position_id: String = ""
    var User_id: String = ""
    var salary: String = ""
    var filterCode: String = "0"
    var remainSalary = 0

    var totalSmall = 0
    var totalStarter = 0
    var totalStar = 0
    var status: Long = 0

    var positionListId = 0
    lateinit var HeaderToken: String

    lateinit var playerListColumnAdapter: PlayerListColumnAdapter
    lateinit var playerListAdapter: PlayerListAdapter

    //java

    lateinit var adapterJava:PlayerListAdapter_java

    lateinit var nameManager:LinearLayoutManager
    lateinit var deatilsManager:LinearLayoutManager

    lateinit var dbSqliteHelper: DbSqliteHelper

    var getPlayersSqlite: ArrayList<Player> = ArrayList()

    var getPlayer:ArrayList<Player> = ArrayList()

    var isScrolling:Boolean = false

    var currentItem:Int = 0
    var totalItem:Int = 0
    var scrollOutItems:Int = 0

    var TAG = "SELECT_TEST"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test)
        allocatememory()
        getDataIntent()
        getSqliteData()

    }

    private fun getSqliteData()
    {
        getPlayersSqlite = dbSqliteHelper.getPlayersData(positionListId,filterCode.toInt())

        playerListColumnAdapter = PlayerListColumnAdapter(applicationContext, getPlayersSqlite, this@SelectTest)
        playerListAdapter = PlayerListAdapter(applicationContext, getPlayersSqlite, this@SelectTest)


        rv_player_name.adapter = playerListColumnAdapter
        rv_players_details.adapter = playerListAdapter

        rv_player_name.layoutManager = nameManager
        rv_players_details.layoutManager = deatilsManager


        rv_player_name.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling = true;
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItem = nameManager.childCount
                totalItem = nameManager.itemCount
                scrollOutItems = nameManager.findFirstVisibleItemPosition()

                if (isScrolling && (currentItem + scrollOutItems == totalItem))
                {
                    isScrolling =false;
                    fetchData()
                }
            }
        })
    }

    private fun fetchData()
    {
    }

    private fun getDataIntent()
    {
        postingCodeLeague = intent.getStringExtra("PG").toString()
        League_id_Position = intent.getStringExtra("league_id").toString()
        Position_id = intent.getStringExtra("position_id").toString()
        positionListId = intent.getIntExtra("positionListId", 0)

        tv_league_name.text = "Select $postingCodeLeague"

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
    }

    private fun allocatememory()
    {
        rv_player_name = findViewById(R.id.rv_players_column)
        rv_players_details = findViewById(R.id.rv_players)

        header_scroll = findViewById(R.id.hv_scroll_header)
        details_scroll = findViewById(R.id.rv_nested_horizontal)

        tv_league_name = findViewById(R.id.tv_league_name)

        nameManager = LinearLayoutManager(this)
        deatilsManager = LinearLayoutManager(this)

        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(applicationContext)


        dbSqliteHelper = DbSqliteHelper(applicationContext)

        getPlayer= ArrayList()


        scroll_nested = findViewById<NestedScrollView>(R.id.scroll_nested)

    }

    override fun onPlayerClick(position: Int, player: Player) {
        TODO("Not yet implemented")
    }

    override fun OnAddPlayerClick(position: Int, player: Player) {
        TODO("Not yet implemented")
    }

}
package com.example.sportsfantasy.Activities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.androidadvance.topsnackbar.TSnackbar
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Model.LeageFieldResponse
import com.example.sportsfantasy.Model.LoginErrorResponse
import com.example.sportsfantasy.Model.UpdateLeague.updateLeagueModel
import com.example.sportsfantasy.Model.UpdateLeague.updateLeagueResponse
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.ActivityEditBasicLeagueInfoBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditBasicLeagueInfoActivity : BaseActivity() {
    private lateinit var binding: ActivityEditBasicLeagueInfoBinding
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var apiInterface: ApiInterface

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var LeagueSizeAdapter: ArrayAdapter<String>
    lateinit var progressDialogue: Dialog

    var HeaderToken=""
    var User_id = ""
    var LeagueId = ""
    var UserName = ""
    var LeagueName = ""
    var LeagueSize = ""
    val gson =  Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBasicLeagueInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CustHeader()
        AllocateMemory()
        getArgumentsData()
        LeagueSize = sharedPrefManager.getLeague.league_size
        getLeagueSize()
        setEvents()

    }

    private fun setEvents() {
        binding.tvCancel.setOnClickListener {
            onBackPressed()
        }

        binding.tvSave.setOnClickListener {
            val leagueName = binding.edtLeagueName.text.toString().trim()
            val SizeLeague = binding.leagueSize.text.toString().trim()

            if (leagueName == "")
            {
                MessageForValid("An empty league name is not allowed.")
            }
            else if (SizeLeague.toInt() <= LeagueSize.toInt())
            {
                MessageForValid("New size must be bigger than current size")
            }
            else
            {
                UpdateLeague(leagueName,SizeLeague)
            }
        }
    }

    private fun UpdateLeague(lName: String, lSize: String)
    {
        progressDialoge()
        apiInterface.updateLeague("Bearer $HeaderToken", updateLeagueModel(LeagueId,lName,lSize,User_id))
            .enqueue(object :Callback<updateLeagueResponse>{
                override fun onResponse(call: Call<updateLeagueResponse>, response: Response<updateLeagueResponse>)
                {
                    val upResponse = response.body()
                    if (upResponse != null) {
                        if (upResponse.success)
                        {
                            Log.e("XXX", "onResponse: "+lSize )
                            Log.e("XXX", "onResponse: "+LeagueSize )

                            MessageForValid("League updated")

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
                        val errorResponse = Gson().fromJson(response.errorBody()!!.charStream(), LoginErrorResponse::class.java)
                        MessageForValid(""+errorResponse.message)
                    }
                }
                override fun onFailure(call: Call<updateLeagueResponse>, t: Throwable) {
                    call.cancel()
                }

            })
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
        val snackbar = TSnackbar.make(findViewById(android.R.id.content), s, TSnackbar.LENGTH_SHORT)
        // snackbar.setActionTextColor(Color.WHITE)
        val snackbarView = snackbar.view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackbarView.setBackgroundColor(getColor(R.color.bg))
        }
        snackbar.setIconLeft(R.drawable.ic_warning, 18F);
        snackbar.setIconPadding(10)
        // snackbar.setIconPadding(5)

        val textView = snackbarView.findViewById<View>(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        // textView.compoundDrawablePadding = 5
        textView.textSize = 12F
        snackbar.show()
    }

    private fun getLeagueSize()
    {
        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        apiInterface.getLeagueFields().enqueue(object : Callback<LeageFieldResponse> {
            override fun onResponse(call: Call<LeageFieldResponse>, response: Response<LeageFieldResponse>)
            {
                val leageFieldResponse = response.body()
                if (leageFieldResponse?.success == true)
                {

                    val league_size = gson.toJson(leageFieldResponse.league_size)
                    val league_weeks = gson.toJson(leageFieldResponse.league_weeks)
                    val league_fees = gson.toJson(leageFieldResponse.league_fees)

                    editor.putString("league_size", league_size)
                    editor.putString("league_weeks", league_weeks)
                    editor.putString("league_fees", league_fees)
                    editor.apply();

                    SetLeagueField()
                }
                else
                {
                    //getFields()
                }
            }
            override fun onFailure(call: Call<LeageFieldResponse>, t: Throwable)
            {
                call.cancel()
            }

        })
    }

    private fun SetLeagueField() {

        val l_size = sharedPreferences.getString("league_size", null)

        val gson = GsonBuilder().create()
        val size = gson.fromJson<ArrayList<String>>(l_size, object : TypeToken<ArrayList<String>>() {}.type)

        LeagueSizeAdapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item, size)

        binding.leagueSize.threshold = 0;//will start working from first character
        binding.leagueSize.setAdapter(LeagueSizeAdapter);//setting the adapter data into the AutoCompleteTextView

        Handler(Looper.getMainLooper()).postDelayed({

            binding.tvLeagueSize.setOnClickListener{
                binding.leagueSize.showDropDown()
            }

            binding.leagueSize.setOnClickListener {
                binding.leagueSize.showDropDown()
            }


        },200)


        binding.leagueSize.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_SPACE && event.action == KeyEvent.ACTION_UP && event.action == KeyEvent.ACTION_DOWN)
            {
                binding.leagueSize.showDropDown()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
                return@OnKeyListener true
            }
            false
        })

        binding.leagueSize.setOnTouchListener { v, event ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
            v.onTouchEvent(event)
        }


    }

    private fun getArgumentsData() {
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
        LeagueSize = sharedPrefManager.getLeague.league_size

        binding.edtLeagueName.setText(LeagueName)
        binding.leagueSize.setText(LeagueSize)

    }

    private fun AllocateMemory() {
        sharedPrefManager = SharedPrefManager.getInstance(applicationContext)
        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
    }

    private fun CustHeader() {

        val iv_menu: ImageView = findViewById(R.id.iv_menu)
        val iv_back: ImageView = findViewById(R.id.iv_back)
        val tv_name_app: TextView = findViewById(R.id.tv_name_app)

        iv_back.setOnClickListener {
            onBackPressed()
        }

        binding.tvSave.setOnClickListener {
            val intent = Intent(applicationContext, EditBasicLeagueInfoActivity::class.java)
            startActivity(intent)
        }
    }
}
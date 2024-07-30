package com.example.sportsfantasy.Activities

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
import android.view.KeyEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.androidadvance.topsnackbar.TSnackbar
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Interface.BottomSheetItemClickListener
import com.example.sportsfantasy.Model.CreateLeagueModel
import com.example.sportsfantasy.Model.CreateLeagueModelResponse
import com.example.sportsfantasy.Model.CreatedLeague
import com.example.sportsfantasy.Model.LeageFieldResponse
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.Utils.GlobalDialogHelperTwo
import com.example.sportsfantasy.databinding.ActivityCreateLeagueBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateLeagueActivity : BaseActivity(), BottomSheetItemClickListener
{
    lateinit var binding: ActivityCreateLeagueBinding
    lateinit var apiInterface: ApiInterface

    lateinit var sharedPreferences:SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    val gson =  Gson()

    lateinit var progressDialogue: Dialog

    lateinit var Leageadapter: ArrayList<String>
    lateinit var Weekadapter: ArrayList<String>
    lateinit var Feesadapter: ArrayList<String>


    lateinit var iv_menu: ImageView
    lateinit var iv_back: ImageView
    lateinit var tv_name_app: TextView
    var Postseason = "2"
    var FeesStructure = "2"
    var League = "1"
    var selectedWeek = ""
    var selectedSize = ""
    lateinit var USER_ID :String

    var isBtmWeek = false

    lateinit var clipboard: ClipboardManager
    lateinit var clip: ClipData


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateLeagueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
        clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE)
        editor = sharedPreferences.edit()

        if (SharedPrefManager.getInstance(applicationContext).ULoggedIn)
        {
            USER_ID = SharedPrefManager.getInstance(applicationContext).getLogin.userDetails.id.toString()

        }
        else if (SharedPrefManager.getInstance(applicationContext).RLoggedIn) {
            USER_ID = SharedPrefManager.getInstance(applicationContext).getRegister.userDetails.id.toString()
        } else {
            USER_ID = ""
        }


        getHeader()
        getFields()
        setFields()
        setEvents()


    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setEvents()
    {
        Postseason = "0"
        binding.postNo.isChecked = true

        binding.rdoFeesGroup.setOnCheckedChangeListener { group, checkedId ->

            // on below line we are getting radio button from our group.
            val radioButton = findViewById<RadioButton>(checkedId)

            // on below line we are displaying a toast message.
            Toast.makeText(this@CreateLeagueActivity,
                "Selected Radio Button is : " + radioButton.text,
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.rdoFeesGroup.setOnCheckedChangeListener{ group, checkedId ->
            when (checkedId) {
                R.id.fees_free -> {
                    binding.FeesCard.visibility = View.GONE
                    FeesStructure = "0"
                }
                R.id.fees_paid -> {
                    binding.FeesCard.visibility = View.VISIBLE
                    FeesStructure = "1"
                }
            }
        }

        binding.rdoLeague.setOnCheckedChangeListener{group,checkedId->

            when(checkedId)
            {
                R.id.rb_public ->
                {
                    League = "0"
                }
                R.id.rb_private ->
                {
                    League = "1"
                }
            }

        }


        binding.postseason.setOnCheckedChangeListener{ group, checkedId ->
            when (checkedId) {
                R.id.post_yes -> {
                    Postseason = "1"
                }
                R.id.post_no -> {
                    Postseason = "0"
                }
            }
        }


        /*binding.postYes.setOnCheckedChangeListener { buttonView, isChecked ->
            Postseason = "1"
            binding.postYes.isChecked = true
        }

        binding.postNo.setOnCheckedChangeListener { buttonView, isChecked ->
            Postseason = "0"
            binding.postNo.isChecked = true
        }*/

        binding.feesFree.setOnCheckedChangeListener { buttonView, isCheckedF -> false
            binding.FeesCard.visibility = View.VISIBLE
            FeesStructure = "0"
        }

        binding.feesPaid.setOnCheckedChangeListener { buttonView, isCheckedP -> false
            binding.FeesCard.visibility = View.GONE
            FeesStructure = "1"
        }


        binding.tvCreateLeague.setOnClickListener {

            if(binding.edtLeagueName.text.toString().equals(""))
            {
                binding.edtLeagueName.requestFocusFromTouch()
                binding.edtLeagueName.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake))
                MessageForValid("An empty league name is not allowed.")
            }
            else if (Postseason == "2")
            {
                binding.postNo.requestFocusFromTouch()
                binding.postNo.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake))
                MessageForValid("Select the postseason you prefer.")
            }
            else if (binding.leagueSize.text.toString().equals(""))
            {
                binding.leagueSize.requestFocusFromTouch()
                binding.tvLeagueSize.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake))
                MessageForValid("Select the league-size you prefer.")
            }
            else if (binding.chooseWeek.text.toString().equals(""))
            {
                binding.chooseWeek.requestFocusFromTouch()
                binding.tvChooseWeek.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake))
                MessageForValid("Select the week-size you prefer.")
            }

            else
            {

                progressDialoge()
                apiInterface.createLeague(
                    CreateLeagueModel("0","0", Postseason,binding.edtLeagueName.text.toString().trim(),
                        binding.leagueSize.text.toString(),League,binding.chooseWeek.text.toString(),"1",""+USER_ID)
                ).enqueue(object : Callback<CreateLeagueModelResponse>{
                    override fun onResponse(call: Call<CreateLeagueModelResponse>, response: Response<CreateLeagueModelResponse>)
                    {
                        val response = response.body()
                        if (response?.success == true)
                        {
                            MessageForValid("The league has been successfully created.")
                            progressDialogue.dismiss()

                            showShareLeagueDialog(response.created_league)
                        }
                        else
                        {
                            MessageForValid("Failed")
                            progressDialogue.dismiss()
                          //  binding.llMain.alpha = 0.0F
                        }
                    }

                    override fun onFailure(call: Call<CreateLeagueModelResponse>, t: Throwable) {
                        call.cancel()
                        progressDialogue.dismiss()
                        MessageForValid("Try Again Later")
                    }

                })
            }

        }

    }

    private fun showShareLeagueDialog(createdLeague: CreatedLeague) {
        val dialog = BottomSheetDialog(this@CreateLeagueActivity)

        val view = layoutInflater.inflate(R.layout.layout_btnsheet_league_joined, null)
        val tvCode = view.findViewById<TextView>(R.id.tv_league_join_code)
        val btnCopyCode = view.findViewById<LinearLayout>(R.id.btn_copy_code)
        val btnShareLink = view.findViewById<LinearLayout>(R.id.btn_share_league)
        val btnShareWeChat = view.findViewById<LinearLayout>(R.id.btn_share_wechat)

        btnShareWeChat.visibility = View.GONE

        tvCode.text = getString(R.string.your_code) + createdLeague.invitation_code

        btnCopyCode.setOnClickListener {
            MessageForValid("You've copied the invite link.")
            val data: String = createdLeague.invitation_link.toString()
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
            val data: String = createdLeague.invitation_code.toString()
            clip = ClipData.newPlainText("label", data)
            clipboard.setPrimaryClip(clip)

            try {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name)
                intent.putExtra(Intent.EXTRA_TEXT, "$data")
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share Via"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        btnShareWeChat.setOnClickListener {

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

        dialog.setOnDismissListener {
            onBackPressed()
        }

        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(view)

        dialog.show()
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

    private fun MessageForValid(string: String)
    {
        val snackbar = TSnackbar.make(findViewById(android.R.id.content), string, TSnackbar.LENGTH_SHORT)
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

    @SuppressLint("ClickableViewAccessibility")
    private fun setFields()
    {
        try {
            var l_size = sharedPreferences.getString("league_size", null)
            val l_week = sharedPreferences.getString("league_weeks", null)
            val l_fees = sharedPreferences.getString("league_fees", null)

            val gson = GsonBuilder().create()
            val size = gson.fromJson<ArrayList<String>>(
                l_size,
                object : TypeToken<ArrayList<String>>() {}.type
            )
            val week = gson.fromJson<ArrayList<String>>(
                l_week,
                object : TypeToken<ArrayList<String>>() {}.type
            )
            val fees = gson.fromJson<ArrayList<String>>(
                l_fees,
                object : TypeToken<ArrayList<String>>() {}.type
            )

//        Leageadapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item, size)
//        Weekadapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item, week)
//        Feesadapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item, fees)

            Leageadapter = ArrayList<String>(size)
            Weekadapter = ArrayList<String>(week)
            Feesadapter = ArrayList<String>(fees)


//        binding.leagueSize.threshold = 0;//will start working from first character
//        binding.leagueSize.setAdapter(Leageadapter);//setting the adapter data into the AutoCompleteTextView
//
//
//        binding.chooseWeek.threshold = 0;//will start working from first character
//        binding.chooseWeek.setAdapter(Weekadapter);//setting the adapter data into the AutoCompleteTextView
//
//
//        binding.SelectFee.threshold = 0;//will start working from first character
//        binding.SelectFee.setAdapter(Feesadapter);//setting the adapter data into the AutoCompleteTextView

            Handler(Looper.getMainLooper()).postDelayed({

                binding.tvLeagueSize.setOnClickListener{
                    isBtmWeek = false
                    val dialogHelper = GlobalDialogHelperTwo(this@CreateLeagueActivity, this)
                    dialogHelper.create()
                    dialogHelper.setDialogData(getString(R.string.league_size), Leageadapter, selectedSize)
//                    binding.leagueSize.showDropDown()
                }

                binding.leagueSize.setOnClickListener {
                    isBtmWeek = false
                    val dialogHelper = GlobalDialogHelperTwo(this@CreateLeagueActivity, this)
                    dialogHelper.create()
                    dialogHelper.setDialogData(getString(R.string.league_size), Leageadapter, selectedSize)
//                    binding.leagueSize.showDropDown()
                }

                binding.tvChooseWeek.setOnClickListener{
                    isBtmWeek = true
                    val dialogHelper = GlobalDialogHelperTwo(this@CreateLeagueActivity, this)
                    dialogHelper.create()
                    dialogHelper.setDialogData(getString(R.string.choose_week), Weekadapter, selectedWeek)
//                    binding.chooseWeek.showDropDown()
                }

                binding.chooseWeek.setOnClickListener {
                    isBtmWeek = true
                    val dialogHelper = GlobalDialogHelperTwo(this@CreateLeagueActivity, this)
                    dialogHelper.create()
                    dialogHelper.setDialogData(getString(R.string.choose_week), Weekadapter, selectedWeek)
//                    binding.chooseWeek.showDropDown()
                }

                binding.tvCancel.setOnClickListener {
                    finish()
                }


            },200)


            binding.leagueSize.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_SPACE && event.action == KeyEvent.ACTION_UP && event.action == KeyEvent.ACTION_DOWN)
                {
//                    binding.leagueSize.showDropDown()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
                    return@OnKeyListener true
                }
                false
            })

            binding.chooseWeek.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_SPACE && event.action == KeyEvent.ACTION_UP && event.action == KeyEvent.ACTION_DOWN)
                {
//                    binding.chooseWeek.showDropDown()
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
                    return@OnKeyListener true
                }
                false
            })

            binding.SelectFee.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_SPACE && event.action == KeyEvent.ACTION_UP && event.action == KeyEvent.ACTION_DOWN)
                {
//                    binding.SelectFee.showDropDown()
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

            binding.chooseWeek.setOnTouchListener { v, event ->
//                binding.chooseWeek.showDropDown()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
                v.onTouchEvent(event)
            }

            binding.SelectFee.setOnTouchListener { v, event ->
//                binding.SelectFee.showDropDown()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
                v.onTouchEvent(event)
            }


        }catch (er: Exception){

        }


    }

    private fun getFields()
    {
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
                }
                else
                {
                    getFields()
                }
            }
            override fun onFailure(call: Call<LeageFieldResponse>, t: Throwable)
            {
                call.cancel()
            }

        })

    }

    private fun getHeader()
    {
        iv_menu = findViewById(R.id.iv_menu)
        iv_back = findViewById(R.id.iv_back)
        tv_name_app = findViewById(R.id.tv_name_app)

        tv_name_app.text = getString(R.string.create_league)

        iv_back.setOnClickListener{
            onBackPressed()
        }
    }

    //end
    override fun onBtnSheetItemClicked(buttonText: String) {
        if(isBtmWeek){
            selectedWeek = Weekadapter.indexOf(buttonText).toString()
            binding.chooseWeek.setText(buttonText)
        }else {
            selectedSize = Leageadapter.indexOf(buttonText).toString()
            binding.leagueSize.setText(buttonText)
        }
    }
}
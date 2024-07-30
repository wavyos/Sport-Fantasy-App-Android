package com.example.sportsfantasy.Activities


import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.androidadvance.topsnackbar.TSnackbar
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.ActivityLeagueManagerToolBinding
import de.hdodenhof.circleimageview.CircleImageView

class LeagueManagerToolActivity : BaseActivity()
{
    private lateinit var binding: ActivityLeagueManagerToolBinding
    private lateinit var apiInterface: ApiInterface
    private lateinit var sharedPrefManager: SharedPrefManager
    private lateinit var clipboard: ClipboardManager
    private lateinit var clip:ClipData

    var HeaderToken=""
    var User_id = ""
    var LeagueId = ""
    var UserName = ""
    var LeagueName = ""


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityLeagueManagerToolBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CustHeader()
        AllocateMemory()
        getArgumentsData()
        setEvents()

    }

    private fun setEvents() {
        binding.tvEditBasic.setOnClickListener{
            val intent = Intent(applicationContext,EditBasicLeagueInfoActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0);
        }

        binding.tvEditTeamManager.setOnClickListener{
            val intent = Intent(applicationContext,EditTeamManagerActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0,0);
        }

        binding.tvInviteMember.setOnClickListener{
            InviteDialogebox()
        }

    }

    private fun InviteDialogebox() {
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

            val data:String = sharedPrefManager.getLeague.invitation_code.toString()
            clip = ClipData.newPlainText("label", data)
            clipboard.setPrimaryClip(clip)

            try {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_SUBJECT,R.string.app_name)
                intent.putExtra(Intent.EXTRA_TEXT, "$data")
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share Via"))
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        Invite_Link.setOnClickListener {
            myDialoge.dismiss()
            MessageForValid("You've copied the invite link.")
            val data:String = sharedPrefManager.getLeague.invitation_link.toString()
            clip = ClipData.newPlainText("label", data)
            clipboard.setPrimaryClip(clip)

            try {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_SUBJECT,R.string.app_name)
                intent.putExtra(Intent.EXTRA_TEXT, "$data")
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Share Via"))
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
        Share_WeChat.setOnClickListener {

            val launchIntent = packageManager.getLaunchIntentForPackage("com.michatapp.im")
            if (launchIntent != null)
            {
                startActivity(launchIntent)
            } else {
                MessageForValid("There is no application available in device")
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.michatapp.im")))
                } catch (e: ActivityNotFoundException) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.michatapp.im")))
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

    private fun MessageForValid(s: String)
    {
        val snackbar = TSnackbar.make(findViewById(android.R.id.content), s, TSnackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackbarView.setBackgroundColor(getColor(R.color.bg))
        }
        snackbar.setIconLeft(R.drawable.ic_warning, 18F);
        snackbar.setIconPadding(10)

        val textView = snackbarView.findViewById<View>(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 12F
        snackbar.show()
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

    }

    private fun AllocateMemory() {
        sharedPrefManager = SharedPrefManager.getInstance(applicationContext)
        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
        clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    }

    private fun CustHeader() {
        val iv_menu: ImageView = findViewById(R.id.iv_menu)
        val iv_back: ImageView = findViewById(R.id.iv_back)
        val tv_name_app: TextView = findViewById(R.id.tv_name_app)

        iv_back.setOnClickListener{
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
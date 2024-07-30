package com.example.sportsfantasy.Activities

import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.androidadvance.topsnackbar.TSnackbar
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Interface.BottomSheetItemClickListener
import com.example.sportsfantasy.Model.Login
import com.example.sportsfantasy.Model.LoginErrorResponse
import com.example.sportsfantasy.Model.LoginResponse
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.Utils.Constant
import com.example.sportsfantasy.Utils.GlobalDialogHelper
import com.example.sportsfantasy.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class LoginActivity : BaseActivity(), BottomSheetItemClickListener {
    lateinit var binding: ActivityLoginBinding
    lateinit var apiInterface: ApiInterface
    lateinit var locale: Locale
    private var currentLanguage = "zn-rCN"
    private var progressStatus = 0
    lateinit var sharedPreferences: SharedPreferences
    lateinit var myEdit: SharedPreferences.Editor
    var login_count = 0
    lateinit var progressDialogue: Dialog

    lateinit var gdb : GlobalDialogHelper
    var fcmToken = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val window: Window = window
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//  set status text dark
        window.statusBarColor = getColor(R.color.white)

        gdb = GlobalDialogHelper(this@LoginActivity, this)
        gdb.create()

        binding.ivBack.setOnClickListener {
            onBackPressed()
        }


        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        myEdit = sharedPreferences.edit()



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ForLogin()
        }

        binding.tvForgotpass.setOnClickListener {
            val i = Intent(applicationContext, ForgotPasswordActivity::class.java)
            startActivity(i)
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TestData", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            fcmToken = task.result

            // Log and toast
            Log.d("TestData", fcmToken)
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun ForLogin() {
        binding.btnContinue.setOnClickListener {
            var email = binding.edtEmail.text.toString()
            var password = binding.edtPassword.text.toString()


            if (email.equals("")) {
                binding.edtEmail.requestFocusFromTouch()
                binding.edtEmail.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                //  binding.tvError.visibility = View.VISIBLE
                //  binding.tvError.setText(getString(R.string.EmailEmpty))
                MessageForValid(getString(R.string.EmailEmpty))
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmail.requestFocusFromTouch()
                binding.edtEmail.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                //    binding.tvError.visibility = View.VISIBLE
                //   binding.tvError.setText(getString(R.string.EmailValid))
                MessageForValid(getString(R.string.EmailValid));
            } else if (password.equals("")) {
                binding.edtPassword.requestFocusFromTouch()
                MessageForValid(getString(R.string.PasswordEmpty))
                //    binding.tvError.visibility = View.VISIBLE
                //binding.tvError.setText(getString(R.string.PasswordEmpty))
                binding.edtPassword.startAnimation(
                    AnimationUtils.loadAnimation(
                        this, R.anim.shake
                    )
                );
            } else if (password.length < 8 || password.filter { it.isDigit() }
                    .firstOrNull() == null || password.filter { it.isLetter() }
                    .filter { it.isUpperCase() }
                    .firstOrNull() == null || password.filter { it.isLetter() }
                    .filter { it.isLowerCase() }
                    .firstOrNull() == null || password.filter { !it.isLetterOrDigit() }
                    .firstOrNull() == null) {
                MessageForValid(getString(R.string.PasswordWeek))
                binding.edtPassword.startAnimation(
                    AnimationUtils.loadAnimation(
                        this, R.anim.shake
                    )
                )
            } else {

                showDialoge2()

                apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
                apiInterface.userLogin(Login(email, password, fcmToken))
                    .enqueue(object : Callback<LoginResponse> {
                        override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                            binding.pbBar.visibility = View.GONE
//                            MessageForValid("" + t.message)
                            gdb.setDialogData(
                                getString(R.string.app_name),
                                t.message,
                                true,
                            )
                            progressDialogue.dismiss()
                            call.cancel()
                        }

                        override fun onResponse(
                            call: Call<LoginResponse>, response: Response<LoginResponse>
                        ) {
                            !binding.btnContinue.isClickable
                            val loginResponse = response.body()

                            if (response.isSuccessful) {
                                if (loginResponse!!.status_code == Constant.REQUEST_STATUS_CODE_SUCCESS) {
                                    /* val snack = Snackbar.make(binding.root, getString(R.string.success), Snackbar.LENGTH_INDEFINITE)
                                     snack.show()*/
                                    binding.pbBar.visibility = View.GONE
                                    val loginResponse: LoginResponse = response.body()!!
                                    SharedPrefManager.getInstance(applicationContext).Logout()
                                    myEdit.putString("access_token", loginResponse.access_token)
                                    myEdit.apply()
                                    Log.d("VRAJAN", " access " + loginResponse.access_token.toString())
                                    SharedPrefManager.getInstance(applicationContext)
                                        .SaveLogin(loginResponse)


                                    login_count = login_count + 1
                                    if (login_count == 1) {
                                        login_count++
                                        !binding.btnContinue.isFocusable
                                        binding.btnContinue.isEnabled = false

                                        binding.btnContinue.postDelayed(
                                            Runnable {
                                                val intent = Intent(
                                                    applicationContext, OnboardingActivity::class.java
                                                )
                                                intent.flags =
                                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                startActivity(intent)

                                                binding.btnContinue.isFocusable
                                                binding.btnContinue.isEnabled = true
                                            }, 500
                                        )

                                    } else {
                                        login_count = 0
                                        //  Toast.makeText(applicationContext,"Please Click One Time",Toast.LENGTH_SHORT).show()
                                    }

                                } else {
                                    binding.pbBar.visibility = View.GONE
                                    gdb.setDialogData(
                                        getString(R.string.app_name),
                                        loginResponse.message,
                                        true,
                                    )
//                                    MessageForValid("" + loginResponse.message)
                                    binding.btnContinue.isClickable
                                }


                            } else if (response.code() == Constant.REQUEST_STATUS_CODE_UNPROCESSABLE) {
                                progressDialogue.dismiss()
                                binding.pbBar.visibility = View.GONE
                                Log.d("krunal", " else if response " + response.body())
                                val errorResponse = Gson().fromJson(
                                    response.errorBody()!!.charStream(),
                                    LoginErrorResponse::class.java
                                )
//                                MessageForValid("" + errorResponse.message.toString())
                                gdb.setDialogData(
                                    getString(R.string.app_name),
                                    errorResponse.message,
                                    true,
                                )

                            } else {
                                progressDialogue.dismiss()
                                binding.pbBar.visibility = View.GONE
//                                MessageForValid("" + response.message())
                                gdb.setDialogData(
                                    getString(R.string.app_name),
                                    response.message(),
                                    true,
                                )
                                binding.btnContinue.isClickable
                            }
                        }
                    })
            }
        }
    }

    private fun showDialoge2() {
        val progressDialogeBind = layoutInflater.inflate(R.layout.custom_dialogebox, null)
        progressDialogue = Dialog(binding.root.context)
        progressDialogue.setContentView(progressDialogeBind)

        val civ_progress = progressDialogeBind.findViewById<CircleImageView>(R.id.civ_progress)
        // civ_progress.animate().rotation(360f).setDuration(1000).start()

        val rotate = RotateAnimation(
            0F, 360F, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )

        rotate.duration = 900
        rotate.repeatCount = Animation.INFINITE
        civ_progress.startAnimation(rotate)



        progressDialogue.setCancelable(false)
        progressDialogue.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialogue.show()
    }

    private fun MessageForValid(s: String) {
        val snackbar = TSnackbar.make(findViewById(android.R.id.content), s, TSnackbar.LENGTH_SHORT)
        // snackbar.setActionTextColor(Color.WHITE)
        val snackbarView = snackbar.view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackbarView.setBackgroundColor(getColor(R.color.bg))
        }
        snackbar.setIconLeft(R.drawable.ic_warning, 18F);
        snackbar.setIconPadding(10)
        // snackbar.setIconPadding(5)

        val textView =
            snackbarView.findViewById<View>(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        // textView.compoundDrawablePadding = 5
        textView.textSize = 12F
        snackbar.show()

    }

    override fun onBackPressed() {
        val back = intent.getStringExtra("back")

        if (back == "forgot") {
            val choice = Intent(applicationContext, ChoiceActivity::class.java)
            startActivity(choice)
        } else {
            super.onBackPressed()
        }

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onBtnSheetItemClicked(buttonText: String) {
        gdb.dismiss()
    }

}



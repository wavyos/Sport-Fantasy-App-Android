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
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.androidadvance.topsnackbar.TSnackbar
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Interface.BottomSheetItemClickListener
import com.example.sportsfantasy.Model.ErrorResponse
import com.example.sportsfantasy.Model.Register
import com.example.sportsfantasy.Model.RegisterResponse
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.Utils.Constant
import com.example.sportsfantasy.Utils.GlobalDialogHelper
import com.example.sportsfantasy.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : BaseActivity(), BottomSheetItemClickListener {
    lateinit var binding: ActivityRegisterBinding
    var terms = false
    var privacy = false
    lateinit var sharedPreferences: SharedPreferences
    lateinit var myEdit: SharedPreferences.Editor
    lateinit var apiInterface: ApiInterface
    lateinit var myDialoge2: Dialog

    lateinit var gdb : GlobalDialogHelper
    var fcmToken = ""

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            //     window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//  set status text dark
            window.statusBarColor = getColor(R.color.white)
        }

        gdb = GlobalDialogHelper(this@RegisterActivity, this)
        gdb.create()
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        myEdit = sharedPreferences.edit()

        Log.d("krunal", "register one");

        ContinueRegister()
        //  Login()


        binding.ivBack.setOnClickListener {
            val i = Intent(applicationContext, ChoiceActivity::class.java)
            startActivity(i)
        }


        binding.tvLogin.setOnClickListener {
            Login()
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



    private fun Login() {
        val i = Intent(applicationContext, LoginActivity::class.java)
        startActivity(i)
    }

    private fun ContinueRegister() {
        binding.btnContinue.setOnClickListener {
            val fname = binding.edtFName.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            if (fname.equals("")) {
                binding.edtFName.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                binding.edtFName.requestFocusFromTouch()
                binding.edtFName.setHint(getString(R.string.fullname))
                MessageForValid(getString(R.string.f_name))

            }
            else if (email.equals("")) {
                binding.edtEmail.requestFocusFromTouch()
                binding.edtEmail.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                MessageForValid(getString(R.string.EmailEmpty))
                binding.edtEmail.setHint(getString(R.string.email))
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmail.requestFocusFromTouch()
                binding.edtEmail.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
                MessageForValid(getString(R.string.EmailValid));
            } else if (password.equals("")) {
                binding.edtPassword.requestFocusFromTouch()
                binding.edtPassword.startAnimation(
                    AnimationUtils.loadAnimation(
                        this,
                        R.anim.shake
                    )
                )
                MessageForValid(getString(R.string.PasswordEmpty))
                binding.edtPassword.setHint(getString(R.string.password))
            } else if (password.length < 8 || password.filter { it.isDigit() }
                    .firstOrNull() == null || password.filter { it.isLetter() }
                    .filter { it.isUpperCase() }
                    .firstOrNull() == null || password.filter { it.isLetter() }
                    .filter { it.isLowerCase() }
                    .firstOrNull() == null || password.filter { !it.isLetterOrDigit() }
                    .firstOrNull() == null) {
                binding.edtPassword.requestFocusFromTouch()
                MessageForValid(getString(R.string.PasswordWeek))
                binding.edtPassword.startAnimation(
                    AnimationUtils.loadAnimation(
                        this,
                        R.anim.shake
                    )
                )
            } else if (!binding.chbTermsConditions.isChecked) {
                binding.chbTermsConditions.startAnimation(
                    AnimationUtils.loadAnimation(
                        this, R.anim.shake
                    )
                )
                binding.chbTermsConditions.requestFocusFromTouch()
                MessageForValid(getString(R.string.terms))
            } else if (!binding.chbPrivacy.isChecked) {
                binding.chbPrivacy.startAnimation(
                    AnimationUtils.loadAnimation(
                        this, R.anim.shake
                    )
                );
                binding.chbPrivacy.requestFocusFromTouch()
                MessageForValid(getString(R.string.privacy))
            } else {
                !binding.btnContinue.isClickable

                //binding.pbBar.visibility = View.VISIBLE
                showDialoge2()

                apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)

//                apiInterface.UserLogin(Login(mobile,pass)).enqueue(object: Callback<LoginResponse>

                apiInterface.userRegister(Register(email, fname, password, fcmToken))
                    .enqueue(object : Callback<RegisterResponse> {
                        override fun onResponse(
                            call: Call<RegisterResponse>, response: Response<RegisterResponse>
                        ) {
                            Log.d("kru", "" + response.isSuccessful)
                            if (response.isSuccessful) {

                                val registerResponse: RegisterResponse = response.body()!!

                                if (registerResponse.status_code.equals(Constant.REQUEST_STATUS_CODE_SUCCESS)) {
                                    binding.btnContinue.isClickable
                                    binding.pbBar.visibility = View.GONE
                                    val snack = Snackbar.make(
                                        binding.root,
                                        getString(R.string.success),
                                        Snackbar.LENGTH_INDEFINITE
                                    )
                                    snack.show()

                                    SharedPrefManager.getInstance(applicationContext).Logout()
                                    myEdit.putString("access_token", registerResponse.access_token)
                                    myEdit.commit()
                                    SharedPrefManager.getInstance(applicationContext)
                                        .SaveRegister(registerResponse)

                                    val i = Intent(applicationContext, OnboardingActivity::class.java)
                                    i.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(i)
                                } else {
                                    !binding.btnContinue.isClickable
                                    binding.pbBar.visibility = View.GONE
                                    gdb.setDialogData(
                                        getString(R.string.app_name),
                                        response.message(),
                                        true,
                                    )
//                                    MessageForValid("" + response.message())
                                    myDialoge2.dismiss()
                                }


                            } else {
                                val errorResponse = Gson().fromJson(
                                    response.errorBody()!!.charStream(), ErrorResponse::class.java
                                )
                                //  MessageForValid(""+ errorResponse.message.toString())

                                binding.pbBar.visibility = View.GONE
                                gdb.setDialogData(
                                    getString(R.string.app_name),
                                    errorResponse.message,
                                    true,
                                )

//                                MessageForValid("" + errorResponse.message.toString())
                                !binding.btnContinue.isClickable
                                myDialoge2.dismiss()
                            }

                        }

                        override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                            binding.pbBar.visibility = View.GONE
                            gdb.setDialogData(
                                getString(R.string.app_name),
                                t.message,
                                true,
                            )
//                            MessageForValid("" + t.message)
                            myDialoge2.dismiss()
                            call.cancel()
                        }

                    })


            }

        }
    }

    private fun showDialoge2() {
        val dialogebind2 = layoutInflater.inflate(R.layout.custom_dialogebox, null)
        myDialoge2 = Dialog(this)
        myDialoge2.setContentView(dialogebind2)

        val civ_progress = dialogebind2.findViewById<CircleImageView>(R.id.civ_progress)
        civ_progress.animate().rotation(360f).setDuration(5000).start()

        myDialoge2.setCancelable(false)
        myDialoge2.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        myDialoge2.show()
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
        super.onBackPressed()
    }

    override fun onBtnSheetItemClicked(buttonText: String) {
        gdb.dismiss()
    }


}
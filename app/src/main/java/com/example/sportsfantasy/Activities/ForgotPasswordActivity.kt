package com.example.sportsfantasy.Activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.androidadvance.topsnackbar.TSnackbar
import com.example.sportsfantasy.Model.ForgotPasswordModel
import com.example.sportsfantasy.Model.ForgotPasswordResponse
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.ActivityForgotPasswordBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPasswordActivity : BaseActivity()
{
    lateinit var binding:ActivityForgotPasswordBinding

    lateinit var apiInterface: ApiInterface
    lateinit var email:String
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
        val window: Window = window
        //     window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;//  set status text dark
        window.statusBarColor = getColor(R.color.white)

        binding.ivBack.setOnClickListener{
            onBackPressed()
        }


        binding.btnForgot.setOnClickListener{
            email = binding.edtEmail.text.toString()
            if (email == "")
            {
                binding.edtEmail.requestFocusFromTouch()
                binding.edtEmail.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));
                MessageForValid(getString(R.string.EmailEmpty))
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                binding.edtEmail.requestFocusFromTouch()
                binding.edtEmail.startAnimation(AnimationUtils.loadAnimation(this,R.anim.shake));

                MessageForValid(getString(R.string.EmailValid));
            }
            else
            {
                ForgotPassword_API()
            }
        }

    }

    private fun ForgotPassword_API()
    {
        binding.pbBar.visibility = View.VISIBLE
        apiInterface.sendForgotPassword(ForgotPasswordModel(email)).enqueue(object : Callback<ForgotPasswordResponse>{
            override fun onResponse(call: Call<ForgotPasswordResponse>, response: Response<ForgotPasswordResponse>) {
                if (response.isSuccessful)
                {
                    binding.pbBar.visibility = View.GONE
                    val forgotres = response.body()
                    if (forgotres?.status_code == 200)
                    {
                        MessageForValid(""+forgotres.message.toString())
                        Handler(Looper.getMainLooper()).postDelayed(Runnable
                        {
                            val intent = Intent(applicationContext,LoginActivity::class.java)
                            intent.putExtra("back","forgot")
                            startActivity(intent)
                            finish()
                        }, 2000)

                    }
                    else
                    {
                        MessageForValid(""+ forgotres?.message.toString())
                    }
                }
                else
                {
                    binding.pbBar.visibility = View.GONE
                    MessageForValid(""+response.message().toString())
                }
            }
            override fun onFailure(call: Call<ForgotPasswordResponse>, t: Throwable)
            {
                binding.pbBar.visibility = View.GONE
                call.cancel()
            }

        })

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

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
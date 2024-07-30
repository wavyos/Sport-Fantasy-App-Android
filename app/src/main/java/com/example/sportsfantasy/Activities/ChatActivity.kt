package com.example.sportsfantasy.Activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View.OnFocusChangeListener
import android.view.View.OnTouchListener
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsfantasy.Adapter.ChatAdapter
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Interface.ListItemClick
import com.example.sportsfantasy.Model.ChatResModel
import com.example.sportsfantasy.Model.GetMessageAddModel
import com.example.sportsfantasy.Model.SendMessageAddModel
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.ActivityChatBinding
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChatActivity : BaseActivity() {

    lateinit var binding: ActivityChatBinding

    var HeaderToken = ""
    var leagueId = ""
    var title = ""
    var senderId = ""
    var receiverId = ""
    var channelId = ""
    var userId = ""
    var UserName = ""
    var listSize = 0

    lateinit var sharedPrefManager: SharedPrefManager
    lateinit var apiInterface: ApiInterface
    lateinit var progressDialogue: Dialog

    private var chatList: List<ChatResModel.Message> = java.util.ArrayList()
    lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPrefManager = SharedPrefManager.getInstance(this)
        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)

        binding.ivBack.setOnClickListener {
            finish()
        }
        senderId = intent.getStringExtra("senderId")!!
        receiverId = intent.getStringExtra("receiver_id")!!
        channelId = intent.getStringExtra("channel_id")!!
        leagueId = intent.getStringExtra("leagueId")!!
        title = intent.getStringExtra("title")!!

        Log.d("ChatActivity", ""+title)
        binding.tvNameApp.text = title

        when {
            sharedPrefManager.ULoggedIn -> {
                HeaderToken = sharedPrefManager.getLogin.access_token
                userId = sharedPrefManager.getLogin.userDetails.id.toString()
                UserName = sharedPrefManager.getLogin.userDetails.name.toString()
//                binding.tvNameApp.setText(UserName)
            }
            sharedPrefManager.RLoggedIn -> {
                HeaderToken = sharedPrefManager.getRegister.access_token
                userId = sharedPrefManager.getRegister.userDetails.id.toString()
                UserName = sharedPrefManager.getRegister.userDetails.name.toString()
//                binding.tvNameApp.setText(UserName)
            }
            else -> {
                HeaderToken = ""
                userId = ""
                UserName = ""
//                binding.tvNameApp.setText(UserName)
            }
        }


        binding.imgSend.setOnClickListener {
            sendMessage(binding.edtMessage.text.toString())
        }

        binding.rvChat.layoutManager = LinearLayoutManager(
            this@ChatActivity,
            LinearLayoutManager.VERTICAL,
            true
        )
        chatAdapter = ChatAdapter(this@ChatActivity, userId,
            chatList as ArrayList<ChatResModel.Message>,
            object : ListItemClick {
                override fun onItemClick(position: Int, data: Any?) {
                }
            })
        binding.rvChat.adapter = chatAdapter

        getChatList("First")



        binding.edtMessage.onFocusChangeListener = OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                binding.rvChat.smoothScrollToPosition(0)
            }
        }
    }

    private fun sendMessage(message: String) {
        binding.edtMessage.text.clear()
        apiInterface.sendMessage(
            "Bearer $HeaderToken",
            SendMessageAddModel(
                channelId.toInt(),
                leagueId.toInt(),
                senderId.toInt(),
                receiverId.toInt(),
                message
            )
        ).enqueue(object :
            Callback<ResponseBody> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressDialogue.dismiss()

                binding.edtMessage.clearFocus()
                getChatList("")

            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialogue.dismiss()
                call.cancel()
            }
        })
    }

    private fun getChatList(s: String) {
        if (s == "First") {
            progressDialoge()
        }

        apiInterface.getMessageList(
            "Bearer $HeaderToken",
            GetMessageAddModel(
                channelId.toInt(),
                leagueId.toInt(),
                senderId.toInt(),
                receiverId.toInt()
            )
        ).enqueue(object :
            Callback<ResponseBody> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressDialogue.dismiss()

                try {

                    val standingResponse =
                        Gson().fromJson(response.body()!!.string(), ChatResModel::class.java)
                    if (standingResponse !=null) {
                        if (standingResponse.success!!) {
                            if (standingResponse.messages!!.isNotEmpty()) {

                                chatList =
                                    standingResponse.messages as ArrayList<ChatResModel.Message>

                                chatList = chatList.reversed()
                                chatAdapter.addMessage(chatList)
                                chatAdapter.notifyDataSetChanged()

                                Handler(Looper.getMainLooper()).postDelayed(
                                    Runnable
                                    {
                                        getChatList("not")
                                    }, 3000)
                            }
                        }

                    }
                } catch (e: Exception) {

                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialogue.dismiss()
                call.cancel()
            }
        })
    }

    private fun progressDialoge() {
        val progressDialogeBind = layoutInflater.inflate(R.layout.custom_dialogebox, null)
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
}
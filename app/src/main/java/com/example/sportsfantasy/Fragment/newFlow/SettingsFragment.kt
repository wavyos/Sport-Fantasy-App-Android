package com.example.sportsfantasy.Fragment.newFlow

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidadvance.topsnackbar.TSnackbar
import com.example.sportsfantasy.Adapter.MaxPlayerListAdapter
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Model.UserLeague
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.FragmentSettingsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var maxPlayerAdapter: MaxPlayerListAdapter

    private lateinit var createdLeague: UserLeague
    lateinit var sharedPrefManager: SharedPrefManager

    lateinit var clipboard: ClipboardManager
    lateinit var clip: ClipData

    private val temList = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)

        AllocateMemory()

        return binding.root
    }

    private fun AllocateMemory() {
        sharedPrefManager = SharedPrefManager.getInstance(requireContext())
        createdLeague = sharedPrefManager.getLeague
        clipboard =
            requireActivity().getSystemService(AppCompatActivity.CLIPBOARD_SERVICE) as ClipboardManager
        setUpRecyclerView()
        setUpEvents()

        binding.tvChangeLeagueName.text = createdLeague.league_name

        binding.ivBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setUpEvents() {
        binding.tvInviteFriends.setOnClickListener {
            val dialog = BottomSheetDialog(requireActivity())

            val view = layoutInflater.inflate(R.layout.layout_btnsheet_league_joined, null)
            val tvCode = view.findViewById<TextView>(R.id.tv_league_join_code)
            val btnCopyCode = view.findViewById<LinearLayout>(R.id.btn_copy_code)
            val btnShareLink = view.findViewById<LinearLayout>(R.id.btn_share_league)
            val btnShareWeChat = view.findViewById<LinearLayout>(R.id.btn_share_wechat)

            btnShareWeChat.visibility = View.VISIBLE
            tvCode.text = requireContext().getString(R.string.your_code)+createdLeague.invitation_code

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
                    //startActivity(intent);
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            btnShareWeChat.setOnClickListener {

                val launchIntent =
                    requireActivity().packageManager.getLaunchIntentForPackage("com.michatapp.im")
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

            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(view)

            dialog.show()
        }


        binding.tvDeleteLeague.setOnClickListener {
            val dialog = BottomSheetDialog(requireActivity())

            val view = layoutInflater.inflate(R.layout.layout_btnsheet_delete_league, null)
            val btnDeleteLeague = view.findViewById<LinearLayout>(R.id.btn_delete_league)
            val btnDeleteCancel = view.findViewById<LinearLayout>(R.id.btn_delete_cancel)


            btnDeleteLeague.setOnClickListener {
                dialog.dismiss()
            }

            btnDeleteCancel.setOnClickListener {
                dialog.dismiss()
            }

            dialog.setCancelable(true)
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(view)

            dialog.show()
        }
    }

    private fun setUpRecyclerView() {

        temList.add("Jack1 Jack1")
        temList.add("John John")
        temList.add("Billie Bucher")
        temList.add("Jam Jam")

        binding.rvMaxPlayerList.layoutManager = LinearLayoutManager(requireActivity())
        maxPlayerAdapter = MaxPlayerListAdapter(this, temList)
        binding.rvMaxPlayerList.adapter = maxPlayerAdapter
    }

    private fun MessageForValid(s: String) {
        val snackbar = TSnackbar.make(
            requireActivity().findViewById(android.R.id.content),
            s,
            TSnackbar.LENGTH_SHORT
        )
        // snackbar.setActionTextColor(Color.WHITE)
        val snackbarView = snackbar.view
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            snackbarView.setBackgroundColor(requireActivity().getColor(R.color.bg))
        }
        snackbar.setIconLeft(R.drawable.ic_warning, 18F)
        snackbar.setIconPadding(10)
        // snackbar.setIconPadding(5)

        val textView = snackbarView.findViewById<View>(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        // textView.compoundDrawablePadding = 5
        textView.textSize = 12F
        snackbar.show()
    }

    fun showPlayerDialog(item: String) {
        val dialog = BottomSheetDialog(requireActivity())

        val view = layoutInflater.inflate(R.layout.layout_btmsheet_promote_kick_view_item, null)
        val tvSelectedPlayer = view.findViewById<TextView>(R.id.tv_selected_max_player_name)
        val tvPopUpMsg = view.findViewById<TextView>(R.id.tv_max_popup_msg)
        val tvKickText = view.findViewById<TextView>(R.id.tv_kick_text)
        val tvBtnCancel = view.findViewById<TextView>(R.id.btn_sheet_cancel)
        val imgKickPromote = view.findViewById<ImageView>(R.id.img_kick_promote)
        val btnPromotManager = view.findViewById<LinearLayout>(R.id.btn_promote_manager)
        val btnViewProfile = view.findViewById<LinearLayout>(R.id.btn_view_profile)
        val btnKickFromLeague = view.findViewById<LinearLayout>(R.id.btn_kick_from_league)

        tvSelectedPlayer.text = item
        tvBtnCancel.visibility = View.GONE

        btnPromotManager.setOnClickListener {
            btnPromotManager.visibility = View.GONE
            btnViewProfile.visibility = View.GONE
            tvBtnCancel.visibility = View.VISIBLE
            tvPopUpMsg.visibility = View.VISIBLE

            tvPopUpMsg.text = "Are you sure you want to promote $item? He will be able to manage the team."
            tvSelectedPlayer.text = "Promote $item"

            tvKickText.text = "Promote $item"
            imgKickPromote.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.promote_arrow))
        }

        btnKickFromLeague.setOnClickListener {
            btnPromotManager.visibility = View.GONE
            btnViewProfile.visibility = View.GONE
            tvBtnCancel.visibility = View.VISIBLE
            tvPopUpMsg.visibility = View.VISIBLE

            tvPopUpMsg.text = getString(R.string.str_kick_user_msg)
            tvSelectedPlayer.text = getString(R.string.str_kick)+ item

            tvKickText.text = getString(R.string.str_kick)+ item
            imgKickPromote.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable._ic_close_))
        }

        btnViewProfile.setOnClickListener {
            dialog.dismiss()
        }

        tvBtnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setCancelable(true)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setContentView(view)

        dialog.show()
    }
}
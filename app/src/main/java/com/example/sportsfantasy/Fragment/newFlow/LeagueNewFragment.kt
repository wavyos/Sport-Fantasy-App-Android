package com.example.sportsfantasy.Fragment.newFlow

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidadvance.topsnackbar.TSnackbar
import com.bumptech.glide.Glide
import com.example.sportsfantasy.Activities.BottomNavigationActivity
import com.example.sportsfantasy.Adapter.PositionListAdapter
import com.example.sportsfantasy.DataStorage.SharedPrefManager
import com.example.sportsfantasy.Model.AutoDraftResponse
import com.example.sportsfantasy.Model.Draft_Remove_Response
import com.example.sportsfantasy.Model.LoginErrorResponse
import com.example.sportsfantasy.Model.Position
import com.example.sportsfantasy.Model.PositionListResponse
import com.example.sportsfantasy.Model.UserLeagueResponse
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.FragmentLeagueBinding
import com.example.sportsfantasy.databinding.FragmentLeagueNewBinding
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class LeagueNewFragment : Fragment() {
    private lateinit var binding: FragmentLeagueNewBinding

    var LeagueName: String = ""
    lateinit var apiInterface: ApiInterface
    lateinit var sharedPrefManager: SharedPrefManager
    lateinit var HeaderToken: String
    lateinit var login_user_id: String
    lateinit var league_id: String
    lateinit var positionListAdapter: PositionListAdapter
    lateinit var positionList: ArrayList<Position>

    var itemC : Position? = null
    var itemPG : Position? = null
    var itemSG : Position? = null
    var itemSF : Position? = null
    var itemPF : Position? = null

    var totalSmall = 0
    var totalStarter = 0
    var totalStar = 0

    var totalSalary = 100
    var totalUsedSalary = 0

    var Draft_id = ""
    lateinit var myDialoge2: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLeagueNewBinding.inflate(layoutInflater)

        AllocateMemory()
        GetData()
        showDialoge2()
        HeaderEvents()
        setEvents()

        return binding.root
    }

    private fun MessageForValid(s: String)
    {
        try {
            val snackbar = TSnackbar.make(binding.root.findViewById(android.R.id.content), s, TSnackbar.LENGTH_SHORT)
            // snackbar.setActionTextColor(Color.WHITE)
            val snackbarView = snackbar.view
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                snackbarView.setBackgroundColor(ContextCompat.getColor(requireActivity().applicationContext,R.color.bg))
            }
            snackbar.setIconLeft(R.drawable.ic_warning, 18F)
            snackbar.setIconPadding(10)
            // snackbar.setIconPadding(5)

            val textView = snackbarView.findViewById<View>(com.androidadvance.topsnackbar.R.id.snackbar_text) as TextView
            textView.setTextColor(Color.WHITE)
            // textView.compoundDrawablePadding = 5
            textView.textSize = 12F
            snackbar.show()
        }catch (er:Exception){

        }
    }

    private fun autoDrafting() {
        showDialoge2()
        apiInterface.autoDraft("Bearer $HeaderToken",login_user_id,league_id).enqueue(object :
            Callback<AutoDraftResponse> {
            override fun onResponse(call: Call<AutoDraftResponse>, response: Response<AutoDraftResponse>, )
            {
                if (response.isSuccessful)
                {
                    if (response.body()!!.success)
                    {
                        MessageForValid("Player has been assign successfully.")
                        myDialoge2.dismiss()
                        setEvents()
                    }
                    else
                    {
                        myDialoge2.dismiss()
                        MessageForValid(""+ response.message())
                    }
                }
                else
                {
                    myDialoge2.dismiss()
                    val errorResponse = Gson().fromJson(response.errorBody()!!.charStream(), LoginErrorResponse::class.java)
                    MessageForValid(""+ errorResponse.message.toString())
                }
            }
            override fun onFailure(call: Call<AutoDraftResponse>, t: Throwable) {
                call.cancel()
            }

        })
    }

    private fun setEvents() {
        try {
            myDialoge2.show()
            apiInterface.positionList("Bearer $HeaderToken",login_user_id,league_id).enqueue(object :
                Callback<PositionListResponse>
            {
                override fun onResponse(call: Call<PositionListResponse>, response: Response<PositionListResponse>)
                {
                    if (response.isSuccessful)
                    {
                        if (response.body()?.success == true)
                        {
                            /*if (response.body()!!.position_list[0].salary==0) {
                                binding.tvUsedSalary.text = ""
                            } else {
                                binding.tvUsedSalary.text = response.body()!!.position_list[0].salary.toString()
                            }*/

                            positionList.clear()
                            positionList.addAll(response.body()!!.position_list)

                            totalSmall = 0
                            totalStarter = 0
                            totalStar = 0

                            for (position in positionList){

                                if (position.player.isNotEmpty()) {
                                    val salary = position.player[0].salary
                                    Log.d("krunal"," salary "+salary)

                                    if (salary!=null)
                                    {
                                        var s =  salary.toInt()
                                        totalUsedSalary += s

                                        if (s <= 15) {
                                            totalSmall = totalSmall + 1
                                        } else if (s < 20) {
                                            totalStarter = totalStarter + 1
                                        } else {
                                            totalStar = totalStar + 1
                                        }
                                    }

                                    val codename:String = ""+position.code
                                    try {
                                        when(codename){
                                            "PG"->{
                                                binding.tvPgSalary.isVisible = true
                                                binding.tvPgPpg.isVisible = true
                                                itemPG = position
                                                binding.tvPlayernamePG.text = position.player[0].fullname
                                                Glide.with(requireContext())
                                                    .load(position.imagepath+position.player[0].headshot)
                                                    .placeholder(ContextCompat.getDrawable(requireContext(), R.drawable.player_placeholder_img))
                                                    .into(binding.imgPG)
                                                binding.tvAddEditPG.text = "Edit"
                                                binding.tvPgSalary.text = position.player[0].salary+"$"
                                                if(position.points is String){
                                                    binding.tvPgPpg.text = "${String.format("%.2f", position.points.toDouble())}ppg"
                                                }
                                            }
                                            "C"->{
                                                binding.tvCSalary.isVisible = true
                                                binding.tvCPpg.isVisible = true
                                                itemC = position
                                                binding.tvPlayernameC.text = position.player[0].fullname
                                                Glide.with(requireContext())
                                                    .load(position.imagepath+position.player[0].headshot)
                                                    .placeholder(ContextCompat.getDrawable(requireContext(), R.drawable.player_placeholder_img))
                                                    .into(binding.imgC)
                                                binding.tvAddEditC.text = "Edit"
                                                binding.tvCSalary.text = position.player[0].salary+"$"
                                                if(position.points is String){
                                                    binding.tvCPpg.text = "${String.format("%.2f", position.points.toDouble())}ppg"
                                                }
                                            }
                                            "PF"->{
                                                binding.tvPfSalary.isVisible = true
                                                binding.tvPfPpg.isVisible = true
                                                itemPF = position
                                                binding.tvPlayernamePF.text = position.player[0].fullname
                                                Glide.with(requireContext())
                                                    .load(position.imagepath+position.player[0].headshot)
                                                    .placeholder(ContextCompat.getDrawable(requireContext(), R.drawable.player_placeholder_img))
                                                    .into(binding.imgPF)
                                                binding.tvAddEditPF.text = "Edit"
                                                binding.tvPfSalary.text = position.player[0].salary+"$"
                                                if(position.points is String){
                                                    binding.tvPfPpg.text = "${String.format("%.2f", position.points.toDouble())}ppg"
                                                }
                                            }
                                            "SF"->{
                                                binding.tvSfSalary.isVisible = true
                                                binding.tvSfPpg.isVisible = true
                                                itemSF = position
                                                binding.tvPlayernameSF.text = position.player[0].fullname
                                                Glide.with(requireContext())
                                                    .load(position.imagepath+position.player[0].headshot)
                                                    .placeholder(ContextCompat.getDrawable(requireContext(), R.drawable.player_placeholder_img))
                                                    .into(binding.imgSF)
                                                binding.tvAddEditSF.text = "Edit"
                                                binding.tvSfSalary.text = position.player[0].salary+"$"
                                                if(position.points is String){
                                                    binding.tvSfPpg.text = "${String.format("%.2f", position.points.toDouble())}ppg"
                                                }
                                            }
                                            "SG"->{
                                                binding.tvSgSalary.isVisible = true
                                                binding.tvSgPpg.isVisible = true
                                                itemSG = position
                                                binding.tvPlayernameSG.text = position.player[0].fullname
                                                Glide.with(requireContext())
                                                    .load(position.imagepath+position.player[0].headshot)
                                                    .placeholder(ContextCompat.getDrawable(requireContext(), R.drawable.player_placeholder_img))
                                                    .into(binding.imgSG)
                                                binding.tvAddEditSG.text = "Edit"
                                                binding.tvSgSalary.text = position.player[0].salary+"$"
                                                if(position.points is String){
                                                    binding.tvSgPpg.text = "${String.format("%.2f", position.points.toDouble())}ppg"
                                                }
                                            }
                                            else->{
                                            }
                                        }
                                    }catch (_: Exception){

                                    }

                                } else {
                                    val codename:String = ""+position.code

                                    when(codename){
                                        "C"->{
                                            binding.tvCSalary.isVisible = false
                                            binding.tvCPpg.isVisible = false
                                            binding.tvAddEditC.text = requireContext().getString(R.string.add)
                                        }
                                        "SG"->{
                                            binding.tvSgSalary.isVisible = false
                                            binding.tvSgPpg.isVisible = false
                                            binding.tvAddEditSG.text = requireContext().getString(R.string.add)
                                        }
                                        "SF"->{
                                            binding.tvSfSalary.isVisible = false
                                            binding.tvSfPpg.isVisible = false
                                            binding.tvAddEditSF.text = requireContext().getString(R.string.add)
                                        }
                                        "PG"->{
                                            binding.tvPgSalary.isVisible = false
                                            binding.tvPgPpg.isVisible = false
                                            binding.tvAddEditPG.text = requireContext().getString(R.string.add)
                                        }
                                        "PF"->{
                                            binding.tvPfSalary.isVisible = false
                                            binding.tvPfPpg.isVisible = false
                                            binding.tvAddEditPF.text = requireContext().getString(R.string.add)
                                        }
                                        else->{

                                        }
                                    }
                                }

                            }


                            //here , put position wise data in respective views.
                            binding.tvUsedSalary.text = "/ $totalUsedSalary"
                            myDialoge2.dismiss()

                        }
                        else
                        {
                            myDialoge2.dismiss()
//                        binding.shimmer.visibility = View.GONE
                        }
                    }
                    else
                    {
                        myDialoge2.dismiss()
//                    binding.shimmer.visibility = View.GONE
                    }
                }
                override fun onFailure(call: Call<PositionListResponse>, t: Throwable)
                {
                    call.cancel()
                    myDialoge2.dismiss()
                }
            })



        binding.btnSubmitRoster.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.btnAutoDraft.setOnClickListener {
            totalUsedSalary = 0
            autoDrafting()
        }
        binding.cardAddEditC.setOnClickListener {
            onPositionClick(positionList.filter { item -> item.code == "C" }[0])
        }
        binding.cardAddEditPF.setOnClickListener {
            onPositionClick(positionList.filter { item -> item.code == "PF" }[0])
        }
        binding.cardAddEditPG.setOnClickListener {
            onPositionClick(positionList.filter { item -> item.code == "PG" }[0])
        }
        binding.cardAddEditSF.setOnClickListener {
            onPositionClick(positionList.filter { item -> item.code == "SF" }[0])
        }
        binding.cardAddEditSG.setOnClickListener {
            onPositionClick(positionList.filter { item -> item.code == "SG" }[0])
        }
        }catch (er: Exception){
            Log.d("setEvents: ", ""+er.message.toString())
        }
    }

    fun String.capitalizeWords(): String = split(" ").map { it.capitalize() }.joinToString(" ")


    private fun HeaderEvents() {
        val iv_menu: ImageView = binding.root.findViewById(R.id.iv_menu)
        val iv_back: ImageView = binding.root.findViewById(R.id.iv_back)
        val tv_name_app: TextView = binding.root.findViewById(R.id.tv_name_app)

        val sharedPreference = requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        val leagueNameMain = sharedPreference.getString("league_name", "")
        tv_name_app.text = leagueNameMain?.capitalizeWords()

        iv_menu.visibility = View.GONE

        iv_back.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun GetData() {
        val args = arguments
        if (args != null) {
            LeagueName = args.getString("league_name").toString()
            league_id = args.getString("league_id").toString()
        }
//        LeagueName = intent.getStringExtra("league_name").toString();
//        league_id = intent.getStringExtra("league_id").toString();

        val sharedPreference =
            requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        // var editor = sharedPreference.edit()
        val LeagueNameMain = sharedPreference.getString("league_name", "")

        Log.d("krunal", " leaguename " + LeagueName)
        Log.d("krunal", " league_id " + league_id)

        Log.d("krunal", " legaue code receve " + LeagueName)

//        binding.tvLeagueName.text = LeagueNameMain?.capitalizeWords()

        if (sharedPrefManager.ULoggedIn) {
            HeaderToken = sharedPrefManager.getLogin.access_token
            login_user_id = sharedPrefManager.getLogin.userDetails.id.toString()
        } else if (sharedPrefManager.RLoggedIn) {
            HeaderToken = sharedPrefManager.getRegister.access_token
            login_user_id = sharedPrefManager.getRegister.userDetails.id.toString()
        } else {
            HeaderToken = ""
            login_user_id = ""
        }


        getApiCalling()
    }

    private fun getApiCalling() {

        apiInterface.getLeagueList(login_user_id).enqueue(object : Callback<UserLeagueResponse> {
            @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onResponse(
                call: Call<UserLeagueResponse>,
                response: Response<UserLeagueResponse>
            ) {
                val res = response.body()
                if (response.isSuccessful) {
                    if (res?.success == true) {
//                        myDialoge.dismiss()

                        //loading.isDismiss()
                        Log.d("success", "xxxx" + res.userLeagues[0].draftDate)

                        binding.tvStartTime.text = res.userLeagues[0].draftDate

                    } else {


                    }

                } else {

                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected exception
             * occurred creating the request or processing the response.
             */
            override fun onFailure(call: Call<UserLeagueResponse>, t: Throwable) {
                call.cancel()
                Log.d("vr", "" + t.message)

            }

        })
    }

    private fun AllocateMemory() {
        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
        sharedPrefManager = SharedPrefManager.getInstance(requireActivity().applicationContext)

        positionList = ArrayList<Position>()
    }

    private fun showDialoge2()
    {
        val dialogebind2 = layoutInflater.inflate(R.layout.custom_dialogebox, null)
        myDialoge2 = Dialog(requireActivity())
        myDialoge2.setContentView(dialogebind2)

        val civ_progress = dialogebind2.findViewById<CircleImageView>(R.id.civ_progress)
        civ_progress.animate().rotation(360f).setDuration(500).start()

        myDialoge2.setCancelable(false)
        myDialoge2.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun onPositionClick(positionMain: Position) {
        val sharedPreference =
            requireActivity().getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()

        try {
            if (requireActivity() is BottomNavigationActivity) {
                val act = requireActivity() as BottomNavigationActivity
                val bundle = Bundle()
                bundle.putString("ChooseTab", "MyTeam")

                bundle.putBoolean("allowAdd", true)
                bundle.putInt("positionListId", positionMain.id)
                bundle.putString("PG", "" + positionMain.code)
                bundle.putString("league_id", "" + league_id)
                bundle.putString("position_id", "" + positionMain.id)
                bundle.putString("totalSalary", "" + positionMain.salary)
                if (positionMain.player.isNotEmpty()) {
                    bundle.putInt("draft_id", positionMain.player[0].draft_id)
                }
                var tempSalary = totalSalary - totalUsedSalary
                if(positionMain.player.isNotEmpty()){
                    if(positionMain.player[0].salary != "" || positionMain.player[0].salary != "0"){
                        tempSalary += positionMain.player[0].salary.toInt()
                    }
                    bundle.putString("pid", positionMain.player[0].pid)
                } else {
                    bundle.putString("pid", "0")
                }

                bundle.putInt("remainSalary", tempSalary)

                bundle.putInt("totalSmall", totalSmall)
                bundle.putInt("totalStarter", totalStarter)
                bundle.putInt("totalStar", totalStar)

                editor.putString("totalSalary", "" + positionMain.salary)
                editor.apply()

                val fragment = SelectPlayerFragment()
                fragment.arguments = bundle

                act.addFragment(fragment)
                totalUsedSalary = 0
            }
        } catch (er: Exception) {
            Log.d("LoadFrag", er.message.toString())
        }
    }

    fun onPositionDraftRemove(position: Int, positionMain: Position)
    {
        if (positionMain.player.size == 0)
        {
            Draft_id = ""
        }
        else
        {
            Draft_id = positionMain.player[0].draft_id.toString()
        }

        val builder = AlertDialog.Builder(requireActivity())

        builder.setMessage("You want to remove this player from the draft, are you sure?")

        builder.setTitle(getString(R.string.app_name))

        builder.setCancelable(false)

        builder.setPositiveButton(getString(R.string.yes))
        {
                dialog, which ->
            showDialoge2()
            apiInterface.removeFromDraft("Bearer $HeaderToken",""+Draft_id).enqueue(object :
                Callback<Draft_Remove_Response> {

                override fun onResponse(call: Call<Draft_Remove_Response>, response: Response<Draft_Remove_Response>)
                {
                    if (response.isSuccessful)
                    {
                        if (response.body()!!.success==true)
                        {
                            dialog.dismiss()
                            myDialoge2.dismiss()
                            MessageForValid(""+ response.body()!!.message.toString())
                            setEvents()
                        }
                        else
                        {
                            dialog.dismiss()
                            myDialoge2.dismiss()
                            Log.d("krunal"," else if response "+response.body())
                            MessageForValid(""+ response.body()!!.message.toString())
                        }
                    }
                    else
                    {
                        dialog.dismiss()
                        myDialoge2.dismiss()
                        Log.d("krunal"," else if response "+response.body())
                        val errorResponse = Gson().fromJson(response.errorBody()!!.charStream(), LoginErrorResponse::class.java)
                        MessageForValid(""+ errorResponse.message.toString())
                    }
                }
                override fun onFailure(call: Call<Draft_Remove_Response>, t: Throwable) {
                    call.cancel()
                }

            })

        }
        builder.setNegativeButton(getString(R.string.no)) { // If user click no then dialog box is canceled.
                dialog, which -> dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()



    }
}
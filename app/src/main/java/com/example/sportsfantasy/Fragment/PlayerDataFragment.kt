package com.example.sportsfantasy.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sportsfantasy.Adapter.PlayerListAdapter
import com.example.sportsfantasy.Adapter.PlayerListColumnAdapter
import com.example.sportsfantasy.Model.Player
import com.example.sportsfantasy.Model.PlayerListResponse
import com.example.sportsfantasy.Network.ApiInterface
import com.example.sportsfantasy.Network.RetrofitClient
import com.example.sportsfantasy.R
import com.example.sportsfantasy.databinding.FragmentPlayerDataBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayerDataFragment : Fragment()
{
    lateinit var binding:FragmentPlayerDataBinding
    lateinit var apiInterface: ApiInterface
    lateinit var playerListAdapter: PlayerListAdapter
    lateinit var arrSearchPlayerList: MutableList<Player>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlayerDataBinding.inflate(layoutInflater)

        apiInterface = RetrofitClient.getInstance().create(ApiInterface::class.java)
        binding.rvPlayerDetails.setHasFixedSize(true)
        binding.rvPlayerDetails.layoutManager = LinearLayoutManager(requireContext())
        arrSearchPlayerList = ArrayList<Player>();
        apiInterface.getPlayerList("Bearer 268|CNj9egEUdzFz3swIYmWpAAndghKc4n44M7hUD2kl", "c",  ""+ 0,"","0").enqueue(object :
            Callback<PlayerListResponse>
        {
            override fun onResponse(call: Call<PlayerListResponse>, response: Response<PlayerListResponse>)
            {
                val playerListResponse = response.body()
                if (playerListResponse != null)
                {
                    if (playerListResponse.success)
                    {
                        arrSearchPlayerList.addAll(playerListResponse.playerList)

                        playerListAdapter = PlayerListAdapter(requireContext(), arrSearchPlayerList)

                        binding.rvPlayerDetails.adapter = playerListAdapter
                        playerListAdapter.notifyDataSetChanged()


                    } else {

                    }
                }
            }
            override fun onFailure(call: Call<PlayerListResponse>, t: Throwable) {
                call.cancel()
            }

        })



        return binding.root
    }
}
package com.example.sportsfantasy.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsfantasy.Interface.ILoadMore;
import com.example.sportsfantasy.Model.Player;

import java.util.ArrayList;

public class PlayerListAdapter_java extends RecyclerView.Adapter<PlayerListAdapter_java.MyViewHolder> {

    ArrayList<Player> getPlayerSqlite = new ArrayList<>();
    Context context;
    ILoadMore iLoadMore;
    Boolean isLoading;
    int visibleThreSold = 5;
    int lastVisible,totalItemCount;

    public PlayerListAdapter_java(RecyclerView recyclerView,ArrayList<Player> getPlayerSqlite, Context context)
    {
        this.getPlayerSqlite = getPlayerSqlite;
        this.context = context;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
             @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

             }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisible = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= lastVisible+visibleThreSold)
                {
                    if (iLoadMore != null)
                    {
                        iLoadMore.onLoadMore();
                    }
                    isLoading = true;
                }
             }
        });

    }

    @NonNull
    @Override
    public PlayerListAdapter_java.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerListAdapter_java.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

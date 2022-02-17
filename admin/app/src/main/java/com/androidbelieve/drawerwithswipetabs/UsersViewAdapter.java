package com.androidbelieve.drawerwithswipetabs;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class UsersViewAdapter extends RecyclerView.Adapter<UsersViewHolders> {

    private List<String> nameList;
    private List<String> statusList;
    private List<String> eidList;
    private List<String> phnList;
    private Context context;

    public UsersViewAdapter(Context context, List<String> nameList, List<String> statusList,
                            List<String> eidList, List<String> phnList) {
        this.nameList = nameList;
        this.statusList = statusList;
        this.eidList = eidList;
        this.phnList = phnList;
        this.context = context;
    }

    @Override
    public UsersViewHolders onCreateViewHolder(ViewGroup parent, int viewType)
    {

        View layoutView = LayoutInflater.from(context).inflate(R.layout.users_list_card, null);
        UsersViewHolders rcv = new UsersViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(UsersViewHolders holder, int position)
    {
        holder.name.setText(nameList.get(position));
        holder.status.setText(statusList.get(position));
        holder.email.setText(eidList.get(position));
        holder.phn.setText(phnList.get(position));

    }


    @Override
    public int getItemCount() {
        return this.nameList.size();
    }
}
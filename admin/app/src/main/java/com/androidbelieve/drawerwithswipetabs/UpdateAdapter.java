package com.androidbelieve.drawerwithswipetabs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.List;

public class UpdateAdapter extends RecyclerView.Adapter<UpdateHolder> {

    private Context context;

    public UpdateAdapter(Context context) {
        this.context = context;
    }

    @Override
    public UpdateHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_update_card, null);
        UpdateHolder rcv = new UpdateHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final UpdateHolder holder, final int position) {
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
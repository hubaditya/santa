package com.androidbelieve.drawerwithswipetabs;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;


public class RecyclerViewHolders extends ParentViewHolder implements View.OnClickListener
{
    public TextView orderID,totAmt,timeofdel,instr;
    String city, college, vendor, customer, contact, instruct, status, vContact;

    public RecyclerViewHolders(View itemView)
    {
        super(itemView);
        itemView.setOnClickListener(this);
        orderID = (TextView)itemView.findViewById(R.id.textView);
        totAmt=(TextView)itemView.findViewById(R.id.textView4);
        timeofdel=(TextView)itemView.findViewById(R.id.textView13);
        instr=(TextView)itemView.findViewById(R.id.textView15);
    }

    @Override
    public void onClick(View view) {
    }
}
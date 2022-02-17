package com.deliverysanta.vendorapp;

import android.view.View;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ParentViewHolder;

public class RecyclerViewHolders extends ParentViewHolder implements View.OnClickListener
{
    public TextView orderID, totAmt, timeOfdel, instr, oId;
    public String userContact, code, instruct, orID, status;

    public RecyclerViewHolders(View itemView)
    {
        super(itemView);
        itemView.setOnClickListener(this);

        orderID = (TextView)itemView.findViewById(R.id.textView);
        oId = (TextView)itemView.findViewById(R.id.textView16);
        totAmt=(TextView)itemView.findViewById(R.id.textView4);
        timeOfdel = (TextView)itemView.findViewById(R.id.textView13);
        instr=(TextView)itemView.findViewById(R.id.textView15);
    }

    /*@Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked ORDER = " + getPosition(), Toast.LENGTH_SHORT).show();
    }*/
}
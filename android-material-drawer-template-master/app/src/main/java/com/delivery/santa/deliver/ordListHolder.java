package com.delivery.santa.deliver;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ordListHolder extends RecyclerView.ViewHolder {

    public TextView ordId,cusId,bill,bag, code;
    public Button button;

    public ordListHolder(View itemView)
    {
        super(itemView);
        ordId = (TextView)itemView.findViewById(R.id.textView3);
        cusId = (TextView)itemView.findViewById(R.id.textView4);
        bill = (TextView)itemView.findViewById(R.id.textView6);
        bag = (TextView)itemView.findViewById(R.id.textView5);
        button = (Button)itemView.findViewById(R.id.button3);
        code = (TextView) itemView.findViewById(R.id.textView10);
    }
}

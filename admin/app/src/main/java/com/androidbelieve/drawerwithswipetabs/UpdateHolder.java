package com.androidbelieve.drawerwithswipetabs;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class UpdateHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView city,clg,vdr,req,cat,dname,dop,dnp;

    public UpdateHolder(View itemView)
    {
        super(itemView);
        itemView.setOnClickListener(this);
        city = (TextView)itemView.findViewById(R.id.textView16);
        clg = (TextView)itemView.findViewById(R.id.textView17);
        vdr = (TextView)itemView.findViewById(R.id.textView18);
        req = (TextView)itemView.findViewById(R.id.textView19);
        cat = (TextView)itemView.findViewById(R.id.textView20);
        dname = (TextView)itemView.findViewById(R.id.textView21);
        dop = (TextView)itemView.findViewById(R.id.textView22);
        dnp = (TextView)itemView.findViewById(R.id.textView23);
    }

    @Override
    public void onClick(View view)
    {
        AlertDialog.Builder alert1 = new AlertDialog.Builder(view.getContext());
        alert1.setTitle("VERIFY"); //Set Alert dialog title here
        alert1.setMessage("Do you approve this request?"); //Message here
        alert1.setPositiveButton("YES", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert1.setNegativeButton("NO", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        AlertDialog alertDialog1 = alert1.create();
        alertDialog1.show();
    }
}
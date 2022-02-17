package com.deliverysanta.vendorapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class VendorMenuAdapter extends BaseAdapter
{
    private final Context context;
    private LayoutInflater inflater;
    private ArrayList<String> mNames, mPrices, mStatus;

    public VendorMenuAdapter(Context context, ArrayList<String> mNames, ArrayList<String> mPrices, ArrayList<String>mStatus)
    {
        this.context=context;
        this.mNames = mNames;
        this.mPrices = mPrices;
        this.mStatus = mStatus;
    }

    @Override
    public int getCount() {
        return mNames.size();
    }

    @Override
    public Object getItem(int i) {
        return mNames.get(i);
    }

    @Override
    public long getItemId(int i) {
        return mNames.indexOf(getItem(i));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder2 holder = null;

        if (convertView == null)
        {
            holder = new ViewHolder2();
            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.dish, null);
            holder.tv1=(TextView)convertView.findViewById(R.id.name);
            holder.tv2=(TextView)convertView.findViewById(R.id.price);
            convertView.setTag(holder);
        }

        else holder = (ViewHolder2) convertView.getTag();

        holder.tv1.setText(mNames.get(position));
        holder.tv2.setText(mPrices.get(position));

        if(mStatus.get(position).equals("0")) {
            convertView.setBackgroundColor(Color.RED);
        }

        else convertView.setBackgroundColor(Color.WHITE);

        return convertView;
    }

    class ViewHolder2 {
        public TextView tv1,tv2;
    }
}

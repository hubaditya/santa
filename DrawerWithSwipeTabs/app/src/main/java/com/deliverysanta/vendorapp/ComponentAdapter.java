package com.deliverysanta.vendorapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import java.util.List;

public class ComponentAdapter extends RecyclerView.Adapter<ComponentAdapter.ComponentHolder>
{
    Component cp;
    Context context;
    List<Component> objects;

    public ComponentAdapter(Context context, List<Component> objects)
    {
        //super(context, objects);
        this.context = context;
        this.objects = objects;

    }

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
       ComponentHolder holder = null;

        if (convertView == null)
        {
            holder = new ComponentHolder();

            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.my_order_card_list_item, null);
            cp = getItem(position);
            holder.name = (TextView) convertView.findViewById(R.id.textView15);
            holder.qty = (TextView) convertView.findViewById(R.id.textView17);

            convertView.setTag(holder);
        }

        else holder = (ComponentHolder)convertView.getTag();

        holder.name.setText(cp.getName());
        holder.qty.setText(cp.getQty());

        return convertView;
    }

    public int getOrderSize() {
        return nos;
    }
*/
    @Override
    public ComponentHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        /*View view = mInflater.inflate(R.layout.my_order_card_list_item, parent, false);*/
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_list_item, parent, false);
        ComponentHolder viewHolder = new ComponentHolder(layoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ComponentHolder holder, int position)
    {
        cp = objects.get(position);
        holder.name.setText(cp.getName());
        holder.qty.setText(cp.getQty());

        holder.name.setTag(position);
        holder.qty.setTag(position);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ComponentHolder extends ChildViewHolder
    {
        TextView name, qty;

        public ComponentHolder(View itemView)
        {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            qty = (TextView) itemView.findViewById(R.id.qty);
        }
    }
}
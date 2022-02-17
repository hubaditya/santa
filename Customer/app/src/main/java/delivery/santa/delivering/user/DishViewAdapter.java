package delivery.santa.delivery.customer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.materialviewpager.sample.R;

import java.util.ArrayList;
import java.util.List;

public class DishViewAdapter extends RecyclerView.Adapter<DishViewAdapter.ViewHolder>
{
    ViewHolder viewHolder;
    static String vendorName;
    DB db;
    private final LayoutInflater mInflater;
    private final List<ExampleModel> mName;
    private final List<ExamplePrice> mPrice;
    private final List<ExampleCategory> mCat;
    private TextView tvv1;
    private TextView tvv2;

    public DishViewAdapter(Context context, DB db, List<ExampleModel> mName, List<ExamplePrice> mPrice, String vendorname, TextView tvv1, List<ExampleCategory> mCat)
    {
        this.db=db;
        tvv2=MainActivity.tvf;
        this.vendorName=vendorname;
        mInflater = LayoutInflater.from(context);
        this.mName = new ArrayList<>(mName);
        this.mPrice = new ArrayList<>(mPrice);
        this.mCat = new ArrayList<>(mCat);
        this.tvv1=tvv1;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.dish_card, parent, false);
        viewHolder = new ViewHolder(view,db);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {

        final ExampleModel name = mName.get(position);
        final ExamplePrice price = mPrice.get(position);
        final ExampleCategory cat = mCat.get(position);

        holder.bind(name, price, cat);

        holder.id=holder.db1.getID(holder.n1.getText().toString(),vendorName);

        Cursor cursor = holder.db1.getItemDetail(holder.id);
        if (cursor.moveToFirst())
        {
            do {
                String data = cursor.getString(cursor.getColumnIndex("qty"));
                holder.q1.setText(data);
            }while (cursor.moveToNext());
        }

        else holder.q1.setText(String.valueOf(0));
        cursor.close();

        holder.a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                holder.c[position] = Integer.parseInt(holder.q1.getText().toString());
                holder.c[position]++;
                holder.q1.setText(String.valueOf(holder.c[position]));

                if (holder.id > 0) {
                    holder.db1.updateItem(holder.id, String.valueOf(holder.c[position]), String.valueOf((holder.c[position]) * Integer.parseInt(holder.p1.getText().toString())));
                    Cursor cursor1 = holder.db1.getAllItems();
                    Double amt = 0.0;
                    int q = 0;
                    cursor1.moveToFirst();
                    if (cursor1.moveToFirst()) {

                        do {
                            amt = amt + Double.parseDouble(cursor1.getString(cursor1.getColumnIndex("amount")));
                            q = q + Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("qty")));

                        } while (cursor1.moveToNext());
                    }
                    /*tvv1.setText(String.valueOf(q));
                    tvv2.setText(String.valueOf(q));*/
                    Snackbar.make(v, "TOTAL: Rs." + amt, Snackbar.LENGTH_SHORT).show();

                } else {
                    holder.id = (int) holder.db1.addItem(new Item(holder.n1.getText().toString(), holder.p1.getText().toString(), String.valueOf(holder.c[position]), vendorName));
                    Cursor cursor1 = holder.db1.getAllItems();
                    Double amt = 0.0;
                    int q = 0;
                    cursor1.moveToFirst();
                    if (cursor1.moveToFirst()) {

                        do {
                            amt = amt + Double.parseDouble(cursor1.getString(cursor1.getColumnIndex("amount")));
                            q = q + Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("qty")));
                        } while (cursor1.moveToNext());
                    }
                    /*tvv1.setText(String.valueOf(q));
                    tvv2.setText(String.valueOf(q));*/
                    Snackbar.make(v, "TOTAL: Rs." + amt, Snackbar.LENGTH_SHORT).show();

                }

            }
        });

        holder.r1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                int position = (Integer) v.getTag();
                holder.c[position] = Integer.parseInt(holder.q1.getText().toString());

                if (holder.c[position] > 1)
                {
                    holder.c[position]--;
                    holder.db1.updateItem(holder.id, String.valueOf(holder.c[position]), String.valueOf((holder.c[position]) * Integer.parseInt(holder.p1.getText().toString())));
                    Cursor cursor1=holder.db1.getAllItems();
                    Double amt=0.0;
                    int q=0;
                    cursor1.moveToFirst();
                    if (cursor1.moveToFirst()) {

                        do {
                            amt = amt + Double.parseDouble(cursor1.getString(cursor1.getColumnIndex("amount")));
                            q = q + Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("qty")));
                        } while (cursor1.moveToNext());
                    }
                    /*tvv1.setText(String.valueOf(q));
                    tvv2.setText(String.valueOf(q));*/
                    Snackbar.make(v, "TOTAL: Rs."+amt, Snackbar.LENGTH_SHORT).show();

                }

                else
                {
                    holder.c[position] = 0;
                    holder.db1.deleteItem(holder.id);
                    holder.id = -1;
                    Cursor cursor1=holder.db1.getAllItems();
                    int q=0;
                    cursor1.moveToFirst();
                    if (cursor1.moveToFirst()) {
                        do {
                            q = q + Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("qty")));
                        } while (cursor1.moveToNext());
                    }
                    /*tvv1.setText(String.valueOf(q));
                    tvv2.setText(String.valueOf(q));*/
                }

                holder.q1.setText(String.valueOf(holder.c[position]));

            }
        });

        holder.a1.setTag(position);
        holder.r1.setTag(position);
        holder.n1.setTag(position);
        holder.p1.setTag(position);
        holder.i1.setTag(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mName.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView r1,a1;
        int id = -1;
        TextView q1,n1,p1;
        private ImageView i1;
        int[] c = new int[200];
        DB db1;

        public void bind(ExampleModel name, ExamplePrice price, ExampleCategory cat)
        {
            n1.setText(name.getText());
            p1.setText(price.getText());
            if(cat.getText().equals("NonVeg"))
                i1.setBackgroundResource(R.drawable.nonvegdish);
            else if(cat.getText().equals("Veg"))
                i1.setBackgroundResource(R.drawable.vegdish);
            else i1.setBackgroundColor(Color.BLACK);
        }

        public ViewHolder(final View itemView, final DB db)
        {
            super(itemView);
            this.db1=db;

            r1=(TextView)itemView.findViewById(R.id.remove1);
            a1=(TextView)itemView.findViewById(R.id.add1);
            q1=(TextView)itemView.findViewById(R.id.quantity1);
            n1=(TextView)itemView.findViewById(R.id.name1);
            p1=(TextView)itemView.findViewById(R.id.price1);
            i1=(ImageView)itemView.findViewById(R.id.imageView3);

            for(int i = 0;i<200;i++)
            {
                c[i]=0;
            }
            id = db1.getID(n1.getText().toString(), vendorName);
            Cursor cursor = db1.getItemDetail(id);

            if (cursor.moveToFirst())
            {
                do {
                    String data = cursor.getString(cursor.getColumnIndex("qty"));
                    q1.setText(data);
                }while (cursor.moveToNext());
            }

            else q1.setText(String.valueOf(0));
            cursor.close();


/*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View toolbar = itemView.findViewById(R.id.toolbar1);

                    if (toolbar.getVisibility() == View.GONE)
                    {
                        ExpandAnimation expandAni = new ExpandAnimation(toolbar, 500);
                        toolbar.startAnimation(expandAni);

                        id = db1.getID(n1.getText().toString(), vendorName);
                        Cursor cursor = db1.getItemDetail(id);

                        if (cursor.moveToFirst())
                        {
                            do {
                                String data = cursor.getString(cursor.getColumnIndex("qty"));
                                q1.setText(data);
                            }while (cursor.moveToNext());
                        }

                        else q1.setText(String.valueOf(0));
                        cursor.close();
                    }

                    if (toolbar.getVisibility() == View.VISIBLE)
                    {
                        CollapseAnimation collapseAni = new CollapseAnimation(toolbar, 5000);
                        toolbar.startAnimation(collapseAni);
                    }
                }
            });
*/
        }
    }
}
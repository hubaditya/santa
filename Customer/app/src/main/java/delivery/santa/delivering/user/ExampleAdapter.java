package delivery.santa.delivery.customer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.materialviewpager.sample.R;

import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder>
{

    ExampleViewHolder viewfinder;
    DB db;
    private final LayoutInflater mInflater;
    private final List<ExampleModel> mName;
    private final List<ExamplePrice> mPrice;
    private final List<ExampleCategory> mCat;

    static String vendorName;

    public ExampleAdapter(Context context, DB db, List<ExampleModel> names, List<ExamplePrice> prices, String vendorName, List<ExampleCategory> mCatt)
    {
        this.db=db;
        this.vendorName=vendorName;
        mInflater = LayoutInflater.from(context);
        mName = new ArrayList<>(names);
        mPrice = new ArrayList<>(prices);
        mCat = new ArrayList<>(mCatt);

    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = mInflater.inflate(R.layout.dish_card, parent, false);
        viewfinder = new ExampleViewHolder(itemView,db);
        return viewfinder;
    }

    @Override
    public void onBindViewHolder(final ExampleViewHolder holder, int position)
    {
        final ExampleModel name = mName.get(position);
        final ExamplePrice price = mPrice.get(position);
        final ExampleCategory cat = mCat.get(position);

        holder.bind(name, price, cat);

        holder.id = db.getID(holder.n1.getText().toString(), vendorName);

        Cursor cursor = db.getItemDetail(holder.id);

        if (cursor.moveToFirst())
        {
            do {
                String data = cursor.getString(cursor.getColumnIndex("qty"));
                holder.q1.setText(data );
            } while (cursor.moveToNext());
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
                    //Toast.makeText(v.getContext(), "count = " + String.valueOf(holder.c[position]), Toast.LENGTH_LONG).show();
                } else {
                    holder.id = (int) holder.db1.addItem(new Item(holder.n1.getText().toString(), holder.p1.getText().toString(), String.valueOf(holder.c[position]), vendorName));
                }

            }
        });
        holder.r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                int position = (Integer) v.getTag();
                holder.c[position] = Integer.parseInt(holder.q1.getText().toString());

                if (holder.c[position] > 1) {
                    holder.c[position]--;
                    holder.db1.updateItem(holder.id, String.valueOf(holder.c[position]), String.valueOf((holder.c[position]) * Integer.parseInt(holder.p1.getText().toString())));
                } else {
                    holder.c[position] = 0;
                    holder.db1.deleteItem(holder.id);
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

    public void animateTo(List[] list)
    {
        applyAndAnimateRemovals(list[0]);
        applyAndAnimateAdditions(list[0], list[1], list[2]);
        applyAndAnimateMovedItems(list[0]);
    }

    private void applyAndAnimateRemovals(List<ExampleModel> names)
    {
        for (int i = mName.size() - 1; i >= 0; i--)
        {
            final ExampleModel name = mName.get(i);

            if (!names.contains(name)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<ExampleModel> names, List<ExamplePrice> prices, List<ExampleCategory> category)
    {
        for (int i = 0, count = names.size(); i < count; i++)
        {
            final ExampleModel name = names.get(i);
            final ExamplePrice price = prices.get(i);
            final ExampleCategory cat = category.get(i);

            if (!mName.contains(name)) {
                addItem(i, name, price, cat);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<ExampleModel> names)
    {
        for (int toPosition = names.size() - 1; toPosition >= 0; toPosition--)
        {
            final ExampleModel name = names.get(toPosition);
            final int fromPosition = mName.indexOf(name);

            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public void removeItem(int position)
    {
        mName.remove(position);
        mPrice.remove(position);
        mCat.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(int position, ExampleModel name, ExamplePrice price, ExampleCategory category)
    {
        mName.add(position, name);
        mPrice.add(position, price);
        mCat.add(position, category);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition)
    {
        final ExampleModel name = mName.remove(fromPosition);
        final ExamplePrice price = mPrice.remove(fromPosition);
        final ExampleCategory category = mCat.remove(fromPosition);
        mName.add(toPosition, name);
        mPrice.add(toPosition,price);
        mCat.add(toPosition, category);
        notifyItemMoved(fromPosition, toPosition);
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder
    {
        TextView r1,a1;
        int id = -1;
        TextView q1,n1,p1;
        ImageView i1;
        int[] c = new int[20];
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

        public ExampleViewHolder(final View itemView, final DB db)
        {
            super(itemView);
            this.db1=db;
            r1=(TextView)itemView.findViewById(R.id.remove1);
            a1=(TextView)itemView.findViewById(R.id.add1);
            q1=(TextView)itemView.findViewById(R.id.quantity1);
            n1=(TextView)itemView.findViewById(R.id.name1);
            p1=(TextView)itemView.findViewById(R.id.price1);
            i1=(ImageView)itemView.findViewById(R.id.imageView3);
            for(int i = 0;i<20;i++)
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
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    View toolbar = itemView.findViewById(R.id.toolbar1);
                    if (toolbar.getVisibility() == View.GONE)
                    {
                        // Creating the expand animation for the item
                        ExpandAnimation expandAni = new ExpandAnimation(toolbar, 500);
                        // Start the animation on the toolbar
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
                        //creating the collapse animation for the item
                        CollapseAnimation collapseAni = new CollapseAnimation(toolbar, 5000);
                        // Start the animation on the toolbar
                        toolbar.startAnimation(collapseAni);
                    }
                }
            });
*/
        }
    }
}
package delivery.santa.delivery.customer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.florent37.materialviewpager.sample.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartViewHolder extends BaseAdapter
{
    private final Context context;
    private LayoutInflater inflater;
    DB db;
    private ProgressDialog progressDialog;
    private int delP;

    private ArrayList<Integer> id;
    private ArrayList<String> name;
    private ArrayList<Integer> qty;
    private ArrayList<Integer> price;
    private ArrayList<Integer> amt;
    private TextView tv5,tv6,tv7;
    int sub=0,del=0,tot=0;

    public CartViewHolder(Context context, DB db, ArrayList<String> name, ArrayList<Integer> qty, ArrayList<Integer> price, ArrayList<Integer> amt, ArrayList<Integer> id, TextView tv1, TextView tv2, TextView tv3)
    {
        this.context = context;
        this.db=db;
        this.name=name;
        this.qty=qty;
        this.price=price;
        this.amt=amt;
        this.id=id;
        this.tv5=tv1;
        this.tv6=tv2;
        this.tv7=tv3;
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);

    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder2 holder = null;

        if (convertView == null)
        {
            holder = new ViewHolder2();
            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.cart_card, null);

            holder.tv1 = (TextView) convertView.findViewById(R.id.textView4); //Name
            holder.tv2 = (TextView) convertView.findViewById(R.id.textView11); //Price
            holder.tv3 = (TextView) convertView.findViewById(R.id.textView12); //Amount
            holder.tv4 = (TextView) convertView.findViewById(R.id.quantity1); //Quantity
            holder.tv8=(TextView)convertView.findViewById(R.id.removeItem);
            holder.b1=(TextView)convertView.findViewById(R.id.add1); //Add
            holder.b2=(TextView)convertView.findViewById(R.id.remove1); //Subtract

            final ViewHolder2 finalHolder = holder;

            holder.tv8.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int position=(Integer) v.getTag();
                    db.deleteItem(id.get(position));
                    id.remove(position);
                    name.remove(position);
                    qty.remove(position);
                    price.remove(position);
                    amt.remove(position);
                    notifyDataSetChanged();

                    int q=0;
                    Cursor cursor1=db.getAllItems();
                    cursor1.moveToFirst();
                    if (cursor1.moveToFirst()) {
                        do {
                            q = q + Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("qty")));
                        } while (cursor1.moveToNext());
                    }
                    MainActivity.tvf.setText(String.valueOf(q));
                    sub=del=tot=0;
                    for(int i = 0; i < amt.size(); i++)
                    {sub += amt.get(i);}

                    getDelPrice(String.valueOf(sub));

                    tv5.setText(String.valueOf(sub));
                    tv6.setText(String.valueOf(del));
                    tv7.setText(String.valueOf(tot));
                }
            });

            holder.b1.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int position=(Integer) v.getTag();
                    qty.set(position,Integer.parseInt(finalHolder.tv4.getText().toString())+1);
                    finalHolder.tv4.setText(String.valueOf(qty.get(position)));
                    finalHolder.tv3.setText(String.valueOf(Integer.parseInt(finalHolder.tv2.getText().toString()) * Integer.parseInt(finalHolder.tv4.getText().toString())));

                    amt.set(position, qty.get(position) * price.get(position));
                    db.updateItem(id.get(position), String.valueOf(qty.get(position)), String.valueOf(amt.get(position)));

                    sub=del=tot=0;
                    for(int i = 0; i < amt.size(); i++)
                    {sub += amt.get(i);}

                    getDelPrice(String.valueOf(sub));

                    int q=0;
                    Cursor cursor1=db.getAllItems();
                    cursor1.moveToFirst();
                    if (cursor1.moveToFirst()) {
                        do {
                            q = q + Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("qty")));
                        } while (cursor1.moveToNext());
                    }
                    MainActivity.tvf.setText(String.valueOf(q));

                    tv5.setText(String.valueOf(sub));
                    tv6.setText(String.valueOf(del));
                    tv7.setText(String.valueOf(tot));
                }
            });

            holder.b2.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int position=(Integer)v.getTag();

                    if (qty.get(position) > 1)
                    {
                        qty.set(position, Integer.parseInt(finalHolder.tv4.getText().toString()) - 1);
                        finalHolder.tv4.setText(String.valueOf(qty.get(position)));
                        finalHolder.tv3.setText(String.valueOf(Integer.parseInt(finalHolder.tv2.getText().toString()) * Integer.parseInt(finalHolder.tv4.getText().toString())));

                        amt.set(position, qty.get(position) * price.get(position));
                        db.updateItem(id.get(position), String.valueOf(qty.get(position)), String.valueOf(qty.get(position) * price.get(position)));
                    }

                    else
                    {
                        db.deleteItem(id.get(position));
                        id.remove(position);
                        name.remove(position);
                        qty.remove(position);
                        price.remove(position);
                        amt.remove(position);
                        notifyDataSetChanged();
                    }
                    sub=del=tot=0;
                    for(int i = 0; i < amt.size(); i++)
                    {sub += amt.get(i);}

                    getDelPrice(String.valueOf(sub));

                    int q=0;
                    Cursor cursor1=db.getAllItems();
                    cursor1.moveToFirst();
                    if (cursor1.moveToFirst()) {
                        do {
                            q = q + Integer.parseInt(cursor1.getString(cursor1.getColumnIndex("qty")));
                        } while (cursor1.moveToNext());
                    }
                    MainActivity.tvf.setText(String.valueOf(q));

                    tv5.setText(String.valueOf(sub));
                    tv6.setText(String.valueOf(del));
                    tv7.setText(String.valueOf(tot));

                }
            });

            convertView.setTag(holder);
        }

        holder = (ViewHolder2) convertView.getTag();
        holder.b1.setTag(position);
        holder.b2.setTag(position);
        holder.tv1.setTag(position);
        holder.tv2.setTag(position);
        holder.tv4.setTag(position);
        holder.tv8.setTag(position);

        holder.tv1.setText(name.get(position)); //name
        holder.tv2.setText(String.valueOf(price.get(position))); //price
        holder.tv3.setText(String.valueOf(amt.get(position))); //amount
        holder.tv4.setText(String.valueOf(qty.get(position))); //qty

        return convertView;
    }

    class ViewHolder2
    {
        TextView tv1, tv2, tv3, tv4,tv8;
        TextView b1, b2;
    }

    public void getDelPrice(final String sub) {
        String tag_string_req = "req_vendor";
        progressDialog.setMessage("Loading ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELIVPRICE, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String dp=jsonObject.getString("deliveryprice");
                    Double dd=Double.parseDouble(dp);
                    delP=(int) Math.round(dd);
                    del=delP;
                    tot=Integer.parseInt(sub)+del;
                    tv5.setText(String.valueOf(sub));
                    tv6.setText(String.valueOf(del));
                    tv7.setText(String.valueOf(tot));

                }

                catch (JSONException e) {
                    Toast.makeText(context, "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(context, "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "myorders");
                params.put("totalAmount", sub);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog()
    {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog()
    {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
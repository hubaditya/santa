package com.androidbelieve.drawerwithswipetabs;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VendorMenuAdapter extends BaseAdapter
{
    private final Context context;
    private LayoutInflater inflater;
    private ArrayList<String> mNames;
    private ArrayList<String> mPrices;
    private String city;
    private String college;
    private String vendor;
    private ProgressDialog pDialog;

    public VendorMenuAdapter(Context context, String city, String college, String vendor,
                             ArrayList<String> mNames, ArrayList<String> mPrices)
    {
        this.context=context;
        this.city = city;
        this.college = college;
        this.vendor = vendor;
        this.mNames = mNames;
        this.mPrices = mPrices;
    }

    @Override
    public int getCount() {
        return mNames.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder2 holder = null;

        if (convertView == null)
        {
            holder = new ViewHolder2();
            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.dish, null);

            holder.tv1 = (TextView) convertView.findViewById(R.id.name); //Name
            holder.tv2=(TextView)convertView.findViewById(R.id.price); //Price

            holder.tv1.setText(mNames.get(position));
            holder.tv2.setText(mPrices.get(position));

            final ViewHolder2 finalHolder = holder;
            convertView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    LayoutInflater li = LayoutInflater.from(v.getContext());
                    View promptsView = li.inflate(R.layout.menu_dish_edit, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                    alertDialogBuilder.setView(promptsView);
                    alertDialogBuilder.setTitle("EDIT THE DISH");
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    TextView dishName = (TextView) alertDialog.findViewById(R.id.textView5);
                    TextView dishPrice = (TextView) alertDialog.findViewById(R.id.textView14);

                    Button updateButton = (Button) alertDialog.findViewById(R.id.button4);
                    Button deleteButton = (Button) alertDialog.findViewById(R.id.button5);
                    Button cancelButton = (Button) alertDialog.findViewById(R.id.button6);

                    final String newPrice = ((EditText) alertDialog.findViewById(R.id.editText17)).getText().toString();
                    dishName.setText(finalHolder.tv1.getText().toString());;
                    dishPrice.setText(finalHolder.tv2.getText().toString());

                    updateButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            pDialog = new ProgressDialog(context);
                            pDialog.setCancelable(false);
                            updateDish(city, college, vendor, mNames.get(position), newPrice);
                            finalHolder.tv2.setText(newPrice);
                            alertDialog.dismiss();
                        }
                    });

                    deleteButton.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            pDialog = new ProgressDialog(context);
                            pDialog.setCancelable(false);
                            deleteDish(city, college, vendor, mNames.get(position));
                            mNames.remove(position);
                            mPrices.remove(position);
                            alertDialog.dismiss();
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });
                }
            });

            convertView.setTag(holder);
        }

        holder = (ViewHolder2) convertView.getTag();
        holder.tv1.setTag(position);
        holder.tv2.setTag(position);

        return convertView;
    }

    public void updateDish(final String city, final String college, final String vendor,
                           final String dish, final String price)
    {
        String tag_string_req = "req_update";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UPDATE, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                try
                {
                   /* jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        String dish = jsonobject.getString("dish");
                        String price = jsonobject.getString("price");
                        mNames.add(dish);
                        mPrices.add(price);
                    }

                    VendorMenuAdapter menuAdapter = new VendorMenuAdapter(rootview.getContext(), city, clg,
                            vendor, mNames, mPrices);
                    listview.setAdapter(menuAdapter);*/

                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<>();
                params.put("tag", "menu");
                params.put("city", city);
                params.put("college", college);
                params.put("vendor", vendor);
                params.put("dish", dish);
                params.put("price", price);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void deleteDish(final String city, final String college, final String vendor, final String dish)
    {
        String tag_string_req = "req_menu";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                try
                {
                    /*jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        String dish = jsonobject.getString("dish");
                        String price = jsonobject.getString("price");
                        mNames.add(dish);
                        mPrices.add(price);
                    }

                    VendorMenuAdapter menuAdapter = new VendorMenuAdapter(rootview.getContext(), city, clg,
                            vendor, mNames, mPrices);
                    listview.setAdapter(menuAdapter);*/

                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<>();
                params.put("tag", "menu");
                params.put("city", city);
                params.put("college", college);
                params.put("vendor", vendor);
                params.put("dish", dish);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog()
    {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog()
    {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    class ViewHolder2 {
        TextView tv1,tv2;
    }
}

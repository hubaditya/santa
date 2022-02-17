package com.delivery.santa.deliver;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ordListAdapter extends RecyclerView.Adapter<ordListHolder>
{
    private final LayoutInflater mInflater;
    private Context context;
    private List<String> ordNo, cusIds, Bills, Bags, contact, code;
    private int len;
    private ProgressDialog progressDialog;
    private JSONClasses ob;

    public ordListAdapter(Context context, List<String> ordNo, List<String> cusIds, List<String> bags, List<String> contact, List<String> code, List<String> finalAmount)
    {
        mInflater = LayoutInflater.from(context);
        this.context=context;
        this.ordNo = ordNo;
        this.cusIds = cusIds;
        this.Bags = bags;
        Bills = finalAmount;
        this.contact = contact;
        this.code = code;
        /*db = new DBAdapter(context);
        cursor = db.fetchData();*/
        len = ordNo.size();
    }

    @Override
    public ordListHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View layoutView = mInflater.inflate(R.layout.card_order, null);
        ordListHolder rcv = new ordListHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final ordListHolder holder, int i)
    {
        final int q=i;

        /*if (cursor.moveToFirst())
        {
            do {
                ordIds.add(cursor.getString(cursor.getColumnIndex("orderno")));
                cusIds.add(cursor.getString(cursor.getColumnIndex("customer")));
                Bills.add(cursor.getString(cursor.getColumnIndex("bill")));
                Bags.add(cursor.getString(cursor.getColumnIndex("bags")));
                contact.add(cursor.getString(cursor.getColumnIndex("contact")));
                code.add(cursor.getString(cursor.getColumnIndex("code")));
            }while (cursor.moveToNext());
        }*/



        for(int j=0;j<len;j++)
        {
            holder.ordId.setText("OrderNo: "+ordNo.get(i));
            holder.cusId.setText("CustomerID: "+cusIds.get(i));
            holder.bill.setText("Rs."+Bills.get(i));
            holder.bag.setText("Bags: "+Bags.get(i));
            holder.code.setText("Code:"+code.get(i));

            holder.cusId.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final String phoneNo = contact.get(q);
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage("Do you want to call customer? "+phoneNo);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                    {
                        String uri = "tel:"+phoneNo;
                        public void onClick(DialogInterface dialog, int whichButton)
                        {
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse(uri));
                            context.startActivity(intent);
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }
            });
            holder.button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    ob = AppController.getList(context, "token");
                    String token = ob.token;
                    progressDialog = new ProgressDialog(v.getContext());
                    progressDialog.setCancelable(false);
                    holder.button.setBackgroundColor(Color.GREEN);
                    changeStatus(token, ordNo.get(q));
                }
            });

        }
    }

    private void changeStatus(final String token, final String orderNo)
    {
        String tag_string_req = "req_login";

        progressDialog.setMessage("Loading...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_STATUS, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                hideDialog();

                try {

                }

                catch (Exception e) {
                    Toast.makeText(context, "No or slow Internet connection. Please try again later.", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "No or slow Internet connection. Please try again later.", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "login");
                params.put("token", token);
                params.put("orderNo", orderNo);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public int getItemCount() {
        return len;
    }
}

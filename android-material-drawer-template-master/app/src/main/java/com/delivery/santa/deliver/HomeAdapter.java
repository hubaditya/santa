package com.delivery.santa.deliver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeAdapter extends BaseAdapter
{
    JSONClasses ob;
    private final Context context;
    private LayoutInflater inflater;
    private ArrayList<String> data;
    private ProgressDialog progressDialog;

    public HomeAdapter(Context context, ArrayList<String> dataItem)
    {
        this.context=context;
        this.data = dataItem;
    }

    @Override
    public int getCount() {
        return data.size();
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder2 holder = null;

        if (convertView == null)
        {
            holder = new ViewHolder2();
            inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.vendors_list_card, null);

            holder.tv1 = (TextView) convertView.findViewById(R.id.text1); //Name

            convertView.setTag(holder);
        }

        else holder = (ViewHolder2) convertView.getTag();

        final String vendor = data.get(position);

        holder.tv1.setText(vendor);

        holder.tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ob = AppController.getList(v.getContext(), "token");
                String token = ob.token;
                progressDialog = new ProgressDialog(v.getContext());
                progressDialog.setCancelable(false);
                getOrderByRest(token, vendor);
            }
        });

        holder.tv1.setTag(position);

        return convertView;
    }

    public void getOrderByRest(final String token, final String vendor)
    {
        ob = new JSONClasses();
        ob.id = new ArrayList<>();
        ob.cus = new ArrayList<>();
        ob.bill = new ArrayList<>();
        ob.con = new ArrayList<>();
        ob.code = new ArrayList<>();

        String tag_string_req = "req_list";

        progressDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ORDER, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                hideDialog();

                try {
                        JSONArray jsonArray = new JSONArray(response);

                        int sum = 0;

                        for(int i = 0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id4=jsonObject.getString("status");

                            if(id4.equals("0"))
                            {
                                String id = jsonObject.getString("restaurant");
                                String id1=jsonObject.getString("orderNo");
                                String id2=jsonObject.getString("orderBy");
                                String id3=jsonObject.getString("totalAmount");
                                String id5 = jsonObject.getString("userContactNo");
                                String id6 = jsonObject.getString("uniqueCode");

                                sum+=Integer.parseInt(id3);

                                ob.id.add(id1);
                                ob.cus.add(id2);
                                ob.bill.add(id3);
                                ob.con.add(id5);
                                ob.code.add(id6);
                            }
                        }

                        AppController.setList(context, "id", ob);
                        AppController.setList(context, "contact", ob);
                        AppController.setList(context, "code", ob);

                        Intent i=new Intent(context, VendorActivity.class);
                        //i.putExtra("vendorName", vendor);
                        i.putExtra("bill", sum);
                        context.startActivity(i);
                }

                catch (Exception e) {
                    Toast.makeText(context, "No or slow Internet connection. Please try again later.", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error)
            {
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
                params.put("restaurant", vendor);
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

    class ViewHolder2 {
        TextView tv1;
    }

}
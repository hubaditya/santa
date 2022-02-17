package com.delivery.santa.deliver;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

public class VendorAdapter extends BaseAdapter
{
    private final Context context;
    private LayoutInflater inflater;
    private ArrayList<String> data, cus, bill, code, contact;
    //DBAdapter db;
    private ProgressDialog progressDialog;

    public VendorAdapter(Context context, ArrayList<String> dataItem, ArrayList<String> cusNames, ArrayList<String> bills,
                         ArrayList<String> code, ArrayList<String> contact)
    {
        //db = new DBAdapter(context);
        this.context=context;
        this.data = dataItem;
        this.cus=cusNames;
        this.bill=bills;
        this.code = code;
        this.contact = contact;
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
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder2 holder = null;

        if (convertView == null)
        {
            holder = new ViewHolder2();
            inflater = LayoutInflater.from(this.context);
            convertView = inflater.inflate(R.layout.item_card, parent, false);

            holder.tv1 = (TextView) convertView.findViewById(R.id.textView); //Name
            holder.et1=(EditText)convertView.findViewById(R.id.editText); //No Of Bags
            holder.b1=(Button)convertView.findViewById(R.id.button6); //Done

            convertView.setTag(holder);
        }

        else holder = (ViewHolder2) convertView.getTag();

        holder.tv1.setText(data.get(position));
        holder.et1.setText("1", TextView.BufferType.EDITABLE);
        holder.et1.setInputType(InputType.TYPE_CLASS_NUMBER);

        final ViewHolder2 finalHolder = holder;

        holder.b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               /* db.insertData(data.get(position),cus.get(position),bill.get(position), finalHolder.et1.getText().toString(),
                        contact.get(position), code.get(position));*/

                progressDialog = new ProgressDialog(context);
                progressDialog.setCancelable(false);
                addBags(data.get(position), finalHolder.et1.getText().toString());

                finalHolder.b1.setBackgroundColor(Color.GREEN);
            }
        });

        holder.b1.setTag(position);
        holder.et1.setTag(position);
        holder.tv1.setTag(position);

        return convertView;
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

    public void addBags(final String orderNo, final String bags)
    {
        String tag_string_req = "req_list";
        progressDialog.setMessage("Loading ...");

        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_BAGS, new Response.Listener<String>()
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
                params.put("tag", "bagslist");
                params.put("orderNo", orderNo);
                params.put("bags", bags);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    class ViewHolder2
    {
        TextView tv1;
        Button b1;
        EditText et1;
    }
}

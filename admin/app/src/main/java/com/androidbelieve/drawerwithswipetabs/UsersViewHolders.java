package com.androidbelieve.drawerwithswipetabs;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

public class UsersViewHolders extends RecyclerView.ViewHolder
{
    TextView name, status, email, phn;
    Spinner spinner4;
    JSONClasses ob;
    String token;
    private ProgressDialog progressDialog;

    public UsersViewHolders(final View itemView)
    {
        super(itemView);
        name = (TextView)itemView.findViewById(R.id.textView6);
        status = (TextView)itemView.findViewById(R.id.textView7);
        email=(TextView)itemView.findViewById(R.id.textView8);
        phn=(TextView)itemView.findViewById(R.id.textView9);

        ob = AppController.getData(itemView.getContext(), "token");
        token = ob.token;

        itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LayoutInflater li = LayoutInflater.from(v.getContext());
                View promptsView = li.inflate(R.layout.user_update_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setView(promptsView);

                spinner4=(Spinner)promptsView.findViewById(R.id.spinner4);
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(promptsView.getContext(),
                        R.array.user_status_list, android.R.layout.simple_spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner4.setAdapter(adapter1);

                alertDialogBuilder.setTitle("UPDATE STATUS").setPositiveButton("OK",
                        new DialogInterface.OnClickListener()
                        {

                            public void onClick(DialogInterface dialog, int id)
                            {
                                progressDialog = new ProgressDialog(itemView.getContext());
                                progressDialog.setCancelable(false);

                                if(spinner4.getSelectedItem().toString().equals("ACTIVATE")) {
                                    status.setText("ACTIVE");
                                    unblockUser(token, name.getText().toString());

                                }

                                else if(spinner4.getSelectedItem().toString().equals("DEACTIVATE")) {
                                    status.setText("INACTIVE");
                                    blockUser(token, name.getText().toString());
                                }
                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                Toast.makeText(v.getContext(), "USER_LIST_ITEM", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void unblockUser(final String token, final String name)
    {
        String tag_string_req = "req_order";

        progressDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UNBLOCK, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                hideDialog();

                try {
                }

                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(itemView.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "login");
                params.put("token", token);
                params.put("id", name);
                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void blockUser(final String token, final String name)
    {
        String tag_string_req = "req_order";

        progressDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_BLOCK, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                hideDialog();

                try {
                }

                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(itemView.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "login");
                params.put("token", token);
                params.put("id", name);
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
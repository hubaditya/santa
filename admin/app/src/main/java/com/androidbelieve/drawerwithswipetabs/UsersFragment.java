package com.androidbelieve.drawerwithswipetabs;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsersFragment extends Fragment
{
    View rootview1;
    Button b1;
    private LinearLayoutManager lLayout;
    Spinner spinner,spinner2;
    ArrayList<String> mName, mStatus, mEmail, mPhn;
    UsersViewAdapter uvAdapter;
    EditText name, email, phn, pass, address, vownername, orderTime,timeofdel;
    String user, clg;
    private JSONClasses ob;
    private ProgressDialog progressDialog;
    RecyclerView rView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        rootview1 = inflater.inflate(R.layout.users_fragment,null);
        b1 = (Button) rootview1.findViewById(R.id.button3);
        spinner = (Spinner) rootview1.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(container.getContext(),
                R.array.santa_users_list, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);

        ob = AppController.getData(getActivity(), "token");
        final String token = ob.token;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usertype = spinner.getSelectedItem().toString();
                if (usertype.equals("CUSTOMER")) {
                    LayoutInflater li = LayoutInflater.from(v.getContext());
                    final View promptsView = li.inflate(R.layout.new_customer, null);

                    name = (EditText) promptsView.findViewById(R.id.editText);
                    email = (EditText) promptsView.findViewById(R.id.editText2);
                    phn = (EditText) promptsView.findViewById(R.id.editText3);
                    pass = (EditText) promptsView.findViewById(R.id.editText4);
                    spinner2 = (Spinner) promptsView.findViewById(R.id.spinner5);

                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(promptsView.getContext(),
                            R.array.JAIPUR_college_list, android.R.layout.simple_spinner_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adapter2);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                    alertDialogBuilder.setView(promptsView);

                    alertDialogBuilder.setTitle(usertype); //Set Alert dialog title here
                    alertDialogBuilder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            clg = spinner2.getSelectedItem().toString();

                            signUp(token, name.getText().toString(), email.getText().toString(),
                                    phn.getText().toString(), pass.getText().toString(), clg);
                        }
                    });
                    alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else if (usertype.equals("VENDOR")) {
                    LayoutInflater li = LayoutInflater.from(v.getContext());
                    final View promptsView = li.inflate(R.layout.new_vendor, null);

                    name = (EditText) promptsView.findViewById(R.id.editText10);
                    vownername = (EditText) promptsView.findViewById(R.id.editText11);
                    email = (EditText) promptsView.findViewById(R.id.editText12);
                    phn = (EditText) promptsView.findViewById(R.id.editText13);
                    pass = (EditText) promptsView.findViewById(R.id.editText14);
                    orderTime = (EditText) promptsView.findViewById(R.id.editText15);
                    address = (EditText) promptsView.findViewById(R.id.editText16);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                    alertDialogBuilder.setView(promptsView);
                    alertDialogBuilder.setTitle(usertype);

                    alertDialogBuilder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            addRest(token, name.getText().toString(), vownername.getText().toString(),
                                    email.getText().toString(), phn.getText().toString(),
                                    pass.getText().toString(), orderTime.getText().toString(),
                                    address.getText().toString());
                        }
                    });
                    alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else if (usertype.equals("DELIVERY BOY")) {
                    LayoutInflater li = LayoutInflater.from(v.getContext());
                    final View promptsView = li.inflate(R.layout.new_deliveryboy, null);

                    name = (EditText) promptsView.findViewById(R.id.editText5);
                    email = (EditText) promptsView.findViewById(R.id.editText6);
                    phn = (EditText) promptsView.findViewById(R.id.editText7);
                    pass = (EditText) promptsView.findViewById(R.id.editText8);
                    timeofdel = (EditText) promptsView.findViewById(R.id.editText9);
                    spinner2 = (Spinner) promptsView.findViewById(R.id.spinner6);

                    ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(promptsView.getContext(),
                            R.array.JAIPUR_college_list, android.R.layout.simple_spinner_item);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner2.setAdapter(adapter2);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                    alertDialogBuilder.setView(promptsView);
                    alertDialogBuilder.setTitle(usertype);

                    alertDialogBuilder.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            clg = spinner2.getSelectedItem().toString();

                            signUpDeliveryBoy(token, name.getText().toString(), email.getText().toString(),
                                    phn.getText().toString(), pass.getText().toString(),
                                    timeofdel.getText().toString(), clg);
                        }
                    });
                    alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
                    final AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(v.getContext(), "Please select user type and try again.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        lLayout = new LinearLayoutManager(rootview1.getContext());
        rView = (RecyclerView) rootview1.findViewById(R.id.usersList);
        rView.setLayoutManager(lLayout);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                user = spinner.getSelectedItem().toString();

                if (user.equals("CUSTOMER")) {
                    userList();
                }

                else if (user.equals("VENDOR")) {
                    vendorList();
                }

                else if (user.equals("DELIVERY BOY")) {
                    deliveryList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return rootview1;
    }

    public void signUp(final String token, final String name, final String email, final String phone,
                       final String password, final String clg)
    {
        Toast.makeText(getActivity(),"token: "+token,Toast.LENGTH_SHORT).show();

        String tag_string_req = "req_register";

        progressDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_USER_SIGNUP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    Toast.makeText(getActivity(),"created customer",Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "register");
                params.put("token", token);
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("password", password);
                params.put("college", clg);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void addRest(final String token, final String name, final String owner, final String email,
                         final String phone, final String password, final String time, final String address)
    {
        String tag_string_req = "req_register";

        progressDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_VENDOR_SIGNUP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "register");
                params.put("token", token);
                params.put("name", name);
                params.put("owner", owner);
                params.put("email", email);
                params.put("phone", phone);
                params.put("password", password);
                params.put("time", time);
                params.put("college", address);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void signUpDeliveryBoy(final String token, final String name, final String email,
                                   final String phone, final String password, final String time,
                                   final String clg)
    {
        String tag_string_req = "req_register";
        Toast.makeText(getActivity(),"token: "+token,Toast.LENGTH_SHORT).show();

        progressDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELIVERY_SIGNUP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    Toast.makeText(getActivity(),"created dboy",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "register");
                params.put("token", token);
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                params.put("password", password);
                params.put("timeOfDelivery", time);
                params.put("college", clg);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void userList()
    {
        mName = new ArrayList<>();
        mStatus = new ArrayList<>();
        mEmail = new ArrayList<>();
        mPhn = new ArrayList<>();

        String tag_string_req = "req_register";

        progressDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_USER_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try
                {
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i = 0; i < jsonArray.length(); i++)
                    {

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mName.add(jsonObject.getString("name"));
                        mStatus.add(jsonObject.getString("active"));
                        mEmail.add(jsonObject.getString("email"));
                        mPhn.add(jsonObject.getString("phone"));
                    }

                    uvAdapter = new UsersViewAdapter(rootview1.getContext(), mName, mStatus, mEmail, mPhn);
                    rView.setAdapter(uvAdapter);
                }

                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        error.getMessage()+"volley error", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void vendorList()
    {
        mName = new ArrayList<>();
        mStatus = new ArrayList<>();
        mEmail = new ArrayList<>();
        mPhn = new ArrayList<>();

        String tag_string_req = "req_register";

        progressDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_VENDOR_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try
                {
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mName.add(jsonObject.getString("name"));
                        mStatus.add(jsonObject.getString("active"));
                        mEmail.add(jsonObject.getString("email"));
                        mPhn.add(jsonObject.getString("phone"));
                    }

                    uvAdapter = new UsersViewAdapter(rootview1.getContext(), mName, mStatus, mEmail, mPhn);
                    rView.setAdapter(uvAdapter);
                }

                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        error.getMessage()+"volley error", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void deliveryList()
    {
        mName = new ArrayList<>();
        mStatus = new ArrayList<>();
        mEmail = new ArrayList<>();
        mPhn = new ArrayList<>();

        String tag_string_req = "req_register";

        progressDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_DELIVERY_LIST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try
                {
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        mName.add(jsonObject.getString("name"));
                        mStatus.add(jsonObject.getString("active"));
                        mEmail.add(jsonObject.getString("email"));
                        mPhn.add(jsonObject.getString("phone"));
                    }

                    uvAdapter = new UsersViewAdapter(rootview1.getContext(), mName, mStatus, mEmail, mPhn);
                    rView.setAdapter(uvAdapter);
                }

                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),
                        error.getMessage()+"volley error", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

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

}

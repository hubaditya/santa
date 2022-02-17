package com.androidbelieve.drawerwithswipetabs;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

public class MenuFragment extends Fragment
{
	View rootview;
	Button b1, b2;
    ListView listview;
    private ArrayList<String> cities, colleges, vendors, mNames, mPrices;
    private JSONArray jsonArray;
    Spinner citySpinner, clgSpinner, vdrSpinner;
    private ProgressDialog pDialog;
    private String city, clg, vendorName;

    @Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

		rootview = inflater.inflate(R.layout.menu_fragment, null);
		listview = (ListView) rootview.findViewById(R.id.listView);
        b1 = (Button) rootview.findViewById(R.id.button);
        b2 = (Button) rootview.findViewById(R.id.button7);

        mNames = new ArrayList<>();
        mPrices = new ArrayList<>();

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LayoutInflater li = LayoutInflater.from(container.getContext());
                View promptsView = li.inflate(R.layout.menu_location_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(container.getContext());
                alertDialogBuilder.setView(promptsView);
                alertDialogBuilder.setTitle("LOCATION");
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                citySpinner = (Spinner) promptsView.findViewById(R.id.spinner1);
                clgSpinner = (Spinner) promptsView.findViewById(R.id.spinner2);
                vdrSpinner = (Spinner) promptsView.findViewById(R.id.spinner3);

                final Button mButton = (Button) promptsView.findViewById(R.id.button2);

                pDialog = new ProgressDialog(getActivity());
                pDialog.setCancelable(false);
                //getCities();

                citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                    {
                        pDialog = new ProgressDialog(getActivity());
                        pDialog.setCancelable(false);
                        getColleges(citySpinner.getSelectedItem().toString());

                        clgSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
                        {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                            {
                                pDialog = new ProgressDialog(getActivity());
                                pDialog.setCancelable(false);
                                getVendors(citySpinner.getSelectedItem().toString(),
                                        clgSpinner.getSelectedItem().toString());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                mButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        listview.setAdapter(null);
                        mNames.clear();
                        mPrices.clear();
                        alertDialog.hide();

                        city = citySpinner.getSelectedItem().toString();
                        clg = clgSpinner.getSelectedItem().toString();
                        vendorName = vdrSpinner.getSelectedItem().toString();

                        getMenu(city, clg, vendorName);

                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
                            {

                            }
                        });
                    }
                });
            }
        });

        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LayoutInflater li = LayoutInflater.from(container.getContext());
                View promptsView = li.inflate(R.layout.item_add_dialog, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(container.getContext());
                alertDialogBuilder.setView(promptsView);
                alertDialogBuilder.setTitle("NEW DISH");
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                final EditText name = (EditText) promptsView.findViewById(R.id.editText18);
                final EditText rate = (EditText) promptsView.findViewById(R.id.editText19);
                final EditText type = (EditText) promptsView.findViewById(R.id.editText20);
                final Button mButton = (Button) promptsView.findViewById(R.id.button2);

                mButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        addDish(city, clg, vendorName, name.getText().toString(),
                                rate.getText().toString(), type.getText().toString());
                        alertDialog.hide();
                    }
                });
            }
        });

		return rootview;
	}

    /*public void getCities()
    {
        cities = new ArrayList<>();

        pDialog.setMessage("Loading ...");
        showDialog();

        String tag_string_req = "req_cities";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_CITY, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                try
                {
                    jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        String city = jsonobject.getString("name");
                        cities.add(city);
                    }

                    ArrayAdapter adapter1 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, cities);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(adapter1);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })

        {

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
*/
    public void getColleges(final String city)
    {
        colleges = new ArrayList<>();

        String tag_string_req = "req_colleges";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_COLLEGES, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                try
                {
                    jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        String college = jsonobject.getString("name");
                        colleges.add(college);
                    }

                    ArrayAdapter adapter2 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, colleges);
                    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(adapter2);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<>();
                params.put("tag", "colleges");
                params.put("city", city);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void getVendors(final String city, final String college)
    {
        vendors = new ArrayList<>();

        String tag_string_req = "req_vendors";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_VENDORS, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                try
                {
                    jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        String vendor = jsonobject.getString("name");
                        vendors.add(vendor);
                    }

                    ArrayAdapter adapter3 = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, colleges);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    citySpinner.setAdapter(adapter3);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<>();
                params.put("tag", "vendors");
                params.put("city", city);
                params.put("college", college);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void getMenu(final String city, final String clg, final String vendor)
    {
        vendors = new ArrayList<>();

        String tag_string_req = "req_menu";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_MENU, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                try
                {
                    jsonArray = new JSONArray(response);

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
                    listview.setAdapter(menuAdapter);

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
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
                params.put("college", clg);
                params.put("vendor", vendor);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void addDish(final String city, final String clg, final String vendor,
                        final String dish, final String price,final String type)
    {
        String tag_string_req = "req_add";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ADD, new Response.Listener<String>()
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
                        String vendor = jsonobject.getString("name");
                        vendors.add(vendor);
                    }*/
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
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<>();
                params.put("tag", "add");
                params.put("city", city);
                params.put("college", clg);
                params.put("vendor", vendor);
                params.put("dish", dish);
                params.put("price", price);
                params.put("type", type);
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
}
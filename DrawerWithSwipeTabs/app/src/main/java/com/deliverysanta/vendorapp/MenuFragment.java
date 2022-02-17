package com.deliverysanta.vendorapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class MenuFragment extends Fragment
{
	View rootview;
	JSONClasses ob;
	private ProgressDialog progressDialog;
	ListView listview;
	String tokenn;
	ArrayList<String> mNames;
	ArrayList<String> mPrices;
    ArrayList<String> mStatus;
    JSONClasses ob2;
    String vendor;

    @Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
		rootview=inflater.inflate(R.layout.menu_fragment,null);
        setHasOptionsMenu(true);

        listview =(ListView)rootview.findViewById(R.id.listView);

		ob = AppController.getList(getActivity(), "token");
		String token = ob.token;
		tokenn=token;

        ob2 = AppController.getList(getActivity(), "vendor");
        vendor = ob.vendorName;

		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setCancelable(false);

        listview.setBackgroundColor(getResources().getColor(R.color.white));

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id)
            {
                AlertDialog.Builder alert1 = new AlertDialog.Builder(view.getContext());
                alert1.setTitle(mNames.get(position));
                alert1.setMessage("Is this product available?");

                alert1.setPositiveButton("YES", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dishAvail(tokenn, mNames.get(position));
                        listview.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.white));
                    }
                });

                alert1.setNegativeButton("NO", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        dishUnavail(tokenn, mNames.get(position));
                        listview.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.red));
                    }
                });
                AlertDialog alertDialog1 = alert1.create();
                alertDialog1.show();
            }
        });


        getMenu();

        return rootview;
	}

    private void dishUnavail(final String tokenn, final String s) {
        String tag_string_req = "req_login";

        progressDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DISABLE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response)
            {
                hideDialog();

                try {
                    getMenu();
                }

                catch (Exception e) {
                    Toast.makeText(getContext(), "No or slow Internet connection. Please try again later.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No or slow Internet connection. Please try again later.", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "disable");
                params.put("token", tokenn);
                params.put("nameOfDish", s);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void dishAvail(final String tokenn, final String s) {
        String tag_string_req = "req_login";

        progressDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ENABLE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response)
            {
                hideDialog();

                try {
                    getMenu();
                }

                catch (Exception e) {
                    Toast.makeText(getContext(), "No or slow Internet connection. Please try again later.", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(getContext(), "No or slow Internet connection. Please try again later.", Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "enable");
                params.put("token", tokenn);
                params.put("nameOfDish", s);


                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public void getMenu()
	{
		mNames = new ArrayList<>();
		mPrices = new ArrayList<>();
        mStatus = new ArrayList<>();
        String tag_string_req = "req_order";

		progressDialog.setMessage("Loading ...");
		showDialog();

		StringRequest strReq = new StringRequest(Request.Method.POST,
				AppConfig.URL_MENU, new Response.Listener<String>()
        {

			@Override
			public void onResponse(String response)
			{
				hideDialog();

				try {
					JSONArray jsonArray = new JSONArray(response);

					for(int i = 0; i<jsonArray.length(); i++)
					{
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						String dish = jsonObject.getString("nameOfDish");
						String price = jsonObject.getString("priceOfDish");
                        String status = jsonObject.getString("dishStatus");

                        mNames.add(dish);
						mPrices.add(price);
                        mStatus.add(status);
					}

					VendorMenuAdapter menuAdapter=new VendorMenuAdapter(rootview.getContext(), mNames, mPrices, mStatus);
					listview.setAdapter(menuAdapter);
					//menuAdapter.notifyDataSetChanged();

                    /*for(int k=0; k<jsonArray.length(); k++){

                        if(mStatus.get(k).equals("1")){
                            listview.getChildAt(k).setBackgroundColor(getResources().getColor(R.color.white));
                        }
                        else {
                            listview.getChildAt(k).setBackgroundColor(getResources().getColor(R.color.red));
                        }
                    }*/
				}
				catch (Exception e) {
                    Toast.makeText(getContext(), "No or slow Internet connection. Please try again later.", Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No or slow Internet connection. Please try again later.", Toast.LENGTH_LONG).show();
				hideDialog();
			}
		}) {

			@Override
			protected Map<String, String> getParams()
			{
				Map<String, String> params = new HashMap<>();
				params.put("tag", "login");
                params.put("nameOfRestaurant", vendor);
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



    public void onRefresh() {
        getMenu();
        Snackbar.make(rootview, "List updated.", Snackbar.LENGTH_SHORT).show();
    }


	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		//menu.findItem(R.id.action_add).setVisible(true);
        menu.findItem(R.id.action_refresh).setVisible(true);
        super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar2, menu);
		//return true;
		//super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			/*case R.id.action_add:
				final AlertDialog.Builder dialog = new AlertDialog.Builder(this.getActivity());
				dialog.setView(R.layout.dish_add_box);
				dialog.setTitle("NEW DISH");
				dialog.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Toast.makeText(getActivity(), "UPDATE REQUESTED.", Toast.LENGTH_SHORT).show();
					}
				});

				dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				});
				AlertDialog alertDialog1 = dialog.create();
				alertDialog1.show();
				return true;
            */
			case R.id.action_refresh:
                onRefresh();
                return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

}

package delivery.santa.delivery.customer.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.florent37.materialviewpager.MaterialViewPagerHelper;
import com.github.florent37.materialviewpager.adapter.RecyclerViewMaterialAdapter;
import com.github.florent37.materialviewpager.sample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import delivery.santa.delivery.customer.AppConfig;
import delivery.santa.delivery.customer.AppController;
import delivery.santa.delivery.customer.DB;
import delivery.santa.delivery.customer.DishSearchFragment;
import delivery.santa.delivery.customer.DishViewAdapter;
import delivery.santa.delivery.customer.ExampleCategory;
import delivery.santa.delivery.customer.ExampleModel;
import delivery.santa.delivery.customer.ExamplePrice;
import delivery.santa.delivery.customer.MainActivity;

public class VendorFragment1 extends Fragment
{

    private static List<ExampleModel> mName;
    private static List<ExamplePrice> mPrice;
    private static List<ExampleCategory> mCat;

    String [] dishAll;
    String [] rate;
    String [] cat;
    public static TextView tvv1;
    private static String type;
    RecyclerView mRecyclerView;
    static String vendorName;
    private DB db;
    RecyclerView.Adapter mAdapter;
    DishViewAdapter mAdapter1;
    private ProgressDialog pDialog;
    private JSONArray jsonArray;

    public static VendorFragment1 newInstance(String vendorname, String typeOfDish)
    {
        vendorName=vendorname;
        type = typeOfDish;
        return new VendorFragment1();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_vendor, container, false);
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(rootView, savedInstanceState);

        mRecyclerView = (RecyclerView)rootView.findViewById(R.id.listView1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        findMenuByType(vendorName, type);
        findMenu(vendorName);
    }

    public void findMenu(final String vendorName)
    {
        String tag_string_req = "req_vendor";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_FULLMENU, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {

                hideDialog();

                try
                {
                    jsonArray = new JSONArray(response);

                    dishAll = new String[jsonArray.length()];
                    rate = new String[jsonArray.length()];
                    cat = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);

                        String dish = jsonobject.getString("nameOfDish");
                        String price = jsonobject.getString("priceOfDish");
                        String catt="";
                        if(jsonobject.has("vegOrNonVeg"))
                            catt = "NonVeg";

                        else catt="Veg";

                        dishAll[i] = dish;
                        rate[i] = price;
                        cat[i] = catt;
                    }
                }

                catch (JSONException e) {
                    //Toast.makeText(getActivity(), "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                //Toast.makeText(getActivity(), "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })

        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "register");
                params.put("nameOfRestaurant", vendorName);

                return params;
            }

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void findMenuByType(final String vendorName, final String type)
    {
        mName = new ArrayList<>();
        mPrice = new ArrayList<>();
        mCat = new ArrayList<>();

        String tag_string_req = "req_vendor";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_MENU, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {

                hideDialog();

                try
                {
                    jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        String dish = jsonobject.getString("nameOfDish");
                        String price = jsonobject.getString("priceOfDish");
                        String catt="";
                        if(jsonobject.has("vegOrNonVeg"))
                            catt = "NonVeg";

                        else catt="Veg";

                        mName.add(new ExampleModel(dish));
                        mPrice.add(new ExamplePrice(price));
                        mCat.add(new ExampleCategory(catt));
                    }

                    db = new DB(getActivity());
                    tvv1 = MainActivity.tvf;

                    mAdapter1 = new DishViewAdapter(getContext(), db, mName, mPrice,vendorName,tvv1,mCat);
                    mAdapter = new RecyclerViewMaterialAdapter(mAdapter1);
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();

                    MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);
                }
                catch (JSONException e) {
                    //Toast.makeText(getActivity(), "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                //Toast.makeText(getActivity(), "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })

        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "register");
                params.put("restaurant", vendorName);
                params.put("typeOfDish", type);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.menu_vendors, menu);
        //super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.search:
                //Toast.makeText(getActivity(), ""+dishAll[1], Toast.LENGTH_LONG).show();
                Intent intent=new Intent(this.getActivity(), DishSearchFragment.class);
                intent.putExtra("vname",vendorName);
                intent.putExtra("dishList", dishAll);
                intent.putExtra("rate", rate);
                intent.putExtra("cat", cat);
                startActivity(intent);
                break;
            default:
                return false;
        }

        return false;
    }
    public void onResume()
    {
        mAdapter1 = new DishViewAdapter(getContext(), db, mName, mPrice,vendorName,tvv1,mCat);
        mAdapter = new RecyclerViewMaterialAdapter(mAdapter1);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        MaterialViewPagerHelper.registerRecyclerView(getActivity(), mRecyclerView, null);

        super.onResume();
    }
}

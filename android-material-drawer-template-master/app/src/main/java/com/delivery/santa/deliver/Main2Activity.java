package com.delivery.santa.deliver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.List;
import java.util.Map;

public class Main2Activity extends AppCompatActivity implements NavigationDrawerCallbacks
{
    private Toolbar mToolbar;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    ListView listview;
    List<String> vendorList, amountList;
    static List<String> doneVenList;
    JSONClasses ob;
    private ProgressDialog progressDialog;
    String token;
    SessionManager session;
    public static Main2Activity act;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        act = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_drawer);
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        //db = new DBAdapter(getApplicationContext());

        ob = AppController.getList(getApplicationContext(), "token");
        token = ob.token;

        /*Bundle bundle=getIntent().getExtras();
        if(bundle == null){
            doneVenList = new ArrayList<>();
        }
        else {
            doneVenList.add(bundle.getString("doneVendor"));
        }*/

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        getRest(token);

        session  = new SessionManager(getApplicationContext());

    }

    public void getRest(final String token)
    {
        vendorList = new ArrayList<>();
        amountList = new ArrayList<>();

        String tag_string_req = "req_list";
        progressDialog.setMessage("Loading ...");

        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_VENDORS, new Response.Listener<String>()
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
                        String vName = jsonObject.getString("restaurant");

                        /*if(doneVenList.contains(vName));

                        else {
                             vendorList.add(vName);
                         }*/

                        vendorList.add(vName);
                    }

                    listview=(ListView)findViewById(R.id.listView1);
                    HomeAdapter adapter = new HomeAdapter(Main2Activity.this, (ArrayList<String>) vendorList);
                    listview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No or slow Internet connection. Please try again later.", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(), "No or slow Internet connection. Please try again later.", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "restlist");
                params.put("token", token);
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
        getRest(token);
        Snackbar.make(this.findViewById(R.id.main_layout), "List updated.", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        //Toast.makeText(this, "Menu item selected -> " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed()
    {

        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();

        else if(session.isLoggedIn())
        {
            //db.deleteData();
            System.exit(0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_clg:

            /*if(vendorList.size()==0)
            {
                Intent i=new Intent(Main2Activity.this,CollegeActivity.class);
                i.putExtra("clgName","LNMIIT");
                startActivity(i);
            }
                else{
                Toast.makeText(Main2Activity.this,"Vendors still remaining.",Toast.LENGTH_LONG).show();
            }*/

                if(vendorList.size() != 0)
                {
                    Intent i=new Intent(Main2Activity.this,CollegeActivity.class);
                    i.putExtra("clgName","LNMIIT");
                    startActivity(i);
                }

                else Toast.makeText(Main2Activity.this,"No orders.",Toast.LENGTH_LONG).show();

                return true;

            case R.id.menu_search:
                onRefresh();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }
}

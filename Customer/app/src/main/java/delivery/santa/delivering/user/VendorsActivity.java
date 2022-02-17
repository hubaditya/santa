package delivery.santa.delivery.customer;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.florent37.materialviewpager.sample.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendorsActivity extends AppCompatActivity
{
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<String> dataList,statList, contactList;
    SessionManager session;
    private List<String> dataItems;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private ListView drawerListView;
    private JSONClasses ob1,ob2;
    private TextView tv1,tv2;
    private String[] drawerListViewItems;
    private ProgressDialog pDialog;
    private DB db;
    String college;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors);

        db=new DB(VendorsActivity.this);
        db.deleteAllItems();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.abc_primary_text_disable_only_material_light));
        }

        byte b[]={0};

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            college = bundle.getString("collegeName");
        }
        recyclerView = (RecyclerView) findViewById(R.id.vendorView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        drawerListViewItems= new String[]{"My Orders", "College", "T&C", "Privacy Policy", "About Us", "Log Out"};
        drawerListView = (ListView) findViewById(R.id.leftdrawer);
        List<String> dataTemp = Arrays.asList(drawerListViewItems);
        dataItems = new ArrayList<>();
        dataItems.addAll(dataTemp);
        DrawerAdapter adapter=new DrawerAdapter(this, (ArrayList<String>) dataItems, "0", b, "", college, "");
        drawerListView.setAdapter(adapter);

        session = new SessionManager(getApplicationContext());

        toolbar = (Toolbar)findViewById(R.id.vact_toolbar);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (toolbar != null)
        {
            setSupportActionBar(toolbar);

            final ActionBar actionBar = getSupportActionBar();
            if (actionBar != null)
            {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setDisplayUseLogoEnabled(true);
                actionBar.setHomeButtonEnabled(true);
                //actionBar.setSlidingActionBarEnabled(true);
            }
        }

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0){

            public void onDrawerClosed(View view)
            {
                supportInvalidateOptionsMenu();
                //drawerOpened = false;
            }

            public void onDrawerOpened(View drawerView)
            {
                supportInvalidateOptionsMenu();
                //drawerOpened = true;
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawer.setDrawerListener(mDrawerToggle);

        drawerListView.setOnItemClickListener(new DrawerItemClickListener());

        tv1=(TextView)findViewById(R.id.txtUsername);
        tv2=(TextView)findViewById(R.id.txtUserEmail);
        session = new SessionManager(getApplicationContext());

        if(session.isLoggedIn())
        {
            ob1 = AppController.getList(getApplicationContext(),"name");
            ob2 = AppController.getList(getApplicationContext(),"email");

            tv1.setText(ob1.name);
            tv2.setText(ob2.email);
        }

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        findVendor(college);
    }

    private void findVendor(final String collegeName)
    {
        dataList = new ArrayList<>();
        statList = new ArrayList<>();
        contactList = new ArrayList<>();

        String tag_string_req = "req_vendor";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_VENDOR, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                hideDialog();

                try
                {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonobject = jsonArray.getJSONObject(i);
                        String vendor = jsonobject.getString("nameOfRestaurant");
                        String contact = jsonobject.getString("contactNo");
                        String vstat = jsonobject.getString("status");

                        if(vstat.equals("1")){
                            vstat="Available";
                        }
                        else if(vstat.equals("0")){
                            vstat="Unavailable";
                        }

                        dataList.add(vendor);
                        statList.add(vstat);
                        contactList.add(contact);
                    }

                    adapter = new VendorsAdapter((ArrayList) dataList,(ArrayList) statList, (ArrayList) contactList, collegeName);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(), "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })

        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "register");
                params.put("college", collegeName);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
     public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId())
    {
        case R.id.cart:
            Intent intent = new Intent(this,CartActivity.class);
            startActivity(intent);
            finish();
            return true;
        default:
            return  mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed()
    {
        if(mDrawer.isDrawerOpen(Gravity.LEFT)){
            mDrawer.closeDrawer(Gravity.LEFT);
        }

        else if(session.isLoggedIn()) {
            finish();
        }

        else
        {
            Intent intent = new Intent(VendorsActivity.this, CollegeActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }
}
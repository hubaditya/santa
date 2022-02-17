package delivery.santa.delivery.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

public class CollegeActivity extends AppCompatActivity implements View.OnClickListener
{

    AutoCompleteTextView college;
    TextView b,b1,b2;
    List<String> colleges;
    private ProgressDialog pDialog;
    JSONArray jsonArray;
    SessionManager session;
    private JSONClasses ob;
    private List<String> dataItems;
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar toolbar;
    private ListView drawerListView;
    private String[] drawerListViewItems;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.abc_primary_text_disable_only_material_light));
        }

        byte bytes[]={0};;

        college = (AutoCompleteTextView) findViewById(R.id.college);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        getColleges();

        drawerListViewItems= new String[]{"College", "T&C", "Privacy Policy", "About Us"};
        drawerListView = (ListView) findViewById(R.id.leftdrawer);
        List<String> dataTemp = Arrays.asList(drawerListViewItems);
        dataItems = new ArrayList<>();
        dataItems.addAll(dataTemp);
        DrawerAdapter adapter=new DrawerAdapter(this, (ArrayList<String>) dataItems, "2", bytes, "", "", "");
        drawerListView.setAdapter(adapter);

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

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, 0, 0)
        {
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

        b = (TextView) findViewById(R.id.cllgbtn);
        b.setOnClickListener(this);
        b1 = (TextView)findViewById(R.id.button6); //login
        b2 = (TextView)findViewById(R.id.button7); //register
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CollegeActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CollegeActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });

        session = new SessionManager(getApplicationContext());


        if (session.isLoggedIn())
        {
            ob = AppController.getList(getApplicationContext(), "token");
            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);
            getMyCollege(ob.token);
        }
    }

    public void getMyCollege(final String token)
    {
        ob = new JSONClasses();

        String tag_string_req = "req_colleges";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ME, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {

                try
                {
                    JSONObject jsonobject = new JSONObject(response);

                    String college = jsonobject.getString("college");
                    ob.name = jsonobject.getString("name");
                    ob.email = jsonobject.getString("email");

                    AppController.setList(getApplicationContext(), "name", ob);
                    AppController.setList(getApplicationContext(), "email", ob);

                    Intent intent = new Intent(CollegeActivity.this, VendorsActivity.class);
                    intent.putExtra("collegeName", college);
                    startActivity(intent);
                    finish();

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
                params.put("token", token);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    @Override
    public void onClick(View v)
    {
        String name = college.getText().toString();

        if(colleges.contains(name))
        {
            Intent intent=new Intent(CollegeActivity.this, VendorsActivity.class);
            intent.putExtra("collegeName", name);
            startActivity(intent);
            finish();
        }
        else if(name.equals("")){
            Toast.makeText(this,"Please enter the name of your college.",Toast.LENGTH_LONG).show();
        }
        else
            Toast.makeText(this,"Service does not exist in your college.",Toast.LENGTH_LONG).show();
    }

    public void getColleges()
    {
        colleges = new ArrayList<>();

        String tag_string_req = "req_colleges";

        StringRequest strReq = new StringRequest(Request.Method.GET,
                AppConfig.URL_COLLEGE, new Response.Listener<String>()
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

                    ArrayAdapter adapter = new ArrayAdapter(CollegeActivity.this, android.R.layout.select_dialog_item, colleges);
                    college.setAdapter(adapter);

                }
                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "No or slow Internet connection. Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(), "No or slow Internet connection. Please try again later.", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        })

        {

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

    /*public void onBackPressed()
    {
        System.exit(0);
    }*/

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id)
        {

        }
    }
}

package delivery.santa.delivery.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrdersActivity extends AppCompatActivity
{
    private List<String> statList;
    private SessionManager session;
    private ArrayList<String> names;
    private ArrayList<String> qtys;
    private ArrayList<String> orders;
    private ArrayList<String> amounts;
    private ArrayList<String> dates;
    RecyclerView rView;
    List<Component> dishList;
    private LinearLayoutManager lLayout;
    RecyclerViewAdapter rcAdapter;
    private ProgressDialog progressDialog;
    List<List<Component>> temp;
    private ArrayList<String> delP;
    private int del;
    private String collegeName, vendorName, contactNo;
    private Toolbar toolbar;
    private String check;
    byte b[];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        setTitle("  MY ORDERS");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.abc_primary_text_disable_only_material_light));
        }

        toolbar = (Toolbar)findViewById(R.id.vact_toolbar);

        lLayout = new LinearLayoutManager(MyOrdersActivity.this,LinearLayoutManager.VERTICAL,false);
        rView = (RecyclerView)findViewById(R.id.myOrdersList);
        rView.setLayoutManager(lLayout);

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

        Bundle bundle = getIntent().getExtras();

        if(bundle != null)
        {
            check = bundle.getString("check");
            b = bundle.getByteArray("vendorImage");
            vendorName = bundle.getString("vendorName");
            collegeName = bundle.getString("collegeName");
            contactNo = bundle.getString("contact");
        }

        JSONClasses ob;

        String token = null;

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn())
        {
            ob=AppController.getList(getApplicationContext(),"token");
            token=ob.token;
            delP=new ArrayList<>();
            getDelP(token);
        }

        else
        {
            Intent i = new Intent(MyOrdersActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void getDelP(final String token)
    {
        String tag_string_req = "req_vendor";

        progressDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_MYORDERS, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                hideDialog();

                try
                {
                    JSONArray jsonArray = new JSONArray(response);

                    if(jsonArray.length()==0) {
                        Toast.makeText(getApplicationContext(), "You have no previous orders.", Toast.LENGTH_LONG).show();
                        getMyCollege(token);
                    }

                    else
                    {
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject jsonobject = jsonArray.getJSONObject(i);
                            String amount1 = jsonobject.getString("totalAmount");
                            if(i == jsonArray.length()-1)
                                getDelPrice(amount1, 1, token);
                            else getDelPrice(amount1, 0, token);
                        }
                    }

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
                params.put("tag", "myorders");
                params.put("token", token);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void getMyCollege(final String token)
    {

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
                    collegeName = jsonobject.getString("college");
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

    public void getMyOrders(final String token)
    {
        statList = new ArrayList<>();
        temp = new ArrayList<>();
        orders = new ArrayList<>();
        amounts = new ArrayList<>();
        dates = new ArrayList<>();

        String tag_string_req = "req_vendor";

        progressDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_MYORDERS, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                hideDialog();

                try
                {
                    JSONArray jsonArray = new JSONArray(response);

                    if(jsonArray.length()==0) {
                        Toast.makeText(getApplicationContext(), "You have no previous orders.", Toast.LENGTH_LONG).show();
                    }

                    else
                    {
                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            dishList = new ArrayList<>();
                            names = new ArrayList<>();
                            qtys = new ArrayList<>();

                            JSONObject jsonobject = jsonArray.getJSONObject(i);

                            String dish = jsonobject.getString("nameOfDish");
                            String qty = jsonobject.getString("quantityOfDish");
                            String stat = jsonobject.getString("status");
                            String amount1 = jsonobject.getString("totalAmount");
                            String orderId = jsonobject.getString("orderNo");
                            String date = jsonobject.getString("date");
                            date = date.substring(6, date.length()) + "/" + date.substring(4, 6)+ "/" + date.substring(0,4);
                            String amount = delP.get(i);

                            if(stat.equals("0")){
                                stat="Accepted";
                            }

                            else if(stat.equals("1")) {
                                stat = "Delivered";
                            }

                            else if(stat.equals("-3")) {
                                stat = "Rejected";
                            }

                            else {
                                stat="Not yet confirmed";
                            }

                            int le=0;

                            for (String retdish: dish.split("--"))
                                names.add(retdish);
                            for (String retqty: qty.split("--"))
                                qtys.add(retqty);
                            for(le=0;le<names.size();le++)
                                dishList.add(new Component(names.get(le),qtys.get(le)));

                            temp.add(dishList);
                            statList.add(stat);
                            orders.add(orderId);
                            amounts.add(String.valueOf(Integer.parseInt(amount) + Integer.parseInt(amount1)));
                            dates.add(date);


                        }

                        List<ModelOrder> modelOrderList = new ArrayList<>();

                        for(int i=0;i<orders.size();i++)
                        {
                            ModelOrder temp2 = new ModelOrder().initialize(orders.get(i), temp.get(i),
                                    amounts.get(i), dates.get(i), statList.get(i));
                            modelOrderList.add(temp2);
                        }

                        ModelOrder modelOrder = new ModelOrder((ArrayList<ModelOrder>) modelOrderList);
                        rcAdapter = new RecyclerViewAdapter(getBaseContext(), modelOrder.mIngredients);
                        rView.setAdapter(rcAdapter);
                        rcAdapter.notifyDataSetChanged();
                    }

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
                params.put("tag", "myorders");
                params.put("token", token);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void getDelPrice(final String sub, final int x, final String token)
    {
        del=0;
        String tag_string_req = "req_vendor";
        progressDialog.setMessage("Loading ...");
        showDialog();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELIVPRICE, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
                hideDialog();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String dp=jsonObject.getString("deliveryprice");
                    Double dd=Double.parseDouble(dp);
                    del=(int) Math.round(dd);
                    delP.add(String.valueOf(del));
                   }

                catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
                }

                if(x == 1)
                    getMyOrders(token);
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
                params.put("tag", "myorders");
                params.put("totalAmount", sub);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_orders, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        if(check.equals("0"))
        {
            Intent i = new Intent(MyOrdersActivity.this, VendorsActivity.class);
            i.putExtra("collegeName", collegeName);
            startActivity(i);
            finish();
        }

        else
        {
            Intent i = new Intent(MyOrdersActivity.this, MainActivity.class);
            i.putExtra("collegeName", collegeName);
            i.putExtra("vendorImage", b);
            i.putExtra("vendorName", vendorName);
            i.putExtra("contact", contactNo);
            startActivity(i);
            finish();
        }
    }
}

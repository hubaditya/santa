package delivery.santa.delivery.customer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity
{

    ListView lv1;
    TextView b1;
    TextView et1;
    String inst;
    TextView tv1,tv2,tv3;
    Spinner spin;
    private int delP;
    int i;
    String vendor, contactNo, tod, amount, deliv, totalamount, collegeName;
    int sub = 0, del = 0, tot = 0;
    private ProgressDialog progressDialog;

    DB db;
    Cursor cursor;
    CartViewHolder listAdapter;

    private ArrayList<String> name, deltimes;
    private ArrayList<Integer> qty;
    private ArrayList<Integer> price;
    private ArrayList<Integer> amt;
    private ArrayList<Integer> id;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setTitle("CART");
        setContentView(R.layout.activity_cart);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.abc_primary_text_disable_only_material_light));
        }


        Bundle bundle = getIntent().getExtras();
        vendor=bundle.getString("vendor");
        contactNo = bundle.getString("contact");
        collegeName = bundle.getString("collegeName");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv1 = (ListView) findViewById(R.id.cartListView);
        spin=(Spinner)findViewById(R.id.spinner2); // time of delivery
        b1 = (TextView) findViewById(R.id.button3); //ORDER
        et1 = (TextView) findViewById(R.id.inst1); //instruction
        tv1 = (TextView) findViewById(R.id.cart11); //subtotal
        tv2 = (TextView) findViewById(R.id.cart21); //delivery
        tv3 = (TextView) findViewById(R.id.cart31); //amount
        toolbar = (Toolbar)findViewById(R.id.vact_toolbar);
        inst="";

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

        deltimes = new ArrayList<>();
        deltimes.add("Delivery Time");
        progressDialog = new ProgressDialog(CartActivity.this);
        progressDialog.setCancelable(false);

        tv1.setText(String.valueOf(sub));
        tv2.setText(String.valueOf(del));
        tv3.setText(String.valueOf(tot));

        getDelTimes();


        id = new ArrayList<>();
        name = new ArrayList<>();
        qty = new ArrayList<>();
        price = new ArrayList<>();
        amt = new ArrayList<>();

        et1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(CartActivity.this);
                alert.setTitle("Special Instructions");
                alert.setMessage("Enter your request");
                final EditText input = new EditText(CartActivity.this);
                input.setTextColor(Color.BLACK);
                input.setText(inst);
                alert.setView(input);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String srt = input.getEditableText().toString();
                        inst = srt;
                    }
                });
                alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        db = new DB(this);
        listAdapter = new CartViewHolder(this, db, name, qty, price, amt, id, tv1, tv2, tv3);
        lv1.setAdapter(listAdapter);

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(CartActivity.this);
                alert.setTitle("Confirm Your Order");
                alert.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if(db.getAllItems().moveToFirst())
                        {
                            tod=spin.getSelectedItem().toString();
                            if(tod.equals("Delivery Time")){
                                Toast.makeText(CartActivity.this,"Please select a delivery time",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                amount=tv1.getText().toString();
                                deliv=tv2.getText().toString();
                                totalamount=tv3.getText().toString();
                                Intent intent = new Intent(CartActivity.this, LoginActivity.class);
                                intent.putExtra("tod",tod);
                                intent.putExtra("amount",amount);
                                intent.putExtra("deliv",deliv);
                                intent.putExtra("totalamount",totalamount);
                                intent.putExtra("specialInst",inst);
                                intent.putExtra("contact", contactNo);
                                intent.putExtra("collegeName", collegeName);
                                startActivity(intent);
                            }
                        }
                        else Toast.makeText(CartActivity.this, "No orders to be placed.", Toast.LENGTH_LONG).show();
                    }
                });
                alert.setNegativeButton("GO BACK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alert.create();
                alertDialog.show();

            }
        });
    }


    @Override
    protected void onResume()
    {
        db = new DB(this);
        cursor = null;
        sub = del = tot = 0;

        id.clear();
        name.clear();
        qty.clear();
        price.clear();
        amt.clear();

        cursor = db.getAllItems();

        if (cursor.moveToFirst())
        {
            do{

                id.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
                name.add(cursor.getString(cursor.getColumnIndex("name")));
                qty.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex("qty"))));
                price.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex("price"))));
                amt.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex("amount"))));

            }while(cursor.moveToNext());
        }

        for(i = 0; i < amt.size(); i++)
        { sub += amt.get(i); }

        getDelPrice(String.valueOf(sub));


        cursor.close();

        listAdapter = new CartViewHolder(this, db, name, qty, price, amt, id, tv1, tv2, tv3);
        lv1.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

        super.onResume();
    }


    public void getDelTimes() {
        String tag_string_req = "req_vendor";
        progressDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELIVTIME, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                hideDialog();
                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String dt = jsonObject.getString("deliveryTime");
                        String message = jsonObject.getString("message");

                        if (message.equals("Time Available"))
                            deltimes.add(dt);
                    }

                    if(deltimes.size() == 1)
                        Toast.makeText(getApplicationContext(), "No delivery slots at this time. Please order next time.",
                                Toast.LENGTH_LONG).show();

                    String[] items = deltimes.toArray(new String[deltimes.size()]);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CartActivity.this, android.R.layout.simple_spinner_dropdown_item, items);
                    spin.setAdapter(adapter);
                }

                catch (Exception e) {
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
                params.put("tag", "deltime");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public void getDelPrice(final String sub) {
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
                    delP=(int) Math.round(dd);
                    del=delP;
                    tot=Integer.parseInt(sub)+del;
                    tv1.setText(String.valueOf(sub));
                    tv2.setText(String.valueOf(del));
                    tv3.setText(String.valueOf(tot));

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
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.cartClear:

                sub = del = tot = 0;
                db.deleteAllItems();

                id.clear();
                name.clear();
                qty.clear();
                price.clear();
                amt.clear();

                tv1.setText(String.valueOf(sub));
                tv2.setText(String.valueOf(del));
                tv3.setText(String.valueOf(tot));

                listAdapter = new CartViewHolder(this, db, name, qty, price, amt, id, tv1, tv2, tv3);
                lv1.setAdapter(listAdapter);
                listAdapter.notifyDataSetChanged();

                return true;

            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
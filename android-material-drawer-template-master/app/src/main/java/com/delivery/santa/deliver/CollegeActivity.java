package com.delivery.santa.deliver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.List;
import java.util.Map;

public class CollegeActivity extends AppCompatActivity
{
    TextView tv1;
    Button b1;
    ordListAdapter ordListAdapter;
    RecyclerView rView;
    private SessionManager session;
    DBAdapter db1;
    private LinearLayoutManager lLayout;
    TextView total;
    private ProgressDialog progressDialog;
    private List<String> ordNo, cusIds, Bags, contact, code, finalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college);

        b1=(Button)findViewById(R.id.button2);
        total = (TextView) findViewById(R.id.textView18);

        rView = (RecyclerView)findViewById(R.id.ordList);
        lLayout=new LinearLayoutManager(this);

        session = new SessionManager(CollegeActivity.this);

        Bundle bundle=getIntent().getExtras();
        String clg=bundle.getString("clgName");

        tv1=(TextView)findViewById(R.id.textclg);
        tv1.setText(clg);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        giveOrder();

        /*db1=new DBAdapter(this);

        Cursor cursor = db1.fetchData();

        if (cursor.moveToFirst())
        {
            do {
                sum+=Integer.parseInt(cursor.getString(cursor.getColumnIndex("bill")));
            }while (cursor.moveToNext());
        }*/

        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //db1.deleteData();
                session.setLogin(false);
                Intent intent=new Intent(CollegeActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
                Main2Activity.act.finish();
            }
        });
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

    public void giveOrder()
    {
        ordNo=new ArrayList<>();
        cusIds=new ArrayList<>();
        Bags=new ArrayList<>();
        contact = new ArrayList<>();
        code = new ArrayList<>();
        finalAmount = new ArrayList<>();

        String tag_string_req = "req_list";
        progressDialog.setMessage("Loading ...");

        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_GIVE, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                hideDialog();

                try {

                    int sum = 0;

                    JSONArray jsonArray = new JSONArray(response);

                    for(int i = 0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        ordNo.add(jsonObject.getString("orderNo"));
                        cusIds.add(jsonObject.getString("orderBy"));
                        Bags.add(jsonObject.getString("bags"));
                        contact.add(jsonObject.getString("userContactNo"));
                        code.add(jsonObject.getString("uniqueCode"));
                        finalAmount.add(jsonObject.getString("finalAmount"));

                        sum += Integer.parseInt(jsonObject.getString("finalAmount"));

                    }

                    total.setText(String.valueOf(sum));

                    ordListAdapter = new ordListAdapter(CollegeActivity.this, ordNo, cusIds, Bags, contact, code, finalAmount);
                    rView.setLayoutManager(lLayout);
                    rView.setAdapter(ordListAdapter);
                    ordListAdapter.notifyDataSetChanged();
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
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_college, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

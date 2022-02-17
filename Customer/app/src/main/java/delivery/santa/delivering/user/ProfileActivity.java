package delivery.santa.delivery.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.florent37.materialviewpager.sample.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity
{
    TextView orderId;
    Button btnLogout;
    DB db;
    private SessionManager session;
    private JSONClasses ob;
    private ProgressDialog pDialog;
    private String instr, contactNo;
    private String college;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.abc_primary_text_disable_only_material_light));
        }

        Bundle extras = getIntent().getExtras();
        String tod,amount;

        tod = extras.getString("tod");
        amount=extras.getString("amount"); //textview 27

        instr = extras.getString("specialInst");
        contactNo = extras.getString("contact");

        orderId = (TextView) findViewById(R.id.textView14);

        btnLogout = (Button) findViewById(R.id.btnLogout);
        Toolbar toolBar = (Toolbar) findViewById(R.id.include);
        setSupportActionBar(toolBar);

        ob = AppController.getList(getApplicationContext(), "token");
        String token = ob.token;

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        String dish = "";
        String qty="";
        String vendorName = "";

        db = new DB(this);
        Cursor cursor = db.getAllItems();
        if (cursor.moveToFirst())
        {
            do {
                vendorName = (cursor.getString(cursor.getColumnIndex("vendor"))).toUpperCase();
                dish = dish + cursor.getString(cursor.getColumnIndex("name")) + "--";
                qty = qty + cursor.getString(cursor.getColumnIndex("qty")) + "--";
            }while (cursor.moveToNext());
        }

        //Toast.makeText(getApplicationContext(),vendorName+dish+qty+amount+tod+contactNo+instr,Toast.LENGTH_LONG).show();

        addOrder(token, vendorName, dish, qty, amount, tod, contactNo, instr);
        
        btnLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MyOrdersActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("check", "0");
                intent.putExtra("collegeName", college);
                startActivity(intent);
                finish();
            }
        });
    }

    public void addOrder(final String token, final String vName, final String dish, final String qty, final String amt, final String tod, final String contactNo, final String instr)
    {
        String tag_string_req = "req_order";

        pDialog.setMessage("Loading ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ORDER, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response)
            {
                hideDialog();
                try
                {
                    JSONObject jsonobject = new JSONObject(response);
                    String id = jsonobject.getString("orderNo");
                    college = jsonobject.getString("college");
                    orderId.setText(id);
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
                params.put("tag", "order");
                params.put("token", token);
                params.put("restaurant", vName);
                params.put("nameOfDish", dish);
                params.put("quantityOfDish",qty);
                params.put("totalAmount",amt);
                params.put("timeOfDelivery",tod);
                params.put("contactNo", contactNo);
                params.put("specialInstruction", instr);
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
    public void onBackPressed()
    {
        Intent i = new Intent(ProfileActivity.this, CollegeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }
}
package delivery.santa.delivery.customer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener
{
    String email, phn, srt, password, college, name, message;

    TextInputLayout emailRegister, passwordRegister, phnRegister,nameRegister;
    EditText etEmailRegister, etPasswordRegister, etPhnRegister,etNameRegister;
    Spinner etCollege;
    String tod,amount,deliv,totalamount,collegeName;
    private int st=0,stat=0;
    List<String> colleges;
    JSONArray jsonArray;
    TextView registerButton;
    private String inst, contactNo;
    private Bundle extras;
    final JSONClasses ob =new JSONClasses();

    SessionManager session;

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.abc_primary_text_disable_only_material_light));
        }

        registerButton = (TextView) findViewById(R.id.register_button);
        emailRegister = (TextInputLayout) findViewById(R.id.email_registerlayout);
        passwordRegister = (TextInputLayout) findViewById(R.id.password_registerlayout);
        phnRegister = (TextInputLayout) findViewById(R.id.phn_registerlayout);
        nameRegister = (TextInputLayout) findViewById(R.id.name_registerlayout);

        etEmailRegister = (EditText) findViewById(R.id.email_register);
        etPasswordRegister = (EditText) findViewById(R.id.password_register);
        etPhnRegister = (EditText) findViewById(R.id.phn_register);
        etNameRegister = (EditText) findViewById(R.id.name_register);

        etCollege = (Spinner) findViewById(R.id.collegename);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        getColleges();

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        registerButton.setOnClickListener(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        extras = getIntent().getExtras();
        if(extras!=null)
        {
            tod = extras.getString("tod");
            amount=extras.getString("amount");
            deliv=extras.getString("deliv");
            totalamount=extras.getString("totalamount");
            inst=extras.getString("specialInst");
            contactNo = extras.getString("contact");
            collegeName = extras.getString("collegeName");
            st=1;
        }
    }


    public void getColleges()
    {
        colleges = new ArrayList<>();
        colleges.add("COLLEGE");
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

                    ArrayAdapter adapter = new ArrayAdapter(RegisterActivity.this, R.layout.spinner_item, colleges);
                    adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    etCollege.setAdapter(adapter);

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

        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public void registerUser(final String email, final String password, final String college, final String phn, final String name)
    {

        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                hideDialog();
                try {

                    JSONObject jObj = new JSONObject(response);
                    message = jObj.getString("message");

                    if (message.equals("User created"))
                    {
                        ob.token = jObj.getString("token");
                        ob.college = college;

                        AppController.setList(RegisterActivity.this, "token", ob);
                        AppController.setList(RegisterActivity.this, "college", ob);

                        AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                        alert.setTitle("Verification");
                        alert.setMessage("Enter OTP");
                        final EditText otp = new EditText(RegisterActivity.this);
                        otp.setInputType(InputType.TYPE_CLASS_NUMBER);
                        otp.setRawInputType(Configuration.KEYBOARD_12KEY);
                        otp.setTextColor(Color.BLACK);
                        alert.setView(otp);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                srt = otp.getEditableText().toString();
                                verifyOtp(phn, srt, email);
                            }
                        });
                        /*alert.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });*/
                        final AlertDialog alertDialog = alert.create();
                        alertDialog.show();
                    }

                    else if(message.equals("User already exists")) {
                        showExist();
                    }

                    else
                    {
                        String errorMsg = jObj.getString("error_msg");
                        showError();
                    }
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
                Toast.makeText(getApplicationContext(), "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "register");
                params.put("email", email);
                params.put("password", password);
                params.put("college", college);
                params.put("contactNo", phn);
                params.put("name", name);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onClick(View v)
    {
        email = etEmailRegister.getText().toString().trim();
        password = etPasswordRegister.getText().toString();
        college = etCollege.getSelectedItem().toString();
        phn = etPhnRegister.getText().toString();
        name = etNameRegister.getText().toString();
        String cCode = "91";
        String Regex = "[^\\d]";
        String PhoneDigits = phn.replaceAll(Regex, "");

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(email.matches(emailPattern) && PhoneDigits.length()==10)
        {
            if (!email.isEmpty() && !password.isEmpty() && !college.equals("COLLEGE") && !phn.isEmpty() && !name.isEmpty())
            {
                AlertDialog.Builder alert = new AlertDialog.Builder(RegisterActivity.this);
                //alert.setTitle("");
                alert.setMessage("Please confirm your number");
                final EditText input = new EditText(RegisterActivity.this);
                input.setText(phn, TextView.BufferType.EDITABLE);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setRawInputType(Configuration.KEYBOARD_12KEY);
                input.setTextColor(Color.BLACK);
                alert.setView(input);
                alert.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int whichButton)
                    {
                        srt = input.getEditableText().toString();

                        if(srt.length()==10) {
                            registerUser(email, password, college, srt, name);
                        }

                        else  Toast.makeText(getApplicationContext(), "Please enter valid Contact No", Toast.LENGTH_LONG).show();
                    }
                });
                /*alert.setNegativeButton("BACK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });*/
                final AlertDialog alertDialog = alert.create();
                alertDialog.show();

                //registerUser(email, password, college, phn, name);
            }

            else {
                Snackbar.make(v, "Please enter valid credentials!", Snackbar.LENGTH_LONG).show();
            }
        }

        else {
            Snackbar.make(v, "Please enter valid credentials!", Snackbar.LENGTH_LONG).show();
        }
    }

    private void showExist() {
        Toast.makeText(this, "User already exists.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showError() {
        Toast.makeText(this, "No or slow Internet connection. Please try again later. ", Toast.LENGTH_LONG).show();
    }

    private void registerUnsuccess()
    {
        Toast.makeText(this, "Please enter valid OTP to login", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void registerSuccess()
    {
        SessionManager session = new SessionManager(this);
        session.setLogin(true);
        Toast.makeText(this, "Registration Successful", Toast.LENGTH_LONG).show();
        getMyCollege(ob.token);

        /*if(st==1)
        {
            Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
            intent.putExtra("tod",tod);
            intent.putExtra("amount",amount);
            intent.putExtra("deliv",deliv);
            intent.putExtra("totalamount",totalamount);
            intent.putExtra("specialInst",inst);
            intent.putExtra("contact", contactNo);
            startActivity(intent);
            finish();
        }

        else{
            Intent intent = new Intent(RegisterActivity.this, CollegeActivity.class);
            startActivity(intent);
            finish();
        }*/
    }

    private void getMyCollege(final String token)
    {
        final String tag_string_req = "req_colleges";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_ME, new Response.Listener<String>()
        {

            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonobject = new JSONObject(response);

                    if (st == 1)
                    {
                        ob.name = jsonobject.getString("name");
                        ob.email = jsonobject.getString("email");

                        AppController.setList(getApplicationContext(), "name", ob);
                        AppController.setList(getApplicationContext(), "email", ob);

                        Intent intent = new Intent(RegisterActivity.this, ProfileActivity.class);
                        intent.putExtra("tod", tod);
                        intent.putExtra("amount", amount);
                        intent.putExtra("deliv", deliv);
                        intent.putExtra("totalamount", totalamount);
                        intent.putExtra("specialInst", inst);
                        intent.putExtra("contact", contactNo);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(RegisterActivity.this, CollegeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                    catch(JSONException e){
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

    public void verifyOtp(final String contactNo, final String value, final String email)
    {
        String tag_string_req = "req_register";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_VERIFY, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {

                try {

                    JSONObject jObj = new JSONObject(response);

                     String number = jObj.getString("nModified");

                    if (number.equals("1")) {
                        registerSuccess();
                    }
                    else {
                        registerUnsuccess();
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
            }
        }) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("tag", "register");
                params.put("contactNo", contactNo);
                params.put("value", value);
                params.put("email", email);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

}
